package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.ReplayLobbyHandler
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.replay.ReplayGameData
import com.asd2.beerdistributiongame.replay.SavedGamePath.gameConfigFileName
import com.asd2.beerdistributiongame.replay.SavedGamePath.playerFileName
import com.asd2.beerdistributiongame.replay.SavedGamePath.rolesInGameFileName
import com.asd2.beerdistributiongame.replay.SavedGamePath.savedGamesDir
import com.asd2.beerdistributiongame.replay.SavedGamePath.turnFileName
import com.asd2.beerdistributiongame.settings.Settings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import mapJSONFile
import java.io.File

class ReplayLobbyState : State() {

    private val handler = ReplayLobbyHandler()
    lateinit var results: ObservableList<ReplayGameData>

    override fun onEnter() {
        results = getReplayLobbyData(savedGamesDir)
        Context.loadScene(handler.getViewFile())
    }

    override fun onExit() {
        //todo
    }

    fun getUsername() = Settings.username

    fun notifyBackToMainMenu() = setState(MainMenuState())

    fun notifyToShowGameResults() = setState(LeaderboardState())

    fun getReplayLobbyData(gamePath: String): ObservableList<ReplayGameData> {
        val games = mutableListOf<ReplayGameData>()
        val gamesList = FXCollections.observableArrayList<ReplayGameData>()
        val folderNames = getFolder(gamePath)

        folderNames.forEach {
            try {
                val gameConfiguration: GameConfig = mapJSONFile(path = "$gamePath$it/$gameConfigFileName")
                if (gameConfiguration.gameName != "") {
                    val numberOfTurns = getNumberOfTurns(path = "$gamePath$it/$turnFileName")
                    if (numberOfTurns is Int) {
                        val game = ReplayGameData(gameID = gameConfiguration.gameID, name = gameConfiguration.gameName, turns = numberOfTurns, type = gameConfiguration.gameMode.toString())
                        games.add(game)
                    } else {
                        println("Game turns of game with id ["+gameConfiguration.gameID+"] is corrupt!")
                    }
                } else {
                    println("Game configuration of game with id ["+gameConfiguration.gameID+"] is corrupt!")
                }
            }
            catch (e: Exception) {
                println("Game with id [$it] corrupt!")
            }
        }
        games.forEach { gamesList.add(it) }
        return gamesList

    }

    private fun getNumberOfTurns(path: String): Int? {
        var numberOfTurns = 0
        val turns: Collection<StateOfGame>

        try {
            turns = mapJSONFile(path = path)
            turns.iterator().forEach {
                val turnNumber = it.turnNumber
                if (turnNumber > numberOfTurns) numberOfTurns = turnNumber
            }
        } catch (e: Exception) {
            println("Turns not found in $path")
            return null
        }

        return numberOfTurns
    }

    private fun getFolder(location: String): List<String> {
        val folder = File(location)
        val folderNames = mutableListOf<String>()
        folder.listFiles()?.forEach {
            if (it.isDirectory) folderNames.add(it.name)
        }
        return folderNames
    }

    fun loadGame(gamePath: String, gameID: String) {
        Context.gameConfig = mapJSONFile(path = "$gamePath$gameID${File.separator}$gameConfigFileName")
        Context.rolesInGame = mapJSONFile(path = "$gamePath$gameID${File.separator}$rolesInGameFileName")
        Context.statesOfGame = mapJSONFile(path = "$gamePath$gameID${File.separator}$turnFileName")
        Context.players = mapJSONFile(path = "$gamePath$gameID${File.separator}$playerFileName")
        Context.replayTurnNumber = 1
    }
}