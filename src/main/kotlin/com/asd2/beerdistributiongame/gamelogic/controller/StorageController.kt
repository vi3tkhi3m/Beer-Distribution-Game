package com.asd2.beerdistributiongame.gamelogic.controller

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.replay.SavedGamePath.gameConfigFileName
import com.asd2.beerdistributiongame.replay.SavedGamePath.playerFileName
import com.asd2.beerdistributiongame.replay.SavedGamePath.rolesInGameFileName
import com.asd2.beerdistributiongame.replay.SavedGamePath.savedGamesDir
import com.asd2.beerdistributiongame.replay.SavedGamePath.turnFileName
import mapJSONFile
import mapJSONToFile
import java.io.File

/**
 * Singleton of the StorageController, saves different files to maintain the state of the game, creates necessary folders when these do not yet exist for the current game.
 * @throws IOException permission denied exception if folders or files can not be created/deleted
 */
object StorageController {

    fun saveGameConfig(gameConfig: GameConfig) {
        val gamesaveDir = "$savedGamesDir${gameConfig.gameID}${File.separator}"
        val file = File(gamesaveDir)
        if (!file.exists()) {
            file.mkdirs()
        }
        mapJSONToFile(gameConfigFileName, gamesaveDir, gameConfig)
    }

    fun saveTurn(gameUUID: String, statesOfGame: ArrayList<StateOfGame>) {
        val gamesaveDir = "$savedGamesDir$gameUUID${File.separator}"
        val gameDirFile = File(gamesaveDir)
        if (!gameDirFile.exists()) {
            gameDirFile.mkdirs()
        }

        val turnDirFile = File("$gamesaveDir$turnFileName")
        if (turnDirFile.exists()) {
            turnDirFile.delete()
        }
        mapJSONToFile(turnFileName, gamesaveDir, statesOfGame)
    }

    fun saveRolesInGame(gameUUID: String, rolesInGame: RolesInGame) {
        val gamesaveDir = "$savedGamesDir$gameUUID${File.separator}"
        val gameDirFile = File(gamesaveDir)
        if (!gameDirFile.exists()) {
            gameDirFile.mkdirs()
        }

        val turnDirFile = File("$gamesaveDir$rolesInGameFileName")
        if (turnDirFile.exists()) {
            turnDirFile.delete()
        }
        mapJSONToFile(rolesInGameFileName, gamesaveDir, rolesInGame)
    }

    fun savePlayers(gameUUID: String) {
        val gamesaveDir = "$savedGamesDir$gameUUID${File.separator}"
        val gameDirFile = File(gamesaveDir)
        if (!gameDirFile.exists()) {
            gameDirFile.mkdirs()
        }
        val playerDirFile = File("$gamesaveDir$playerFileName")
        if (playerDirFile.exists()) {
            playerDirFile.delete()
        }
        mapJSONToFile(playerFileName, gamesaveDir, Context.players)
    }

    /**
     * Maps turnhistory from turn file
     * @returns ArrayList<StateOfGame> All turns found in the file converted to StateOfGames
     */
    fun getTurnHistory(gameUUID: String): ArrayList<StateOfGame> {
        return mapJSONFile("$savedGamesDir$gameUUID${File.separator}$turnFileName")
    }
}