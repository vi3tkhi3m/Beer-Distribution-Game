package com.asd2.beerdistributiongame.context

import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.gamelogic.game.BeerDistributionGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.state.InitialState
import com.asd2.beerdistributiongame.state.State
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler

object Context {
    var taskScheduler = ThreadPoolTaskScheduler()
    var springContext: ConfigurableApplicationContext? = null
    lateinit var stage: Stage

    /**
     * Set the activeState in order to perform state logic
     */
    var activeState: State = InitialState()
    set(value) {
        activeState.onExit()
        field = value
        activeState.onEnter()
    }

    /**
     * Call loadScene from your custom state in order to load a new view
     */
    fun loadScene(viewFile: String) {
        if (!::stage.isInitialized)
            return
        val xmlScene: Parent = FXMLLoader.load(javaClass.classLoader.getResource(viewFile))
        Context.stage.scene = Scene(xmlScene)
        Context.stage.scene.stylesheets.add("default-style.css") // Default stylesheet
    }

    var gameConfig = GameConfig()
    var beerDistributionGame : BeerDistributionGame? = null
    var rolesInGame = RolesInGame()
    var players = mutableListOf<Player>()
    var isGameManager = false
    var isHost = false
    var isConnected = false
    var statesOfGame = ArrayList<StateOfGame>()
    var chartToView =  "outgoingOrder"
    var isPaused = false
    var replayTurnNumber: Int = 1
}