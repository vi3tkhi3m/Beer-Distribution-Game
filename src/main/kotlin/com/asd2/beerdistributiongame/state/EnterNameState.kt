package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.EnterNameHandler

class EnterNameState : State() {

    private val handler = EnterNameHandler()

    override fun onEnter() {
        Context.loadScene(handler.getViewFile())
    }

    override fun onExit() = Unit

    fun notifyToContinueToMainMenu() = setState(MainMenuState())

    fun notifyToQuitGame() = setState(CloseAppState())
}