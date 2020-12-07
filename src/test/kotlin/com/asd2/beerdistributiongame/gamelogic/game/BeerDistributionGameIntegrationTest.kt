package com.asd2.beerdistributiongame.gamelogic.game

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.controller.StorageController
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.replay.SavedGamePath
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.unmockkAll
import mapJSONFile
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files

class BeerDistributionGameIntegrationTest : GameConfigMock() {

    private var toDeleteFolder = ArrayList<String>()
    private val tempdir = Files.createTempDirectory("testsaves")

    @Before
    fun prepare() {
        val mock = mockk<SavedGamePath>()
        every { mock.savedGamesDir } returns "$tempdir${File.separator}"
        Context.gameConfig = gameConfigLinear
        Context.rolesInGame = rolesInGameLinear
    }

    @After
    fun clean() {
        toDeleteFolder.forEach {
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.gameConfigFileName}").delete()
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.turnFileName}").delete()
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.rolesInGameFileName}").delete()
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.playerFileName}").delete()

            File("$tempdir${File.separator}$it").delete()
        }
        if (!File("${tempdir}").delete()) {
            throw IOException("Could not delete temp dir: " + tempdir.toString())
        }
        unmockkAll()
    }

    @Test
    fun testInitialGameSave() {
        val beerDistributionGame = BeerDistributionGame()
        beerDistributionGame.initialSave(gameConfigLinear)
        toDeleteFolder.add(beerDistributionGame.gameID)

        val gamesaveDir = "${SavedGamePath.savedGamesDir}${beerDistributionGame.gameID}${File.separator}"
        val gamesaveDirFile = File(gamesaveDir)
        val gameConfigFile = File("${gamesaveDir}${SavedGamePath.gameConfigFileName}")
        val gameturnsFile = File("${gamesaveDir}${SavedGamePath.turnFileName}")
        val rolesInGameFile = File("$gamesaveDir${SavedGamePath.rolesInGameFileName}")

        Assert.assertTrue(gamesaveDirFile.exists())
        Assert.assertTrue(gameConfigFile.exists())
        Assert.assertTrue(gameturnsFile.exists())
        Assert.assertTrue(rolesInGameFile.exists())
    }

    @Test
    fun testGetPreviousTurnInfo() {
        val beerDistributionGame = BeerDistributionGame()
        val uuid = "asdfux32-1234-4651-b6e6-sdkalske2310"
        val statesOfGame: ArrayList<StateOfGame> = mapJSONFile("./src/test/resources/replaylobbysavedgames/valid/$uuid/${SavedGamePath.turnFileName}")
        mockkObject(StorageController)
        every { StorageController.getTurnHistory(any()) } returns statesOfGame

        val expectedTurnModels = beerDistributionGame.getPreviousTurnInfo(gameConfigLinear)

        Assert.assertEquals(expectedTurnModels.previousOutgoingOrders[0].turn, 1)
        Assert.assertEquals(expectedTurnModels.previousIncomingOrders[1].amount, 3)
        Assert.assertEquals(
                expectedTurnModels.previousIncomingOrders[1].cost,
                (3 * gameConfigLinear.productionCost).toDouble(),
                .0)
    }
}