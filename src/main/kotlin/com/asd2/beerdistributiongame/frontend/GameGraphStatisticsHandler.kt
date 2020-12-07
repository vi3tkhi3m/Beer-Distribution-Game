package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.configuration.getRoles
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.state.GraphStatisticsState
import javafx.fxml.FXML
import javafx.scene.chart.LineChart
import javafx.scene.text.Text
import java.util.*

class GameGraphStatisticsHandler : Handler<GraphStatisticsState>() {

    @FXML lateinit var statisticsGraph: LineChart<String, Number>
    @FXML lateinit var usernameText: Text
    @FXML lateinit var chartToViewValue: Text

    override fun getViewFile(): String = "views/GameGraphStatistics.fxml"

    fun initialize() {
        usernameText.text = state.getUserName()
        chartToViewValue.text = state.getChartToView()
        fillGraph(state.getChartToView(), state.getStatesOfGame(), getRoles<Roles>())
    }

    fun onBackToMenu() = state.notifyToGoBackToMainMenu()

    fun onGoToRankings() = state.notifyGoToRankings()

    fun onGoToGraphOutgoingOrders() = state.notifyGoToGraph("outgoingOrder")

    fun onGoToGraphBudget() = state.notifyGoToGraph("budget")

    fun onGoToGraphBacklog() = state.notifyGoToGraph("backlogs")

    fun onGoToGraphStock() = state.notifyGoToGraph("stock")

    private fun fillGraph(valueToGet: String, allStatisticList: ArrayList<StateOfGame>, roleList: MutableList<String>) {
        if (valueToGet == "outgoingOrder") {
            roleList.add(0, "CONSUMER")
        }
        statisticsGraph.data.addAll(state.averageAllRoles(roleList, allStatisticList, valueToGet, state.getPlayerRoles()))
    }
}