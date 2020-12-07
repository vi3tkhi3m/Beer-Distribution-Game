package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.settings.Translations
import com.asd2.beerdistributiongame.state.MainMenuState
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.text.Text

class MainMenuHandler: Handler<MainMenuState>() {

	override fun getViewFile() = "views/MainMenu.fxml"

	@FXML lateinit var usernameText : Text
	@FXML lateinit var hostGameButton: Button
	@FXML lateinit var optionsButton: Button
	@FXML lateinit var joinGameButton: Button
	@FXML lateinit var createGameAgentButton: Button
	@FXML lateinit var replayGameButton: Button
	@FXML lateinit var quitGameButton: Button
	@FXML lateinit var editGameAgentButton: Button

	@FXML
	fun initialize() {
		StorageManager.createDefault()
		usernameText.text = Settings.username
		optionsButton.text = Translations.get("optionsButton")
		hostGameButton.text = Translations.get("hostGameButton")
		joinGameButton.text = Translations.get("joinGameButton")
		createGameAgentButton.text = Translations.get("createGameAgentButton")
		replayGameButton.text = Translations.get("replayGameButton")
		quitGameButton.text = Translations.get("quitGameButton")
		editGameAgentButton.text = Translations.get("editGameAgentButton")
	}

	fun hostGamePressed() = state.notifyToHostGame()

	fun optionsPressed() = state.notifyToOptions()

	fun joinGamePressed() = state.notifyToJoinGame()

	fun createGameAgentPressed() = state.notifyToCreateGameAgent()

	fun editGameAgentPressed() = state.notifyToEditGameAgent()

	fun replayGamePressed() = state.notifyToViewReplays()

	fun quitGamePressed() = state.notifyToQuitGame()
}