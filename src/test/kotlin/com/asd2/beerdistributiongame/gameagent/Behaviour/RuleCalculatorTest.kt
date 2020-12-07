package com.asd2.beerdistributiongame.gameagent.Behaviour

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gameagent.ast.AST
import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import io.mockk.impl.annotations.InjectMockKs
import mapJSONFile
import com.asd2.beerdistributiongame.gameagent.AgentBuilder
import com.asd2.beerdistributiongame.gameagent.GameAgent
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.replay.SavedGamePath
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock

@RunWith(JUnit4::class)
class RuleCalculatorTest : GameConfigMock() {

    var stateOfPlayer: StateOfPlayer
    lateinit var gameAgent: GameAgent
    val roleKey = "Factory0"

    @Mock
    var context: Context = Context

    init {
        val player = "playerUUID"
        val gameAgentActive = false
        val gameAgentFormula = "@var=3*Stock NewOrder = @var * 2 - Backlog if[@var > 9] then [@var = 5]; if[@var > 10] then [@var = 10];"
        val backlogs: HashMap<String, Int> = HashMap()
        backlogs["A"] = 30
        backlogs["B"] = 28
        val stock = 4
        val budget = 60
        val incomingOrders: HashMap<String, Int> = HashMap()
        incomingOrders["Order1"] = 17
        incomingOrders["Order2"] = 3
        val outgoingOrder = 40
        val incomingDelivery = 44
        val outgoingDeliveries: HashMap<String, Int> = HashMap()
        outgoingDeliveries["Delivery1"] = 7

        stateOfPlayer = StateOfPlayer(
                player, gameAgentActive, gameAgentFormula,
                backlogs, stock, budget, incomingOrders, outgoingOrder,
                incomingDelivery, outgoingDeliveries
        )

        val statesOfGame: ArrayList<StateOfGame> = mapJSONFile("src/test/resources/replaylobbysavedgames/valid/asdfux32-1234-4651-b6e6-sdkalske2310/${SavedGamePath.turnFileName}")

        context.statesOfGame = statesOfGame

    }

    @Test
    fun determineOrderIsBelowZeroTest() {
        //init
        stateOfPlayer.gameAgentFormula = "@var=3*Stock NewOrder = @var * (2 - Backlog) if[@var < 19] then [@var = 5]; if[@var > 10] then [@var = 10];"
        context.statesOfGame.last().playerStates[roleKey] = stateOfPlayer
        gameAgent = GameAgent(stateOfPlayer.gameAgentFormula)

        //test
        val actualOrder = gameAgent.calculateOrder(roleKey)
        val expectedOrder = 0

        //check
        Assert.assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun determineOrderIsAboveZeroTest() {
        //init
        stateOfPlayer.gameAgentFormula = "@var=3*Stock NewOrder = @var * (Budget / 12) + Backlog if[@var = 12] then [@var = 7]; if[@var > 10] then [@var = 10];"
        context.statesOfGame.last().playerStates[roleKey] = stateOfPlayer
        gameAgent = GameAgent(stateOfPlayer.gameAgentFormula)

        //test
        val actualOrder = gameAgent.calculateOrder(roleKey)
        val expectedOrder = 93

        //check
        Assert.assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun determineOrderIsReassignedTest() {
        //init
        stateOfPlayer.gameAgentFormula = "@var=3*Stock NewOrder = @var * 2 + Backlog - 1 if[@var > 10] then [Order = Order / 9]; if[@var > 10] then [@var = 10];"
        context.statesOfGame.last().playerStates[roleKey] = stateOfPlayer
        gameAgent = GameAgent(stateOfPlayer.gameAgentFormula)

        //test
        val actualOrder = gameAgent.calculateOrder(roleKey)
        val expectedOrder = 9

        //check
        Assert.assertEquals(expectedOrder, actualOrder)
    }

    @Test
    fun ifClauseIsFalseTest() {
        //init
        stateOfPlayer.gameAgentFormula = "@var=3*Stock NewOrder = @var * 2 + √Backlog if[@var = 10.9] then [Order = 7]; if[@var > 10] then [Order = 10];"
        context.statesOfGame.last().playerStates[roleKey] = stateOfPlayer
        gameAgent = GameAgent(stateOfPlayer.gameAgentFormula)

        //test
        val actualOrder = gameAgent.calculateOrder(roleKey)
        val expectedOrder = 10

        //check
        Assert.assertEquals(expectedOrder, actualOrder)
    }
}