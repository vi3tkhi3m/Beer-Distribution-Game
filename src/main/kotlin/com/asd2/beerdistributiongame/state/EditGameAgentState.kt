package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.EditAgentHandler

class EditGameAgentState : State() {

    private val handler = EditAgentHandler()

    override fun onEnter() {
        Context.loadScene(handler.getViewFile())
    }

    override fun onExit() {
        //todo
    }

    fun notifyToGoBackToMainMenu() {
        setState(MainMenuState())
    }
}