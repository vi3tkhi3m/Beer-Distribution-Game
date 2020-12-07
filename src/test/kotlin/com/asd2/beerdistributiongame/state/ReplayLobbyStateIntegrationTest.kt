package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.replay.ReplayGameData
import javafx.collections.FXCollections
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import org.junit.Test

class ReplayLobbyStateIntegrationTest {

    var testPathSavedGames = "./src/test/resources/replaylobbysavedgames/valid/"
    var testPathSavedGamesCorrupt = "./src/test/resources/replaylobbysavedgames/corrupt/"

    @Test
    fun testGetReplayLobbyDataValidData() {
        val gamesList = FXCollections.observableArrayList<ReplayGameData>()
        val gameOne = ReplayGameData(gameID = "asdfux32-1234-4651-b6e6-sdkalske2310", name = "Test", turns = 2, type = "LINEAR")
        val gameTwo = ReplayGameData(gameID = "oiutgx32-5678-1234-fdse-sdkalske0512", name = "OtherTest", turns = 1, type = "GRAPH")
        gamesList.add(gameOne)
        gamesList.add(gameTwo)
        assertEquals(gamesList, ReplayLobbyState().getReplayLobbyData(testPathSavedGames))
        assertEquals(2, ReplayLobbyState().getReplayLobbyData(testPathSavedGames)[0].turns)
    }

    @Test
    fun testGetReplayLobbyDataCorruptData() {
        val gamesList = FXCollections.observableArrayList<ReplayGameData>()
        assertEquals(gamesList, ReplayLobbyState().getReplayLobbyData(testPathSavedGamesCorrupt))
    }

    @Test
    fun testGetReplayLobbyDataInvalidPath() {
        val gamesList = FXCollections.observableArrayList<ReplayGameData>()
        assertEquals(gamesList, ReplayLobbyState().getReplayLobbyData("invalidPath/"))
    }

    @Test
    fun testLoadGame() {
        ReplayLobbyState().loadGame(testPathSavedGames, "asdfux32-1234-4651-b6e6-sdkalske2310")
        assertEquals("asdfux32-1234-4651-b6e6-sdkalske2310", Context.gameConfig.gameID)
        assertEquals(1, Context.statesOfGame[0].turnNumber)
        assertEquals(2, Context.statesOfGame.size)
        assertEquals("asdfux32-1234-4651-b6e6-sdkalske2310",Context.players.first().uuid)
        assertTrue(Context.rolesInGame.playerRoles.contains("Retailer0"))
    }
}