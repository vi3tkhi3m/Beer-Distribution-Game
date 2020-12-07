package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.replay.PersonStatistics
import com.asd2.beerdistributiongame.settings.Settings
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PersonalStatisticsStateIntegrationTest {
    private val incomingOrders = HashMap<String, Int>()
    private val outgoingDeliveries = HashMap<String, Int>()

    @Before
    fun before(){
        unmockkAll()
        Context.statesOfGame.clear()
        val stateOfPlayers = HashMap<String, StateOfPlayer>()
        val backlogs = HashMap<String, Int>()
        backlogs["Test2"] = 1
        incomingOrders["Wholesale0"] = 10
        outgoingDeliveries["Wholesale0"] = 2
        stateOfPlayers[Settings.uuid] = StateOfPlayer(Settings.uuid, false, "Test", backlogs, 10, 5, incomingOrders, 14, 53, outgoingDeliveries)
        Context.statesOfGame = arrayListOf(StateOfGame(1))
        Context.statesOfGame.first().playerStates = stateOfPlayers
    }

    @After
    fun after(){
        unmockkAll()
        Context.statesOfGame.clear()
    }

    @Test
    fun testGetUsername() {
        Context.players.add(Player("asdfux32-1234-4651-b6e6-sdkalske2310", "unittestGetUsername", "", false))
        Assert.assertEquals("unittestGetUsername", PersonalStatisticsState("asdfux32-1234-4651-b6e6-sdkalske2310").getUsername())
    }

    @Test
    fun testGetNoUsername(){
        Assert.assertEquals("No username", PersonalStatisticsState("asdfux32-1234-4651-b6e6-sdkalske2310").getUsername())
    }

    @Test
    fun testGetInitialBudget() {
        Context.gameConfig.initialBudget = 10
        Assert.assertEquals(10, PersonalStatisticsState().getInitialBudget())
    }

    @Test
    fun getPersonalStatistics() {
        Assert.assertEquals(listOf(PersonStatistics(1, false, 1, 10, 5, 10,14,53,2)), PersonalStatisticsState().getPersonalStatistics(Settings.uuid))
    }
}