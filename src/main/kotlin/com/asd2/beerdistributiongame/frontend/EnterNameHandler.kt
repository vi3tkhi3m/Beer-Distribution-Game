package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.EnterNameState
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField

class EnterNameHandler : Handler<EnterNameState>() {

	@FXML lateinit var chooseNameButton: Button
	@FXML lateinit var quitGameButton: Button
	@FXML lateinit var playerName: TextField
	@FXML lateinit var userNameError: Label

    override fun getViewFile() = "views/EnterPlayerName.fxml"

    @FXML
    fun initialize() {
        chooseNameButton.isDisable = !Settings.validateUsername(playerName.text)
    }

	fun chooseNameButtonPressed() {
        if (Settings.validateUsername(playerName.text)) {
            Settings.changeUsername(playerName.text)
            state.notifyToContinueToMainMenu()
        } else {
            addError(playerName)
            userNameError.isVisible = true
            chooseNameButton.isDisable = true
        }
	}

    fun onChangePlayerNameChanged() {
        if (Settings.validateUsername(playerName.text)) {
            removeError(playerName)
            userNameError.isVisible = false
            chooseNameButton.isDisable = false
        } else {
            addError(playerName)
            userNameError.isVisible = true
            chooseNameButton.isDisable = true
        }
    }

    fun quitGamePressed() = state.notifyToQuitGame()
}