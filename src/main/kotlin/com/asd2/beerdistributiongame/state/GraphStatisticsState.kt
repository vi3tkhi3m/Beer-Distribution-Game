package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.GameGraphStatisticsHandler
import com.asd2.beerdistributiongame.gamelogic.configuration.RoleOwner
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.settings.Settings
import javafx.scene.chart.XYChart
import java.util.*

class GraphStatisticsState : State() {

    private val handler = GameGraphStatisticsHandler()
    private val standardChartToView = "outgoingOrder"
    override fun onEnter() {
        if (Context.chartToView.isEmpty()) setChartToView(standardChartToView)
        Context.loadScene(handler.getViewFile())
    }

    override fun onExit() {
        //todo this is only here to prevent code smells it's actually not a todo
    }

    fun notifyToGoBackToMainMenu() {
        setChartToView(standardChartToView)
        setState(MainMenuState())
    }

    fun notifyGoToGraph(chartValue: String) {
        setChartToView(chartValue)
        setState(GraphStatisticsState())
    }

    fun notifyGoToRankings() {
        setChartToView(standardChartToView)
        setState(LeaderboardState())
    }

    fun setChartToView(chartToView: String) {
        Context.chartToView = chartToView
    }

    fun getChartToView() = Context.chartToView
    fun getStatesOfGame() = Context.statesOfGame
    fun getUserName() = Settings.username
    fun getPlayerRoles() = Context.rolesInGame.playerRoles
    fun averageAllRoles(
            roleList: MutableList<String>, allStatisticList: ArrayList<StateOfGame>, valueToGet: String, playerRoles: HashMap<String, RoleOwner>)
            : MutableList<XYChart.Series<String, Number>> {
        val playerChartList = mutableListOf<XYChart.Series<String, Number>>()
        for (roleListCounter in 0 until roleList.size) {
            val playerChart = averageOneRole(allStatisticList, roleList[roleListCounter], valueToGet, playerRoles)
            playerChartList.add(playerChart)
        }
        return playerChartList
    }

    private fun averageOneRole(
            allStatisticList: ArrayList<StateOfGame>, role: String, valueToGet: String, playerRoles: HashMap<String, RoleOwner>)
            : XYChart.Series<String, Number> {
        var getValue = valueToGet
        val list = MutableList(allStatisticList.size) { 0.0 }
        var divideBy = 0
        var counter = 0
        var turnBefore = 1
        if (role == "CONSUMER") {
            turnBefore = 0
            getValue = "incomingOrder"
        } //makes it so that RETAILER incoming orders become the outgoing orders of the CONSUMER
        for (stateOfGame in allStatisticList) {
            for (playerRole in playerRoles) {
                for (playerState in stateOfGame.playerStates) {
                    if (playerState.value.player == playerRole.value.playerId &&
                            (playerRole.value.role.toString() == role || (turnBefore == 0 && playerRole.value.role.toString() == "RETAILER"))) {
                        list[counter] = list[counter] + (getValueFromPlayerstate(playerState.value, getValue))
                        divideBy += 1
                    }

                }
            }
            counter += 1
        }
        if (divideBy > 1) {
            divideBy /= allStatisticList.size
            for (i in 0 until allStatisticList.size) {
                list[i] = list[i] / divideBy
            }
        }
        val playerChart = XYChart.Series<String, Number>()
        playerChart.name = role
        for (i in 0 until allStatisticList.size) {
            playerChart.data.add(XYChart.Data((i + turnBefore).toString(), list[i]))
        }
        return playerChart
    }

    private fun getValueFromPlayerstate(values: StateOfPlayer, getValue: String): Int {


        return when (getValue) {
            "outgoingOrder" -> values.outgoingOrder
            "incomingOrder" -> values.incomingOrders.values.sum()
            "backlogs" -> values.backlogs.values.sum()
            "stock" -> values.stock
            "budget" -> values.budget
            else -> 0
        }
    }
}