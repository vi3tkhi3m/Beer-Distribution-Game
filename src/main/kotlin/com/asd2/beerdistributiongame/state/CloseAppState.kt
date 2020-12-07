package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.network.Client

class CloseAppState : State() {


    override fun onEnter() {
        if (Context.isConnected) {
            if (Context.isGameManager)
                Client.endGame()
            else
                Client.disconnect()
        }
        Context.taskScheduler.shutdown()
        Context.stage.close()
        Context.springContext?.close()
        Context.beerDistributionGame?.cancelTimer()
        System.exit(0)
    }

    override fun onExit() = Unit
}