package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.state.ReplayLinearState
import javafx.fxml.FXML
import javafx.scene.control.ComboBox
import javafx.scene.control.TextArea
import javafx.scene.control.TextField

class ReplayLinearViewHandler : Handler<ReplayLinearState>() {
    @FXML lateinit var retailerName: TextField
    @FXML lateinit var retailerInventory: TextField
    @FXML lateinit var retailerIncomingOrder: TextArea
    @FXML lateinit var retailerOutgoingOrder: TextArea
    @FXML lateinit var retailerOutgoingBeer: TextArea
    @FXML lateinit var retailerIncomingBeer: TextArea

    @FXML lateinit var wholesalerName: TextField
    @FXML lateinit var wholesalerInventory: TextField
    @FXML lateinit var wholesalerIncomingOrder: TextArea
    @FXML lateinit var wholesalerOutgoingOrder: TextArea
    @FXML lateinit var wholesalerOutgoingBeer: TextArea
    @FXML lateinit var wholesalerIncomingBeer: TextArea

    @FXML lateinit var distributorName: TextField
    @FXML lateinit var distributorInventory: TextField
    @FXML lateinit var distributorIncomingOrder: TextArea
    @FXML lateinit var distributorOutgoingOrder: TextArea
    @FXML lateinit var distributorOutgoingBeer: TextArea
    @FXML lateinit var distributorIncomingBeer: TextArea

    @FXML lateinit var factoryName: TextField
    @FXML lateinit var factoryInventory: TextField
    @FXML lateinit var factoryIncomingOrder: TextArea
    @FXML lateinit var factoryOutgoingOrder: TextArea
    @FXML lateinit var factoryOutgoingBeer: TextArea
    @FXML lateinit var factoryIncomingBeer: TextArea
    @FXML lateinit var factoryProductionDelay: TextArea

    @FXML lateinit var turnComboBox: ComboBox<Int>

    @FXML
    fun initialize() {
        setProperties()
    }

    override fun getViewFile(): String = "views/LinearOverview.fxml"

    fun onMainMenuPressed() = state.notifyToGoBackToMainMenu()

    fun onFinalResultsPressed() = state.notifyToGoToFinalResults()

    fun onNextPressed() = state.notifyToGoToNextTurn()

    fun onPreviousPressed() = state.notifyToGoToPreviousTurn()

    fun onRetailerClicked() = state.notifyToGoToSupplyChainEntityView()

    fun onWholesalerClicked() = state.notifyToGoToSupplyChainEntityView()

    fun onDistributorClicked() = state.notifyToGoToSupplyChainEntityView()

    fun onFactoryClicked() = state.notifyToGoToSupplyChainEntityView()

    fun onTurnValueChanged() = state.notifyToGoToTurn(turnComboBox.value)

    private fun setProperties() {
        for (i in 1..state.maxTurns) turnComboBox.items.addAll(i)
        turnComboBox.value = state.turnNumber
        setRetailerProperties()
        setWholesalerProperties()
        setDistributorProperties()
        setFactoryProperties()
    }

    private fun setRetailerProperties() {
        retailerName.text = state.retailerDetails.name
        retailerInventory.text = state.retailerDetails.inventory.toString()
        retailerIncomingOrder.text = state.retailerDetails.incomingOrders.toString()
        retailerOutgoingOrder.text = state.retailerDetails.outgoingOrders.toString()
        retailerIncomingBeer.text = state.retailerDetails.incomingBeer.toString()
        retailerOutgoingBeer.text = state.retailerDetails.outgoingBeer.toString()
    }

    private fun setWholesalerProperties() {
        wholesalerName.text = state.wholesalerDetails.name
        wholesalerInventory.text = state.wholesalerDetails.inventory.toString()
        wholesalerIncomingOrder.text = state.wholesalerDetails.incomingOrders.toString()
        wholesalerOutgoingOrder.text = state.wholesalerDetails.outgoingOrders.toString()
        wholesalerIncomingBeer.text = state.wholesalerDetails.incomingBeer.toString()
        wholesalerOutgoingBeer.text = state.wholesalerDetails.outgoingBeer.toString()
    }

    private fun setDistributorProperties() {
        distributorName.text = state.distributorDetails.name
        distributorInventory.text = state.distributorDetails.inventory.toString()
        distributorIncomingOrder.text = state.distributorDetails.incomingOrders.toString()
        distributorOutgoingOrder.text = state.distributorDetails.outgoingOrders.toString()
        distributorIncomingBeer.text = state.distributorDetails.incomingBeer.toString()
        distributorOutgoingBeer.text = state.distributorDetails.outgoingBeer.toString()
    }

    private fun setFactoryProperties() {
        factoryName.text = state.factoryDetails.name
        factoryInventory.text = state.factoryDetails.inventory.toString()
        factoryIncomingOrder.text = state.factoryDetails.incomingOrders.toString()
        factoryOutgoingOrder.text = state.factoryDetails.outgoingOrders.toString()
        factoryIncomingBeer.text = state.factoryDetails.incomingBeer.toString()
        factoryOutgoingBeer.text = state.factoryDetails.outgoingBeer.toString()
        factoryProductionDelay.text = state.factoryDetails.productionDelay.toString()
    }
}