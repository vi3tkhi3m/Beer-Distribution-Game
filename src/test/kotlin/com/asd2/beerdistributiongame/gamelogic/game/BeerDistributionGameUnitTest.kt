package com.asd2.beerdistributiongame.gamelogic.game

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.controller.StorageController
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.replay.SavedGamePath
import com.asd2.beerdistributiongame.replay.SavedGamePath.turnFileName
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import mapJSONFile
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files

class BeerDistributionGameUnitTest : GameConfigMock() {
    private val uuid = "asdfux32-1234-4651-b6e6-sdkalske2310"
    private val filePath = "./src/test/resources/replaylobbysavedgames/valid/$uuid/$turnFileName"
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
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.playerFileName}").delete()
            File("$tempdir${File.separator}$it").delete()
        }
        if (!File("${tempdir}").delete()) {
            throw IOException("Could not delete temp dir: " + tempdir.toString())
        }
        unmockkAll()
    }

    @Test
    fun testInitialize() {
        val beerDistributionGame = BeerDistributionGame()
        assertNotNull(beerDistributionGame)
        assertNotNull(beerDistributionGame.supplyChain)
        assertNotNull(beerDistributionGame.gameFlow)
    }

    @Test
    fun testGetPreviousTurnInfo() {
        val expectedIncOrder = 0
        val expectedOutOrder = 0
        val statesOfGame: ArrayList<StateOfGame> = mapJSONFile(filePath)
        StorageController.saveTurn(uuid, statesOfGame)
        val beerDistributionGame = BeerDistributionGame()
        val mockContext = mockk<Context>()
        every { mockContext.beerDistributionGame } returns beerDistributionGame
        toDeleteFolder.add(Context.beerDistributionGame!!.gameID)

        val previousTurnModel = beerDistributionGame.getPreviousTurnInfo(gameConfigLinear)
        Assert.assertEquals(expectedIncOrder, previousTurnModel.previousIncomingOrders[0].amount)
        Assert.assertEquals(expectedOutOrder, previousTurnModel.previousOutgoingOrders[0].amount)
    }
}