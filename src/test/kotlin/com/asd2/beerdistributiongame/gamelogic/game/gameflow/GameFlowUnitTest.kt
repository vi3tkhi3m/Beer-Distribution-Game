package com.asd2.beerdistributiongame.gamelogic.game.gameflow

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import org.junit.Assert
import org.junit.Test

class GameFlowUnitTest : GameConfigMock() {


    @Test
    fun testInitialize() {
        Context.gameConfig = gameConfigLinear
        val gameFlow = GameFlow()
        Assert.assertNotNull(gameFlow)
        Assert.assertEquals(gameConfigLinear.maxTurns, gameFlow.maxTurns)
        Assert.assertEquals(gameConfigLinear.turnDuration, gameFlow.turnTime)
        val placedOrders = gameFlow.placedOrders
        for (order in placedOrders.values) {
            Assert.assertEquals(-1, order.amount)
        }
    }
}