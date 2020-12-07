package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.settings.Settings

class InitialState : State() {

    override fun onEnter() {
        setState(if (Settings.containsUsername()) MainMenuState() else EnterNameState())
    }

    override fun onExit() = Unit
}