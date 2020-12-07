package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.configuration.getRoles
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.settings.Settings
import javafx.scene.chart.XYChart
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GraphStatisticsStateIntegrationTest {
    @Before
    fun setup() {
        ReplayLobbyState().loadGame("./src/test/resources/graphStatisticsSaveData/", "oiutgx32-5678-1234-fdse-sdkalske0513")
    }// loads in a game with 2 Retailers to show that the code works with every gamemode

    @After
    fun afterTests() {
        ReplayLobbyState().loadGame("./src/test/resources/graphStatisticsSaveData/", "asdfux32-1234-4651-b6e6-sdkalske2310")
    }//This resets the gamestate to a linear so it does not cause other integration tests to fail

    @Test
    fun testAverageAllRoles() {
        val allStatisticList = Context.statesOfGame


        val testValuesOutgoingOrders = mutableListOf(
                "[Data[0,10.0,null], Data[1,15.0,null], Data[2,20.0,null]]",
                "[Data[1,11.5,null], Data[2,17.5,null], Data[3,30.0,null]]",
                "[Data[1,10.0,null], Data[2,24.0,null], Data[3,27.0,null]]",
                "[Data[1,14.0,null], Data[2,26.0,null], Data[3,29.0,null]]",
                "[Data[1,13.0,null], Data[2,25.0,null], Data[3,28.0,null]]"

        )
        val testValuesBudget = mutableListOf(
                "[Data[1,100.0,null], Data[2,65.0,null], Data[3,105.0,null]]",
                "[Data[1,100.0,null], Data[2,100.0,null], Data[3,100.0,null]]",
                "[Data[1,100.0,null], Data[2,100.0,null], Data[3,100.0,null]]",
                "[Data[1,100.0,null], Data[2,100.0,null], Data[3,100.0,null]]"
        )
        val testValuesStock = mutableListOf(
                "[Data[1,15.0,null], Data[2,7.0,null], Data[3,0.0,null]]",
                "[Data[1,10.0,null], Data[2,3.0,null], Data[3,0.0,null]]",
                "[Data[1,0.0,null], Data[2,0.0,null], Data[3,0.0,null]]",
                "[Data[1,9.0,null], Data[2,0.0,null], Data[3,0.0,null]]"
        )
        val testValuesBacklogs = mutableListOf(
                "[Data[1,4.5,null], Data[2,9.0,null], Data[3,3.0,null]]",
                "[Data[1,2.0,null], Data[2,3.0,null], Data[3,3.0,null]]",
                "[Data[1,3.0,null], Data[2,3.0,null], Data[3,20.0,null]]",
                "[Data[1,3.0,null], Data[2,3.0,null], Data[3,3.0,null]]"
        )
        val testValuesDoNotExist = mutableListOf(
                "[Data[1,0.0,null], Data[2,0.0,null], Data[3,0.0,null]]",
                "[Data[1,0.0,null], Data[2,0.0,null], Data[3,0.0,null]]",
                "[Data[1,0.0,null], Data[2,0.0,null], Data[3,0.0,null]]",
                "[Data[1,0.0,null], Data[2,0.0,null], Data[3,0.0,null]]"
        )
        val testValuesArray = mutableListOf(testValuesOutgoingOrders, testValuesBudget, testValuesStock, testValuesBacklogs, testValuesDoNotExist)
        val valueToGetArray = arrayListOf("outgoingOrder", "budget", "stock", "backlogs", "doesNotExist")

        for (j in 0 until testValuesArray.size) {
            val chartValues = mutableListOf<XYChart.Series<String, Number>>()
            val roleList = getRoles<Roles>()
            if (valueToGetArray[j] == "outgoingOrder") roleList.add(0, "CONSUMER")
            chartValues.addAll(GraphStatisticsState().averageAllRoles(roleList, allStatisticList, valueToGetArray[j], GraphStatisticsState().getPlayerRoles()))
            for (i in 0 until testValuesArray[j].size) {
                Assert.assertEquals(testValuesArray[j][i], chartValues[i].data.toString())
            }
        }
    }

    @Test
    fun testSetChartValue() {
        Context.chartToView = "causeError"
        GraphStatisticsState().setChartToView("budget")
        Assert.assertEquals("budget", Context.chartToView)
    }

    @Test
    fun testGetChartValue() {
        Context.chartToView = "outgoingOrder"
        Assert.assertEquals("outgoingOrder", GraphStatisticsState().getChartToView())
    }

    @Test
    fun testGetUsername() {
        Assert.assertEquals(GraphStatisticsState().getUserName(), Settings.username)
    }

    @Test
    fun testGetStatesOfGame() {
        val orders = HashMap<String, Int>()
        val stateOfGame = arrayListOf(StateOfGame(1))
        val stateOfPlayer = StateOfPlayer(
                "WHOLESALE",
                false,
                "",
                orders,
                10,
                5,
                orders,
                14,
                53,
                orders
        )
        stateOfGame[0].playerStates["Test"] = stateOfPlayer
        Context.statesOfGame = stateOfGame
        Assert.assertEquals(stateOfGame, GraphStatisticsState().getStatesOfGame())
    }

    @Test
    fun testGetRolesValue() {
        val expectedArray = listOf("RETAILER", "WHOLESALE", "WAREHOUSE", "FACTORY")
        Assert.assertEquals(expectedArray, getRoles<Roles>())
    }

}