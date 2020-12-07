package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.JoinGameState
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TextField

class JoinGameHandler : Handler<JoinGameState>() {

    private val ipAddressRegex = Regex("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\$")

    @FXML lateinit var ipAddress: TextField
    @FXML lateinit var backToMenu: Button
    @FXML lateinit var joinButton: Button

    @FXML
    fun initialize() {
        ipAddress.text = Settings.lastSuccessfulIp
    }

    override fun getViewFile() = "views/JoinGame.fxml"

    fun onBackToMainMenuPressed() = state.notifyToGoBackToMainMenu()

    fun onJoinGamePressed() {
        if (validateIpAddress(ipAddress.text)) state.notifyToConnectWith(ipAddress.text)
        else showAlert("Invalid IP Address!")
    }

    private fun validateIpAddress(ipAddress: String) = ipAddressRegex.matches(ipAddress) || "localhost" == ipAddress.toLowerCase()

    @FXML
    fun onEnter() = onJoinGamePressed()
}