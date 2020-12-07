package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.GameMode
import com.asd2.beerdistributiongame.gamelogic.configuration.OverviewType
import com.asd2.beerdistributiongame.state.GameConfigState
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import java.util.*

class GameConfigHandler : Handler<GameConfigState>() {

    private val maxAmountRoles: Int = 100

    @FXML lateinit var backToMenu: Button
    @FXML lateinit var openLobby: Button
    @FXML lateinit var gameName: TextField
    @FXML lateinit var gameMode: ComboBox<String>
    @FXML lateinit var overviewType: ComboBox<String>
    @FXML lateinit var initialStock: TextField

    @FXML var gameAgentComboBox: ComboBox<String> = ComboBox()

    @FXML lateinit var initialBudget: TextField
    @FXML lateinit var turnDuration: TextField
    @FXML lateinit var backlogCost: TextField
    @FXML lateinit var stockCost: TextField
    @FXML lateinit var productionCost: TextField
    @FXML lateinit var maxTurns: TextField
    @FXML lateinit var infiniteTurns: CheckBox

    @FXML lateinit var quantityOfFactories: TextField
    @FXML lateinit var quantityOfWarehouses: TextField
    @FXML lateinit var quantityOfWholesales: TextField
    @FXML lateinit var quantityOfRetailers: TextField

    @FXML lateinit var retailProfit: TextField
    @FXML lateinit var warehouseProfit: TextField
    @FXML lateinit var wholesaleProfit: TextField
    @FXML lateinit var factoryProfit: TextField

    @FXML lateinit var minCustomerDemand: TextField
    @FXML lateinit var maxCustomerDemand: TextField

    @FXML lateinit var acceptConfigAlert: AnchorPane
    @FXML lateinit var buttonConfirmOpenLobby: Button
    @FXML lateinit var buttonCancelOpenLobby: Button

    private val fieldsWithErrors = ArrayList<Control>()

    override fun getViewFile() = "views/GameConfig.fxml"

    @FXML
    fun initialize() {
        fillUsingGameConfig(Context.gameConfig)
        gameAgentComboBox.items.addAll(getAgentNamesStorage())
    }

    fun onBackToMainMenuPressed() = state.notifyToGoBackToMainMenu()

    fun onOpenLobbyPressed() = changeAcceptConfigAlertVisibility(true)

    fun onGameNameChanged() {
        Context.gameConfig.gameName = gameName.text
        setError(gameName, gameName.text.isEmpty())
    }

    fun onConfirmOpenLobbyPressed() = state.notifyToOpenLobby()

    fun onCancelOpenLobbyPressed() = changeAcceptConfigAlertVisibility(false)

    private fun changeAcceptConfigAlertVisibility(visibility: Boolean) {
        acceptConfigAlert.isVisible = visibility
    }

    fun onGameModeChange() {
        this.fillUsingGameConfig(Context.gameConfig)
        when (this.gameMode.selectionModel.selectedItem) {
            "Linear" -> Context.gameConfig.gameMode = GameMode.LINEAR
            "Pyramid" -> Context.gameConfig.gameMode = GameMode.PYRAMID
            "Graph" -> Context.gameConfig.gameMode = GameMode.GRAPH
        }
        changeRolesGameMode(Context.gameConfig.gameMode)
    }

    fun onOverviewTypeChange() {
        when (this.overviewType.selectionModel.selectedItem) {
            "Local" -> Context.gameConfig.overviewType = OverviewType.LOCAL
            "Global" -> Context.gameConfig.overviewType = OverviewType.GLOBAL
        }
    }

    fun onInitialStockChange() {
        if (checkParseInt(initialStock))
            Context.gameConfig.initialStock = Integer.parseInt(initialStock.text)
    }

    fun onInitialBudgetChange() {
        if (checkParseInt(initialBudget))
            Context.gameConfig.initialBudget = Integer.parseInt(initialBudget.text)
    }

    fun onTurnDurationChange() {
        if (checkParseInt(turnDuration))
            Context.gameConfig.turnDuration = Integer.parseInt(turnDuration.text)
    }

    fun onBacklogCostChanged() {
        if (checkParseInt(backlogCost))
            Context.gameConfig.backlogCost = Integer.parseInt(backlogCost.text)
    }

    fun onStockCostChanged() {
        if (checkParseInt(stockCost))
            Context.gameConfig.stockCost = Integer.parseInt(stockCost.text)
    }

    fun onProductionCostChanged() {
        if (checkParseInt(productionCost))
            Context.gameConfig.productionCost = Integer.parseInt(productionCost.text)
    }

    fun onMaxTurnsChanged() {
        if (checkParseInt(maxTurns))
            Context.gameConfig.maxTurns = Integer.parseInt(maxTurns.text)
    }

    fun onInfiniteTurnsChanged() {
        if(infiniteTurns.isSelected){
            Context.gameConfig.infiniteTurns = infiniteTurns.isSelected
            maxTurns.isDisable = true
        }else{
            maxTurns.isDisable = false
        }
    }

    fun onQuantityFactoriesChanged() {
        if (!checkParseInt(quantityOfFactories)|| !checkParseInt(quantityOfWarehouses))
            return

        val amountOfFactories = Integer.parseInt(quantityOfFactories.text)
        val amountOfWarehouses = Integer.parseInt(quantityOfWarehouses.text)

        setError(quantityOfFactories,  amountOfFactories > maxAmountRoles || amountOfFactories == 0)
        Context.gameConfig.quantityOfFactories = amountOfFactories

        if (Context.gameConfig.gameMode == GameMode.PYRAMID)
            setError(quantityOfFactories,  amountOfFactories > amountOfWarehouses)
    }

    fun onQuantityWarehousesChanged() {
        if (!checkParseInt(quantityOfWarehouses) || !checkParseInt(quantityOfFactories) || !checkParseInt(quantityOfWholesales))
            return

        val amountOfWholesalers = Integer.parseInt(quantityOfWholesales.text)
        val amountOfFactories = Integer.parseInt(quantityOfFactories.text)
        val amountOfWarehouses = Integer.parseInt(quantityOfWarehouses.text)

        if (Context.gameConfig.gameMode == GameMode.LINEAR || Context.gameConfig.gameMode == GameMode.GRAPH) {
            Context.gameConfig.quantityOfWarehouses = amountOfWarehouses
        }
        else if (Context.gameConfig.gameMode == GameMode.PYRAMID) {
            Context.gameConfig.quantityOfWarehouses = amountOfWarehouses
            setError(quantityOfWarehouses, amountOfWarehouses < amountOfFactories
                    || amountOfWarehouses > amountOfWholesalers
                    || amountOfWarehouses > maxAmountRoles
                    || amountOfWarehouses == 0)
        }
    }

    fun onQuantityWholesalesChanged() {
        if (!checkParseInt(quantityOfWholesales)|| !checkParseInt(quantityOfWarehouses) || !checkParseInt(quantityOfRetailers))
            return

        val amountOfWholesalers = Integer.parseInt(quantityOfWholesales.text)
        val amountOfRetailers = Integer.parseInt(quantityOfRetailers.text)
        val amountOfWarehouses = Integer.parseInt(quantityOfWarehouses.text)

        if (Context.gameConfig.gameMode == GameMode.LINEAR || Context.gameConfig.gameMode == GameMode.GRAPH) {
            Context.gameConfig.quantityOfWholesales = amountOfWholesalers
        }
        else if (Context.gameConfig.gameMode == GameMode.PYRAMID) {
            Context.gameConfig.quantityOfWholesales = amountOfWholesalers
            setError(quantityOfWholesales, amountOfWholesalers < amountOfWarehouses
                    || amountOfWholesalers > amountOfRetailers
                    || amountOfWholesalers > maxAmountRoles
                    || amountOfWholesalers == 0 )
        }
    }

    fun onQuantityRetailersChanged() {
        if (!checkParseInt(quantityOfRetailers) || !checkParseInt(quantityOfWholesales))
            return

        val amountOfWholesalers = Integer.parseInt(quantityOfWholesales.text)
        val amountOfRetailers = Integer.parseInt(quantityOfRetailers.text)

        if (Context.gameConfig.gameMode == GameMode.LINEAR || Context.gameConfig.gameMode == GameMode.GRAPH) {
            Context.gameConfig.quantityOfRetailers = amountOfRetailers
        }
        else if (Context.gameConfig.gameMode == GameMode.PYRAMID) {
            Context.gameConfig.quantityOfRetailers = amountOfRetailers
            setError(quantityOfRetailers, amountOfRetailers < amountOfWholesalers
                    || amountOfRetailers > maxAmountRoles
                    || amountOfRetailers == 0 )
        }
    }

    fun customerDemandChanged() {
        if (checkParseInt(minCustomerDemand) && checkParseInt(maxCustomerDemand)) {
            val demand = Integer.parseInt(minCustomerDemand.text)
            Context.gameConfig.minCustomerDemand = demand
            setError(minCustomerDemand, demand > Context.gameConfig.maxCustomerDemand)
        }
        if (checkParseInt(maxCustomerDemand)) {
            val demand = Integer.parseInt(maxCustomerDemand.text)
            Context.gameConfig.maxCustomerDemand = demand
            setError(maxCustomerDemand, demand < Context.gameConfig.minCustomerDemand)
        }
    }

    fun onRetailProfitChanged() {
        if (checkParseInt(retailProfit))
            Context.gameConfig.retailProfit = Integer.parseInt(retailProfit.text)
    }

    fun onWholesaleProfitChanged() {
        if (checkParseInt(wholesaleProfit))
            Context.gameConfig.wholesaleProfit = Integer.parseInt(wholesaleProfit.text)
    }

    fun onWarehouseProfitChanged() {
        if (checkParseInt(warehouseProfit))
            Context.gameConfig.warehouseProfit = Integer.parseInt(warehouseProfit.text)
    }

    fun onFactoryProfitChanged() {
        if (checkParseInt(factoryProfit))
            Context.gameConfig.factoryProfit = Integer.parseInt(factoryProfit.text)
    }

    private fun getAgentNamesStorage() : List<String> = StorageManager.getAgentNames()

    fun onGameAgentComboBoxChanged(){
        Context.gameConfig.standardGameAgent = gameAgentComboBox.selectionModel.selectedItem
    }
    
    private fun fillUsingGameConfig(gameConfig: GameConfig) {
        gameName.text = gameConfig.gameName
        onGameNameChanged()

        initialStock.text = gameConfig.initialStock.toString()
        initialBudget.text = gameConfig.initialBudget.toString()
        turnDuration.text = gameConfig.turnDuration.toString()
        backlogCost.text = gameConfig.backlogCost.toString()
        stockCost.text = gameConfig.stockCost.toString()
        productionCost.text = gameConfig.productionCost.toString()
        maxTurns.text = gameConfig.maxTurns.toString()
        infiniteTurns.isSelected = gameConfig.infiniteTurns

        quantityOfFactories.text = gameConfig.quantityOfFactories.toString()
        quantityOfWarehouses.text = gameConfig.quantityOfWarehouses.toString()
        quantityOfWholesales.text = gameConfig.quantityOfWholesales.toString()
        quantityOfRetailers.text = gameConfig.quantityOfRetailers.toString()
        disableQuantityOfRoles(true)

        gameAgentComboBox.items.add("default")
        Context.gameConfig.standardGameAgent = gameAgentComboBox.selectionModel.select(0).toString()

        retailProfit.text = gameConfig.retailProfit.toString()
        warehouseProfit.text = gameConfig.warehouseProfit.toString()
        wholesaleProfit.text = gameConfig.wholesaleProfit.toString()
        factoryProfit.text = gameConfig.factoryProfit.toString()

        minCustomerDemand.text = gameConfig.minCustomerDemand.toString()
        maxCustomerDemand.text = gameConfig.maxCustomerDemand.toString()
    }

    private fun checkParseInt(textField: TextField): Boolean {
        return try {
            Integer.parseInt(textField.text)
            setError(textField, false)
            true
        } catch (e: NumberFormatException) {
            setError(textField, true)
            false
        }
    }

    private fun setError(textField: TextField, hasError: Boolean) {
        if (hasError) {
            addError(textField)
            if (!fieldsWithErrors.contains(textField))
                fieldsWithErrors.add(textField)
        } else {
            removeError(textField)
            fieldsWithErrors.remove(textField)
        }
        openLobby.isDisable = !fieldsWithErrors.isEmpty()
    }

    private fun fillQuantityOfRoles(_quantityOfFactories: Int, _quantityOfWarehouses: Int, _quantityOWholesales: Int, _quantityOfRetailers: Int) {
        Context.gameConfig.quantityOfFactories = _quantityOfFactories
        Context.gameConfig.quantityOfWarehouses = _quantityOfWarehouses
        Context.gameConfig.quantityOfWholesales = _quantityOWholesales
        Context.gameConfig.quantityOfRetailers = _quantityOfRetailers

        quantityOfFactories.text = _quantityOfFactories.toString()
        quantityOfRetailers.text = _quantityOfRetailers.toString()
        quantityOfWarehouses.text = _quantityOfWarehouses.toString()
        quantityOfWholesales.text = _quantityOWholesales.toString()
    }

    private fun disableQuantityOfRoles(fieldDisable: Boolean) {
        quantityOfFactories.isDisable = fieldDisable
        quantityOfRetailers.isDisable = fieldDisable
        quantityOfWarehouses.isDisable = fieldDisable
        quantityOfWholesales.isDisable = fieldDisable
    }
    private fun changeRolesGameMode(gameMode: GameMode){
        when (gameMode) {
            GameMode.LINEAR -> {
                fillQuantityOfRoles(1, 1, 1, 1)
                disableQuantityOfRoles(true)
            }
            GameMode.PYRAMID -> {
                fillQuantityOfRoles(1, 2, 4, 6)
                disableQuantityOfRoles(false)
            }
            GameMode.GRAPH -> {
                fillQuantityOfRoles(3, 8, 6, 3)
                disableQuantityOfRoles(false)
            }
        }
    }

}





