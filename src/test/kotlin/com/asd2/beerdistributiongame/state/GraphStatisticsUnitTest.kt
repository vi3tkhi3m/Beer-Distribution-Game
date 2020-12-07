package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.configuration.RoleOwner
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import javafx.scene.chart.XYChart
import org.junit.After
import org.junit.Assert
import org.junit.Test

class GraphStatisticsUnitTest {

    @After
    fun unmock() {
        unmockkAll()
    }

    @Test
    fun testAverageAllRoles() {
        val contextMock = mockk<Context>()
        val rolesInGame = RolesInGame()
        val playerRoles = rolesInGame.playerRoles
        playerRoles["Retailer0"] = RoleOwner(Roles.RETAILER, "uuidTest")
        playerRoles["Wholesaler0"] = RoleOwner(Roles.WHOLESALE, "uuidTest2")
        val orders = HashMap<String, Int>()
        orders["Test3"] = 1
        val stateOfGame = arrayListOf(StateOfGame(1),StateOfGame(2))
        val stateOfPlayerTestt1 = StateOfPlayer(
                "uuidTest",
                false,
                "",
                orders,
                10,
                5,
                orders,
                15,
                50,
                orders
        )
        val stateOfPlayerTestt2 = StateOfPlayer(
                "uuidTest",
                false,
                "",
                orders,
                20,
                10,
                orders,
                20,
                60,
                orders
        )
        val stateOfPlayerTest2t1 = StateOfPlayer(

                "uuidTest2",
                false,
                "",
                orders,
                15,
                20,
                orders,
                20,
                30,
                orders
        )
        val stateOfPlayerTest2t2 = StateOfPlayer(
                "uuidTest2",
                false,
                "",
                orders,
                20,
                30,
                orders,
                40,
                70,
                orders
        )
        stateOfGame[0].playerStates["test"] = stateOfPlayerTestt1
        stateOfGame[0].playerStates["test2"] = stateOfPlayerTest2t1
        stateOfGame[1].playerStates["test"] = stateOfPlayerTestt2
        stateOfGame[1].playerStates["test2"] = stateOfPlayerTest2t2
        every { contextMock.rolesInGame.playerRoles } returns playerRoles
        every { contextMock.statesOfGame } returns stateOfGame

        val allStatisticList = contextMock.statesOfGame
        val testValuesOutgoingOrders = mutableListOf(
                "[Data[0,1.0,null], Data[1,1.0,null]]",
                "[Data[1,15.0,null], Data[2,20.0,null]]",
                "[Data[1,20.0,null], Data[2,40.0,null]]"

        )
        val testValuesBudget = mutableListOf(
                "[Data[1,5.0,null], Data[2,10.0,null]]",
                "[Data[1,20.0,null], Data[2,30.0,null]]"

        )
        val testValuesDoNotExist = mutableListOf(
                "[Data[1,0.0,null], Data[2,0.0,null]]",
                "[Data[1,0.0,null], Data[2,0.0,null]]"
        )
        val testValuesArray = mutableListOf(testValuesOutgoingOrders, testValuesBudget, testValuesDoNotExist)
        val valueToGetArray = arrayListOf("outgoingOrder", "budget", "doesNotExist")

        for (j in 0 until testValuesArray.size) {
            val chartValues = mutableListOf<XYChart.Series<String, Number>>()
            val roleList = mutableListOf("RETAILER", "WHOLESALE", "WAREHOUSE", "FACTORY")
            if (valueToGetArray[j] == "outgoingOrder") roleList.add(0, "CONSUMER")
            chartValues.addAll(GraphStatisticsState().averageAllRoles(roleList, allStatisticList, valueToGetArray[j], contextMock.rolesInGame.playerRoles))
            for (i in 0 until testValuesArray[j].size) {
                Assert.assertEquals(testValuesArray[j][i], chartValues[i].data.toString())
            }
        }


    }
}