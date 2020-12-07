package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.*
import com.asd2.beerdistributiongame.gamelogic.datastructure.ChainStructureNode
import com.asd2.beerdistributiongame.gamelogic.game.BeerDistributionGame
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.chainrole.*
import com.asd2.beerdistributiongame.replay.SavedGamePath
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.File
import java.io.IOException
import java.nio.file.Files

class GameStateIntegrationTest : ApplicationTest() {

    private var toDeleteFolder = ArrayList<String>()
    private val tempdir = Files.createTempDirectory("testsaves")

    val gameConfigLinear = GameConfig(
            gameName = "TestGame",
            gameID = "asdfux32-1234-4651-b6e6-sdkalske2310",
            gameMode = GameMode.LINEAR,
            overviewType = OverviewType.LOCAL,
            initialStock = 10,
            initialBudget = 100,
            turnDuration = 60,
            backlogCost = 2,
            stockCost = 1,
            productionCost = 1,
            maxTurns = 40,
            infiniteTurns = false,
            quantityOfFactories = 1,
            quantityOfWarehouses = 1,
            quantityOfWholesales = 1,
            quantityOfRetailers = 1,
            retailProfit = 5,
            minCustomerDemand = 1,
            maxCustomerDemand = 20
    )

    val rolesInGameLinear = RolesInGame()

    @Before
    fun prepare() {
        Context.players.clear()

        val playerA = Player("PlayerA", "testUserA", "testIpA", true)
        val playerB = Player("PlayerB", "testUserB", "testIpB", true)
        val playerC = Player("PlayerC", "testUserC", "testIpC", true)
        val playerD = Player("PlayerD", "testUserD", "testIpD", true)

        Context.players.add(playerA)
        Context.players.add(playerB)
        Context.players.add(playerC)
        Context.players.add(playerD)

        Context.rolesInGame.playerRoles.clear()

        val playerRole = rolesInGameLinear.playerRoles
        playerRole["Retailer0"] = RoleOwner(Roles.RETAILER, "PlayerA")
        playerRole["Wholesaler0"] = RoleOwner(Roles.WHOLESALE, "PlayerB")
        playerRole["Distributor0"] = RoleOwner(Roles.WAREHOUSE, "PlayerC")
        playerRole["Factory0"] = RoleOwner(Roles.FACTORY, "PlayerD")
    }

    @After
    fun clean() {
        toDeleteFolder.forEach {
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.gameConfigFileName}").delete()
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.turnFileName}").delete()
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.rolesInGameFileName}").delete()
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.playerFileName}").delete()
            File("$tempdir${File.separator}${SavedGamePath.playerFileName}").delete()
            File("$tempdir${File.separator}$it").delete()
        }
        if (!File("${tempdir}").delete()) {
            throw IOException("Could not delete temp dir: " + tempdir.toString())
        }
        Context.players.clear()
        Context.rolesInGame.playerRoles.clear()
        Context.isHost = false
    }

    @Test
    fun testCreateGameLinearSuccess() {
        Context.gameConfig = gameConfigLinear
        Context.rolesInGame = rolesInGameLinear
        Context.activeState = GameState()
        val game = Context.beerDistributionGame
        toDeleteFolder.add(game!!.gameID)
        val chain = game.supplyChain
        val chainStructure = chain.chainStructure
        val gameFlow = game.gameFlow

        Assert.assertNotNull(game)
        Assert.assertNotNull(chain)
        Assert.assertNotNull(chainStructure)
        Assert.assertNotNull(gameFlow)

        var retailer: ChainStructureNode<SupplyChainRole>? = null
        var wholesale: ChainStructureNode<SupplyChainRole>? = null
        var warehouse: ChainStructureNode<SupplyChainRole>? = null
        var factory: ChainStructureNode<SupplyChainRole>? = null
        val playerRoles = rolesInGameLinear.playerRoles

        for ((roleKey, chainNode) in chainStructure.playerRoles) {
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
    }

    @Test
    fun testInitialGameSave() {
        Context.gameConfig = gameConfigLinear
        Context.rolesInGame = rolesInGameLinear
        Context.statesOfGame.add(GameState().createGame())

        val beerGame = BeerDistributionGame()
        Context.beerDistributionGame = beerGame

        beerGame.initialSave(Context.gameConfig)
        toDeleteFolder.add(beerGame.gameID)

        val gamesaveDir = "${SavedGamePath.savedGamesDir}${beerGame.gameID}${File.separator}"
        val gamesaveDirFile = File(gamesaveDir)
        val gameConfigFile = File("$gamesaveDir${SavedGamePath.gameConfigFileName}")
        val gameturnsFile = File("$gamesaveDir${SavedGamePath.turnFileName}")
        val rolesInGameFile = File("$gamesaveDir${SavedGamePath.rolesInGameFileName}")

        Assert.assertTrue(gamesaveDirFile.exists())
        Assert.assertTrue(gameConfigFile.exists())
        Assert.assertTrue(gameturnsFile.exists())
        Assert.assertTrue(rolesInGameFile.exists())
    }

    @Test
    fun testGetStateOfGame() {
        Context.isHost = true
        Context.gameConfig = gameConfigLinear
        toDeleteFolder.add(gameConfigLinear.gameID)
        Context.rolesInGame = rolesInGameLinear
        Context.statesOfGame.add(GameState().createGame())
        val game = Context.beerDistributionGame
        val firstStateOfGame = game!!.getStateOfGame()

        Assert.assertNotNull(firstStateOfGame)
        Assert.assertEquals(0, firstStateOfGame.turnNumber)
        Assert.assertEquals(rolesInGameLinear.playerRoles.size, firstStateOfGame.playerStates.size)
    }
}