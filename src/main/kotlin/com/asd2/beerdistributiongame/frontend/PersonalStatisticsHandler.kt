package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.replay.PersonStatistics
import com.asd2.beerdistributiongame.state.PersonalStatisticsState
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.TextField
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.text.Text

class PersonalStatisticsHandler : Handler<PersonalStatisticsState>() {
    @FXML lateinit var username: Text
    @FXML lateinit var startingBudget: TextField
    @FXML lateinit var personalStatisticsTable: TableView<PersonStatistics>
    @FXML lateinit var rounds: TableColumn<PersonStatistics,Int>
    @FXML lateinit var incomingOrders: TableColumn<PersonStatistics,Int>
    @FXML lateinit var outgoingOrder: TableColumn<PersonStatistics,Int>
    @FXML lateinit var budget: TableColumn<PersonStatistics,Int>
    @FXML lateinit var stock: TableColumn<PersonStatistics,Int>
    @FXML lateinit var backlog: TableColumn<PersonStatistics,Int>
    @FXML lateinit var gameAgent: TableColumn<PersonStatistics, Boolean>
    @FXML lateinit var incomingDelivery: TableColumn<PersonStatistics, Int>
    @FXML lateinit var outgoingDeliveries: TableColumn<PersonStatistics, Int>

    @FXML
    fun initialize() {
        setProperties()
        setData()
    }

    override fun getViewFile(): String = "views/GamePersonalStatistics.fxml"

    fun onGoToGraph() = state.notifyGoToGraph()

    fun onBackToMenu() = state.notifyGoToMainMenu()

    fun onGoToRankings() = state.notifyGoToRankings()

    fun onGoToGameView() = state.notifyGoToReplayLinear()

    fun onGameView() {
        val selectedTurn = personalStatisticsTable.selectionModel.selectedItem
        if(selectedTurn != null){
            Context.replayTurnNumber = selectedTurn.turnNumber
            state.notifyGoToReplayLinear()
        } else showAlert("Please select a turn", Alert.AlertType.ERROR, "Error", "Error")
    }

    private fun setData(){
        username.text = state.getUsername()
        startingBudget.text = state.getInitialBudget().toString()
        personalStatisticsTable.items.addAll(state.getPersonalStatistics(state.getUUID()))
    }

    private fun setProperties(){
        rounds.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("turnNumber")
        incomingOrders.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("incomingOrders")
        outgoingOrder.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("outgoingOrder")
        incomingDelivery.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("incomingDelivery")
        outgoingDeliveries.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("outgoingDeliveries")
        gameAgent.cellValueFactory = PropertyValueFactory<PersonStatistics, Boolean>("gameAgentActive")
        budget.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("budget")
        stock.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("stock")
        backlog.cellValueFactory = PropertyValueFactory<PersonStatistics, Int>("backlog")
    }
}
