package com.asd2.beerdistributiongame.gamelogic.controller

import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.replay.SavedGamePath
import com.asd2.beerdistributiongame.replay.SavedGamePath.turnFileName
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkClass
import io.mockk.unmockkAll
import mapJSONFile
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.File
import java.io.IOException
import java.nio.file.Files

class StorageControllerUnitTest : GameConfigMock() {


    private var storageController = mockkClass(StorageController::class)
    private val uuid = "asdfux32-1234-4651-b6e6-sdkalske2310"
    private val player = "Factory0"
    private var toDeleteFolder = ArrayList<String>()
    private val tempdir = Files.createTempDirectory("testsaves")

    @Before
    fun prepare() {
        val mock = mockk<SavedGamePath>()
        every { mock.savedGamesDir } returns "$tempdir${File.separator}"
    }

    @After
    fun clean() {
        toDeleteFolder.forEach {
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.gameConfigFileName}").delete()
            File("$tempdir${File.separator}$it${File.separator}${SavedGamePath.turnFileName}").delete()
            File("$tempdir${File.separator}$it").delete()
        }
        if (!File("${tempdir}").delete()) {
            throw IOException("Could not delete temp dir: " + tempdir.toString())
        }
        unmockkAll()
    }

    @Test
    fun testInitialize() {
        Assert.assertNotNull(StorageController)
    }

    @Test
    fun saveGameConfig() {
        val gameID = gameConfigLinear.gameID
        StorageController.saveGameConfig(gameConfigLinear)
        toDeleteFolder.add(gameID)

        val gamesaveDir = "${SavedGamePath.savedGamesDir}$gameID${File.separator}"
        val gamesaveDirFile = File(gamesaveDir)
        val gameConfigFile = File("${gamesaveDir}${SavedGamePath.gameConfigFileName}")

        Assert.assertTrue(gamesaveDirFile.exists())
        Assert.assertTrue(gameConfigFile.exists())
    }

    @Test
    fun saveTurn() {
        val gameID = gameConfigLinear.gameID
        StorageController.saveTurn(gameID, ArrayList<StateOfGame>())
        toDeleteFolder.add(gameID)

        val gamesaveDir = "${SavedGamePath.savedGamesDir}$gameID${File.separator}"
        val gamesaveDirFile = File(gamesaveDir)
        val gameturnsFile = File("${gamesaveDir}${SavedGamePath.turnFileName}")

        Assert.assertTrue(gamesaveDirFile.exists())
        Assert.assertTrue(gameturnsFile.exists())
    }

    @Test
    fun testGetTurnHistory() {
        val statesOfGame: ArrayList<StateOfGame> = mapJSONFile("./src/test/resources/replaylobbysavedgames/valid/$uuid/$turnFileName")
        every { storageController.getTurnHistory(uuid) } returns statesOfGame

        val expectedStateOfGame = storageController.getTurnHistory(uuid)
        assertEquals(expectedStateOfGame[0].turnNumber, statesOfGame[0].turnNumber)
        assertEquals(expectedStateOfGame[0].playerStates[player]!!.incomingOrders["Wholesale0"], statesOfGame[0].playerStates[player]!!.incomingOrders["Wholesale0"])
    }
}