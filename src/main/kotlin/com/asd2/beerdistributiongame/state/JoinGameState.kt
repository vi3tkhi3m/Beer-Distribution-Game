package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.JoinGameHandler
import com.asd2.beerdistributiongame.network.Client
import com.asd2.beerdistributiongame.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JoinGameState : State() {

	private val handler = JoinGameHandler()

	override fun onEnter() {
		Context.loadScene(handler.getViewFile())
	}

	override fun onExit() = Unit

	fun notifyToGoBackToMainMenu() {
        setState(MainMenuState())
	}

	fun notifyToConnectWith(ipAddress: String) = handler.process {
        val response = withContext(Dispatchers.Default) { Client.connect(ipAddress) }
        if (response.success) {
            Settings.lastSuccessfulIp = ipAddress
			if(response.data != null && response.data.playerIsReconnected)
				setState(GameState())
			 else
				setState(LobbyState())
        } else
            handler.showAlert(response.message)
    }
}