package com.asd2.beerdistributiongame.gamelogic.game

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Context.gameConfig
import com.asd2.beerdistributiongame.frontend.GameHandler
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.controller.StorageController
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.GameFlow
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.PreviousTurnModel
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.SupplyChain
import com.asd2.beerdistributiongame.network.Client
import com.asd2.beerdistributiongame.network.Host
import com.asd2.beerdistributiongame.state.GameState
import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import kotlinx.coroutines.*
import java.util.*
import java.util.TimerTask
import kotlin.concurrent.scheduleAtFixedRate


@JsonIgnoreProperties(ignoreUnknown = true)
class BeerDistributionGame {
    var supplyChain = SupplyChain()
    var gameFlow = GameFlow()
    var gameID = gameConfig.gameID
    @JsonIgnore private val timer = Timer()
    @JsonIgnore private var timerTask: TimerTask? = null
    var remainingTime = ""

    fun initialSave(gameConfig: GameConfig) {
        StorageController.saveGameConfig(gameConfig)
        StorageController.saveTurn(gameID, Context.statesOfGame)
        StorageController.saveRolesInGame(gameID, Context.rolesInGame)
    }

    fun nextTurn() {
        supplyChain.updateChainFlow(gameFlow.placedOrders)
        Context.statesOfGame.add(getStateOfGame())
        StorageController.saveTurn(gameID, Context.statesOfGame)

        GlobalScope.launch {
            (Context.activeState as GameState).nextTurn()
        }

        if (gameFlow.checkIfCurrentTurnIsEqualToMaxTurns()) {
            (Context.activeState as GameState).notifyToEndGame()
        }
    }

    fun getCurrentTurn(): Int = this.gameFlow.currentTurn

    fun getStateOfGame(): StateOfGame {
        val stateOfGame = StateOfGame(getCurrentTurn())
        val playerRoles = supplyChain.chainStructure.playerRoles
        for ((roleKey, node) in playerRoles) {
            val role = node.representative
            val stateOfPlayer = StateOfPlayer(
                    player = role.playerID,
                    gameAgentActive = getGameAgentActive(roleKey),
                    gameAgentFormula = getGameAgentFormula(roleKey),
                    backlogs = role.backlogs,
                    stock = role.stock,
                    budget = role.budget,
                    incomingOrders = role.incomingOrder,
                    outgoingOrder = role.outgoingOrder,
                    incomingDelivery = role.incomingDelivery,
                    outgoingDeliveries = role.outgoingDelivery
            )
            stateOfGame.playerStates[roleKey] = stateOfPlayer
        }
        stateOfGame.factoryQueue = supplyChain.factoryQueue
        return stateOfGame
    }

    fun getPreviousTurnInfo(gameConfig: GameConfig): PreviousTurnModel {
        val turnHistory = StorageController.getTurnHistory(gameConfig.gameID)
        val model = PreviousTurnModel()

        val previousIncomingOrders: ArrayList<PreviousTurnModel.PreviousOrder> = ArrayList()
        val previousOutgoingOrders: ArrayList<PreviousTurnModel.PreviousOrder> = ArrayList()

        turnHistory.forEach { turn ->
            turn.playerStates.forEach {
                if (it.value.player == gameConfig.gameID) {
                    it.value.incomingOrders.forEach {
                        previousIncomingOrders.add(PreviousTurnModel.PreviousOrder(turn.turnNumber, it.value, (it.value * gameConfig.productionCost).toDouble()))
                    }
                    previousOutgoingOrders.add(PreviousTurnModel.PreviousOrder(turn.turnNumber, it.value.outgoingOrder, (it.value.outgoingOrder * gameConfig.productionCost).toDouble()))
                }
            }
        }

        model.previousIncomingOrders = previousIncomingOrders
        model.previousOutgoingOrders = previousOutgoingOrders

        return model
    }

    private fun getGameAgentFormula(roleKey: String): String {
        return if (Context.rolesInGame.playerRoles[roleKey]!!.playerId.isNullOrEmpty()) {
            Context.gameConfig.standardGameAgent
        } else {
            val player = Context.players.singleOrNull { it.uuid == Context.rolesInGame.playerRoles[roleKey]!!.playerId }
            when (player) {
                null -> Context.gameConfig.standardGameAgent
                else -> player.gameAgent
            }
        }
    }

    fun getGameAgentActive(roleKey: String): Boolean {
        return if (Context.rolesInGame.playerRoles[roleKey]!!.playerId.isNullOrEmpty()) {
            true
        } else {
            val player = Context.players.singleOrNull { it.uuid == Context.rolesInGame.playerRoles[roleKey]!!.playerId }
            when (player) {
                null -> true
                else -> player.gameAgentEnabled
            }
        }
    }

    /**
     * Creates and starts a TimerTask with a given duration. Will also migrate the host when the timer has ended.
     *
     * @param duration The duration of the TimerTask
     */
    @Synchronized
    fun startTimer(duration: Int = 300) {
        cancelTimer()
        var interval = duration
        timerTask = timer.scheduleAtFixedRate(1000, 1000) {
            updateRemainingTime(interval)
            val needsToHostMigrate = interval <= -5 && !Context.isHost
            val needsToStartNewRoundAndProcessOrders = Context.isHost && interval <= 0
            val needsToUpdateTimerOnFrontend = interval > 0
            when {
                needsToHostMigrate -> {
                    cancelTimer()
                    migrateHost()
                }
                needsToStartNewRoundAndProcessOrders -> {
                    runBlocking { Context.beerDistributionGame!!.nextTurn() }
                    gameFlow.resetPlacedOrders()
                }

                //TODO: check of je spelhost bent en zo ja dan zorg je dat je nextturn aanroept.
                needsToUpdateTimerOnFrontend -> {
                    updateRemainingTime(interval)
                    interval--
                }
                else -> interval--
            }
        }
    }

    fun cancelTimer() {
        timerTask?.cancel()
    }

    /**
     * Updates the remaining time on the frontend
     *
     * @param timeInSecs is the time in seconds
     */
    private fun updateRemainingTime(timeInSecs: Int) {
        if (Context.isGameManager)
            return

        GlobalScope.launch(Dispatchers.Main) {
            val minutes = timeInSecs / 60
            val seconds = timeInSecs % 60
            ((Context.activeState as GameState).handler as GameHandler).remainingTime = String.format("%02d:%02d", minutes, seconds)
        }
    }

    fun migrateHost() = GlobalScope.launch {
        withContext(Dispatchers.Default) { Client.migrateHost() }
    }
}