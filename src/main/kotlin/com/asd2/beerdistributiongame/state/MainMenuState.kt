package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.MainMenuHandler

class MainMenuState : State() {

	private val handler = MainMenuHandler()

    override fun onEnter() {
        Context.loadScene(handler.getViewFile())
		Context.isConnected = false
		Context.isGameManager = false
		Context.isHost = false
		Context.players.clear()
    }

    override fun onExit() = Unit

	fun notifyToHostGame() {
		setState(GameConfigState())
	}

	fun notifyToOptions() {
        setState(SettingsState())
	}

	fun notifyToJoinGame() {
        setState(JoinGameState())
	}

	fun notifyToCreateGameAgent() {
        setState(CreateGameAgentState())
	}

	fun notifyToEditGameAgent() {
		setState(EditGameAgentState())
	}

	fun notifyToViewReplays() {
        setState(ReplayLobbyState())
	}

	fun notifyToQuitGame() {
		setState(CloseAppState())
	}

}