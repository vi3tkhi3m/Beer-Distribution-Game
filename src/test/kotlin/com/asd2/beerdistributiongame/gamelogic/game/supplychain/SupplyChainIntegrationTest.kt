package com.asd2.beerdistributiongame.gamelogic.game.supplychain

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.game.BeerDistributionGame
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SupplyChainIntegrationTest : GameConfigMock() {

    @Before
    fun prepare() {
        Context.gameConfig = gameConfigLinear
        Context.rolesInGame = rolesInGameLinear
    }

    @Test
    fun testIfOrdersForFactoryAreInQueueAndDecrease() {
        val beerGame = BeerDistributionGame()
        Context.beerDistributionGame = beerGame

        val countTurn1 = beerGame.getStateOfGame().factoryQueue.count()

        createPlacedOrderHashMapStub()
        beerGame.nextTurn()
        //Should have at least one in queue now
        val countTurn2 = beerGame.getStateOfGame().factoryQueue.count()

        //To empty queue we should place orders with the size of 0 so that new ones are not added
        createPlacedOrderHashMapStub(0)
        //Update supply chain twice, default turnsInQueue = 2
        beerGame.nextTurn()
        createPlacedOrderHashMapStub(0)
        beerGame.nextTurn()

        val countTurn4 = beerGame.getStateOfGame().factoryQueue.count()

        Assert.assertEquals(0, countTurn1)
        Assert.assertEquals(1, countTurn2)
        Assert.assertEquals(0, countTurn4)
    }

    @Test
    fun testIfInitialStockAndBudgetHaveChangedInSupplyChain() {
        val beerGame = BeerDistributionGame()
        gameConfigLinear.minCustomerDemand = 1
        gameConfigLinear.maxCustomerDemand = 3
        Context.beerDistributionGame = beerGame
        createPlacedOrderHashMapStub(3)

        beerGame.nextTurn()

        beerGame.getStateOfGame().playerStates.forEach {
            Assert.assertNotEquals(it.value.stock, gameConfigLinear.initialStock)
            Assert.assertNotEquals(it.value.budget, gameConfigLinear.initialBudget)
        }
    }

    @Test
    fun testIfOrdersArePlacedOnBacklog() {
        val beerGame = BeerDistributionGame()
        gameConfigLinear.minCustomerDemand = 10
        gameConfigLinear.maxCustomerDemand = 20
        Context.beerDistributionGame = beerGame
        createPlacedOrderHashMapStub(15)

        beerGame.nextTurn()
        beerGame.getStateOfGame().playerStates.forEach {
            //Order amount is greater then initial stock so al roles should have a 1 backlog item
            Assert.assertEquals(1, it.value.backlogs.size)
        }
    }
}