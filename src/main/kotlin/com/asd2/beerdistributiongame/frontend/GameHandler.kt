package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.PreviousTurnModel
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.settings.Translations
import com.asd2.beerdistributiongame.state.GameState
import javafx.fxml.FXML
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Handles all incoming and outgoing information from the client game view
 */
class GameHandler : Handler<GameState>() {

    @FXML lateinit var previousOrderView: AnchorPane
    @FXML lateinit var backToOrdersButton: Button

    @FXML lateinit var incomingOrdersLabel: Label
    @FXML lateinit var outgoingOrdersLabel: Label

    @FXML lateinit var incOrderTable: TableView<PreviousTurnModel.PreviousOrder>
    @FXML lateinit var incOrderTurn: TableColumn<PreviousTurnModel.PreviousOrder, String>
    @FXML lateinit var incOrderAmount: TableColumn<PreviousTurnModel.PreviousOrder, String>
    @FXML lateinit var incProductionCost: TableColumn<PreviousTurnModel.PreviousOrder, String>

    @FXML lateinit var outOrderTable: TableView<PreviousTurnModel.PreviousOrder>
    @FXML lateinit var outOrderTurn: TableColumn<PreviousTurnModel.PreviousOrder, String>
    @FXML lateinit var outOrderAmount: TableColumn<PreviousTurnModel.PreviousOrder, String>
    @FXML lateinit var outProductionCost: TableColumn<PreviousTurnModel.PreviousOrder, String>

    @FXML lateinit var labelRemainingTime: Label
    @FXML lateinit var labelTurn: Label

    @FXML lateinit var labelIncomingOrdersValue: Label
    @FXML lateinit var labelOutgoingBeerValue: Label
    @FXML lateinit var labelIncomingBeerValue: Label

    @FXML lateinit var tooltipIncomingOrders: Tooltip
    @FXML lateinit var tooltipOutgoingOrders: Tooltip

    @FXML lateinit var labelBudget: Label
    @FXML lateinit var labelInventory: Label
    @FXML lateinit var labelBacklog: Label

    @FXML lateinit var tooltipBudgetStockCost: Tooltip
    @FXML lateinit var tooltipInventoryStockCost: Tooltip
    @FXML lateinit var tooltipBacklogStockCost: Tooltip

    @FXML lateinit var textBoxOutgoingOrders: TextField


    @FXML lateinit var buttonMainMenu: Label
    @FXML lateinit var buttonPreviousOrders: Button
    @FXML lateinit var buttonPlaceOrder: Button

    @FXML lateinit var activateGameAgent: Button
    @FXML lateinit var selectGameAgentMenu: AnchorPane
    @FXML var selectGameAgent: ComboBox<String> = ComboBox()

    @FXML lateinit var buttonStopGame: Button
    @FXML lateinit var stopGameAlert: AnchorPane
    @FXML lateinit var buttonConfirmStopGame: Button
    @FXML lateinit var buttonCancelStopGame: Button

    var remainingTime: String
        get() = labelRemainingTime.text.toString()
        set(value) { labelRemainingTime.text = "Remaining time: " + value }

    private var currentTurn: String
        get() = labelTurn.text.toString().removePrefix("Turn: ")
        set(value) { labelTurn.text = String.format("Turn: $value") }

    private var incomingOrders: Int
        get() = labelIncomingOrdersValue.text.toInt()
        set(value) { labelIncomingOrdersValue.text = value.toString() }

    private var outgoingBeer: Int
        get() = labelOutgoingBeerValue.text.toInt()
        set(value) { labelOutgoingBeerValue.text = value.toString() }

    private var outgoingOrders: Int
        get() = textBoxOutgoingOrders.text.toInt()
        set(value) { textBoxOutgoingOrders.text = value.toString() }

    private var incomingBeer: Int
        get() = labelIncomingBeerValue.text.toInt()
        set(value) { labelIncomingBeerValue.text = value.toString() }

    private var budget: String
        get() = labelBudget.text.toString().removePrefix("Budget: ")
        set(value) { labelBudget.text = String.format("Budget: $value") }

    private var inventory: String
        get() = labelInventory.text.toString().removePrefix("Inventory: ")
        set(value) { labelInventory.text = String.format("Inventory: $value") }

    private var backlog: String
        get() = labelBacklog.text.toString().removePrefix("Backlog: ")
        set(value) { labelBacklog.text = String.format("Backlog: $value") }

    private var gameAgentActivated: Boolean = false
    private lateinit var selectedGameAgent : String

    @FXML
    fun initialize() {
        state.handler = this
        if (!Context.isHost) {
            buttonStopGame.isVisible = false
            updateGameViewData()
        }

        selectGameAgent.items.addAll(StorageManager.getAgentNames())
    }

    override fun getViewFile(): String = "views/GameView.fxml"

    fun onMouseClickedMainMenu() {
        state.notifyToGoBackToMainMenu()
    }

    fun onKeyReleasedOutgoingOrders() {
        if (!isNumeric(textBoxOutgoingOrders.text)) {
            textBoxOutgoingOrders.text = Regex("[^0-9]").replace(textBoxOutgoingOrders.text, "")
        }
    }

    fun onActionButtonPlaceOrder() {
        state.notifyToPlaceOrder(outgoingOrders)
    }

    fun onActionButtonPreviousOrders() {
        previousOrderView.isVisible = true
        loadInfoFromUser()
        changeUIState(true)
    }

    fun onActionBackToOrders() {
        previousOrderView.isVisible = false
        changeUIState(false)
    }

    fun onShowingTooltipIncomingOrders() {
        tooltipIncomingOrders.text = "Value incoming orders: ${Context.gameConfig.retailProfit}" +
                "Fulfillable orders: $incomingOrders" +
                "Total: \$${incomingOrders * Context.gameConfig.retailProfit}"
    }

    fun onShowingTooltipOutgoingOrders() {
        tooltipOutgoingOrders.text = "Cost outgoing orders:${Context.gameConfig.productionCost}\n" +
                "Orders: $outgoingOrders" +
                "Total: \$${outgoingOrders * Context.gameConfig.productionCost}"
    }

    fun onShowingTooltipStockCost() {
        val tooltiptext = "Cost for each stock item: ${Context.gameConfig.backlogCost} \n\r" +
                "Items in stock: $inventory \n\r" +
                "Cost of stock: ${Context.gameConfig.stockCost}"
        tooltipInventoryStockCost.text = tooltiptext
        tooltipBacklogStockCost.text = tooltiptext
        tooltipBudgetStockCost.text = tooltiptext
    }

    fun nextTurn() {
        updateGameViewData()
    }

    @FXML
    fun getTranslationData() {
        backToOrdersButton.text = Translations.get("backToOrdersButton")

        incomingOrdersLabel.text = Translations.get("previousOrdersIncomingOrders")
        outgoingOrdersLabel.text = Translations.get("previousOrdersOutgoingOrders")

        incOrderTurn.text = Translations.get("previousOrdersTurn")
        incOrderAmount.text = Translations.get("previousOrdersAmount")
        incProductionCost.text = Translations.get("previousOrdersCost")

        outOrderTurn.text = Translations.get("previousOrdersTurn")
        outOrderAmount.text = Translations.get("previousOrdersAmount")
        outProductionCost.text = Translations.get("previousOrdersCost")

        incOrderTurn.cellValueFactory = PropertyValueFactory<PreviousTurnModel.PreviousOrder, String>("turn")
        incOrderAmount.cellValueFactory = PropertyValueFactory<PreviousTurnModel.PreviousOrder, String>("amount")
        incProductionCost.cellValueFactory = PropertyValueFactory<PreviousTurnModel.PreviousOrder, String>("cost")

        outOrderTurn.cellValueFactory = PropertyValueFactory<PreviousTurnModel.PreviousOrder, String>("turn")
        outOrderAmount.cellValueFactory = PropertyValueFactory<PreviousTurnModel.PreviousOrder, String>("amount")
        outProductionCost.cellValueFactory = PropertyValueFactory<PreviousTurnModel.PreviousOrder, String>("cost")
    }

    /**
     * Get all data from the context that is needed to fill the labels and textfields in the game view
     * This function also updates the existing view data when a turn passes
     */
    private fun updateGameViewData() {
        val playerRolekey  = Context.rolesInGame.playerRoles.filter{ it.value.playerId == Settings.uuid }.keys.first()
        getTranslationData()
        this.currentTurn = state.getCurrentTurnNumber().toString()
        val currentState = Context.statesOfGame.last().playerStates[playerRolekey]

        labelTurn.text = "Turn: " + Context.beerDistributionGame!!.gameFlow.currentTurn.toString()

        var totalIncomingOrders = 0
        Context.statesOfGame.last().playerStates[playerRolekey]!!.incomingOrders.values.forEach { totalIncomingOrders += it }
        incomingOrders = totalIncomingOrders
        outgoingOrders = currentState!!.outgoingOrder
        incomingBeer = currentState.incomingDelivery

        var totalOutgoingBeer = 0
        Context.statesOfGame.last().playerStates[playerRolekey]!!.outgoingDeliveries.values.forEach { totalOutgoingBeer += it }
        outgoingBeer = totalOutgoingBeer

        budget = currentState.budget.toString()
        inventory = currentState.stock.toString()

        var totalBacklog = 0
        currentState.backlogs.values.forEach { totalBacklog += it }
        backlog = totalBacklog.toString()

        labelIncomingOrdersValue.text = incomingOrders.toString()
        labelIncomingBeerValue.text = incomingBeer.toString()
        labelBacklog.text = backlog
        labelBudget.text = budget
        labelInventory.text = inventory
        labelOutgoingBeerValue.text = outgoingBeer.toString()
        textBoxOutgoingOrders.text = outgoingOrders.toString()
    }

    private fun loadInfoFromUser() {
        incOrderTable.items.clear()
        incOrderTable.items.addAll(state.loadPreviousTurnInfo().previousIncomingOrders)

        outOrderTable.items.clear()
        outOrderTable.items.addAll(state.loadPreviousTurnInfo().previousOutgoingOrders)
    }

    private fun isNumeric(input: String): Boolean =
            try {
                input.toDouble()
                true
            } catch (e: NumberFormatException) {
                false
            }

    fun onActivateGameAgentPressed() {
        if (!gameAgentActivated) {
            changeSelectGameAgentMenuVisibility(true)
        } else {
            GlobalScope.launch(Dispatchers.Main) {
                val response = state.notifyToDeactivateGameAgent()
                if (!response.success) {
                    showAlert(response.message + "De gekozen gameagent is niet uitgeschakeld.")
                } else {
                    gameAgentActivated = false
                    changeTextActivateGameAgentButton()
                    changeUIState(false)
                    showAlert(response.message + "De gekozen gameagent is uitgeschakeld.")
                }
            }
        }
    }

    fun onAcceptGameAgentPressed() {
        selectedGameAgent = this.selectGameAgent.selectionModel.selectedItem.toString()
        GlobalScope.launch(Dispatchers.Main) {
            val response = state.notifyToAcceptGameAgent(selectedGameAgent)
            if (!response.success) {
                showAlert(response.message + "De gekozen gameagent is niet ingeschakeld.")
            } else {
                gameAgentActivated = true
                changeTextActivateGameAgentButton()
                changeSelectGameAgentMenuVisibility(false)
                showAlert(response.message + "De gekozen gameagent is ingeschakeld.")
            }
        }
    }

    fun onCancelGameAgentPressed() {
        changeSelectGameAgentMenuVisibility(false)
    }

    /**
     * Change the state of the ui elements
     * @state the state to which the elements need to be set
     */
    private fun changeUIState(state: Boolean) {
        buttonMainMenu.isDisable = state
        buttonPreviousOrders.isDisable = state
        activateGameAgent.isDisable = state

        if (gameAgentActivated) {
            buttonPlaceOrder.isDisable = gameAgentActivated
            textBoxOutgoingOrders.isDisable = gameAgentActivated
        } else {
            buttonPlaceOrder.isDisable = state
            textBoxOutgoingOrders.isDisable = state
        }
    }

    private fun changeTextActivateGameAgentButton() {
        if (!gameAgentActivated) {
            activateGameAgent.text = "Activate gameagent"
        } else {
            activateGameAgent.text = "Deactivate gameagent"
        }
    }

    private fun changeSelectGameAgentMenuVisibility(visibility: Boolean) {
        selectGameAgentMenu.isVisible = visibility
        changeUIState(visibility)
    }

    fun stopGamePressed() {
        stopGameAlert.isVisible = true
        changeUIState(true)
    }

    fun onStopGamePressed() {
        state.notifyToEndGame()
    }

    fun onCancelPressed() {
        stopGameAlert.isVisible = false
        changeUIState(false)
    }

    fun activateGameStoppedAlert() {
        state.notifyToPersonalResults()
    }
}