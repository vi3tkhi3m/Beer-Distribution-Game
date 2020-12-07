package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.replay.ReplayGameData
import com.asd2.beerdistributiongame.replay.SavedGamePath.savedGamesDir
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.ReplayLobbyState
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.text.Text


class ReplayLobbyHandler : Handler<ReplayLobbyState>() {

    @FXML lateinit var usernameText: Text
    @FXML lateinit var replayButton: Button

    @FXML lateinit var savedGamesTable: TableView<ReplayGameData>

    @FXML lateinit var gameId: TableColumn<ReplayGameData, String>
    @FXML lateinit var gameRounds: TableColumn<ReplayGameData, String>
    @FXML lateinit var gameType: TableColumn<ReplayGameData, String>

    @FXML
    fun initialize() {
        usernameText.text = Settings.username
        gameId.cellValueFactory = PropertyValueFactory<ReplayGameData, String>("name")
        gameRounds.cellValueFactory = PropertyValueFactory<ReplayGameData, String>("turns")
        gameType.cellValueFactory = PropertyValueFactory<ReplayGameData, String>("type")
        savedGamesTable.items.addAll(state.results)
    }

    override fun getViewFile(): String = "views/ReplayLobby.fxml"

    fun goBackToMainMenu() = state.notifyBackToMainMenu()

    fun onReplayGameButton() {
        val game = savedGamesTable.selectionModel.selectedItem
        if (game != null) {
            state.loadGame(savedGamesDir, game.gameID)
            state.notifyToShowGameResults()
        } else {
            showAlert("Please select a saved game", Alert.AlertType.ERROR, "Error", "Error")
        }
    }
}