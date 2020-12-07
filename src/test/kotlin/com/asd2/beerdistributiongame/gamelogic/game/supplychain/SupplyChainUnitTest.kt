package com.asd2.beerdistributiongame.gamelogic.game.supplychain

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.configuration.IllegalChainStructureException
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.datastructure.ChainStructureNode
import com.asd2.beerdistributiongame.gamelogic.game.BeerDistributionGame
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.chainrole.*
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class SupplyChainUnitTest : GameConfigMock() {

    lateinit var corruptChain: SupplyChain

    @Before
    fun initializeTests() {
        Context.gameConfig = gameConfigLinear
        Context.rolesInGame = rolesInGameLinear
        var beerDistributionGame = mockk<BeerDistributionGame>()
    }

    @Test
    fun testInitializeSuccess() {
        val supplyChain = SupplyChain()
        Assert.assertNotNull(supplyChain)

        var retailer: ChainStructureNode<SupplyChainRole>? = null
        var wholesale: ChainStructureNode<SupplyChainRole>? = null
        var warehouse: ChainStructureNode<SupplyChainRole>? = null
        var factory: ChainStructureNode<SupplyChainRole>? = null
        val playerRoles = rolesInGameLinear.playerRoles

        for ((roleKey, chainNode) in supplyChain.chainStructure.playerRoles) {
            when (playerRoles[roleKey]!!.role) {
                Roles.RETAILER -> {
                    if (retailer != null) {
                        Assert.assertTrue(false)
                    }
                    Assert.assertTrue(chainNode.representative is Retailer)
                    retailer = chainNode
                }
                Roles.WHOLESALE -> {
                    if (wholesale != null) {
                        Assert.assertTrue(false)
                    }
                    Assert.assertTrue(chainNode.representative is Wholesale)
                    wholesale = chainNode
                }
                Roles.WAREHOUSE -> {
                    if (warehouse != null) {
                        Assert.assertTrue(false)
                    }
                    Assert.assertTrue(chainNode.representative is Warehouse)
                    warehouse = chainNode
                }
                Roles.FACTORY -> {
                    if (factory != null) {
                        Assert.assertTrue(false)
                    }
                    Assert.assertTrue(chainNode.representative is Factory)
                    factory = chainNode
                }
                else -> {
                    Assert.assertTrue(false)
                }
            }
        }

        Assert.assertEquals(wholesale!!.representative.roleKey, retailer!!.parents[0])
        Assert.assertEquals(0, retailer.children.size)

        Assert.assertEquals(warehouse!!.representative.roleKey, wholesale.parents[0])
        Assert.assertEquals(retailer.representative.roleKey, wholesale.children[0])

        Assert.assertEquals(factory!!.representative.roleKey, warehouse.parents[0])
        Assert.assertEquals(wholesale.representative.roleKey, warehouse.children[0])

        Assert.assertEquals(factory.representative.roleKey, factory.parents[0])
        Assert.assertEquals(warehouse.representative.roleKey, factory.children[0])

        supplyChain.chainStructure.playerRoles.forEach {
            //Outgoing orders and delivery are both set to 0 at the start, caused by resetIncomingOrders method
            Assert.assertEquals(it.value.representative.outgoingOrder, 0)
            Assert.assertEquals(it.value.representative.incomingDelivery, 0)
            Assert.assertEquals(it.value.representative.budget, gameConfigLinear.initialBudget)
            Assert.assertEquals(it.value.representative.stock, gameConfigLinear.initialStock)
        }
    }

    @Test(expected = IllegalChainStructureException::class)
    fun testInitializeFail() {
        Context.rolesInGame = falseRolesInGameLinear
        corruptChain = SupplyChain()
    }
}