package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.replay.PersonStatistics
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Test

class PersonalStatisticsStateUnitTest {

    @After
    fun after(){
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun testGetUUID() {
        Assert.assertEquals("asdfux32-1234-4651-b6e6-sdkalske2310", PersonalStatisticsState("asdfux32-1234-4651-b6e6-sdkalske2310").getUUID())
    }

    @Test
    fun testGetPersonalStatistics(){
        val contextMock = mockk<Context>()
        val backlogs = HashMap<String,Int>()
        val incomingOrders = HashMap<String, Int>()
        val outgoingDeliveries = HashMap<String, Int>()
        backlogs["Test2"] = 1
        incomingOrders["Wholesale0"] = 10
        outgoingDeliveries["Wholesale0"] = 2
        val stateOfGame = StateOfGame(1)
        stateOfGame.playerStates = hashMapOf("Test" to StateOfPlayer("Test",false,"Test",backlogs,10,5,incomingOrders,14,53,outgoingDeliveries))
        every { contextMock.statesOfGame } returns arrayListOf(stateOfGame)
        Assert.assertEquals(listOf(PersonStatistics(1, false, 1, 10, 5, 10,14,53,2)), PersonalStatisticsState().getPersonalStatistics("Test"))
    }
}