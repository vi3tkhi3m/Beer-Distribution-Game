package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context

abstract class State {
    abstract fun onEnter()
    abstract fun onExit()

    protected fun setState(newState: State) {
        Context.activeState = newState
    }
}