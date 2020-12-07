package com.asd2.beerdistributiongame.gamelogic.game.gameflow

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order

class GameFlow() {

    val gameConfig = Context.gameConfig
    val rolesInGame = Context.rolesInGame
    val placedOrders: HashMap<String, Order> = HashMap()
    var currentTurn: Int = 0
    val maxTurns: Int = gameConfig.maxTurns
    val turnTime: Int = gameConfig.turnDuration

    init {
        resetPlacedOrders()
    }

    fun resetPlacedOrders() {
        rolesInGame.playerRoles.keys.forEach {
            placedOrders[it] = Order("", "", -1)
        }
    }

    fun receiveOrder(order: Order) {
        placedOrders[order.origin] = order
        if (checkIfHostReceivedSufficientOrders()) {
            Context.beerDistributionGame!!.nextTurn()
            resetPlacedOrders()
        }
    }

    fun placeOrder(order: Order) {
        placedOrders[order.origin] = order
    }

    private fun checkIfHostReceivedSufficientOrders(): Boolean {
        placedOrders.forEach {
            if(it.value.amount == -1) return false
        }
        return true
    }

    fun checkIfCurrentTurnIsEqualToMaxTurns() = currentTurn == maxTurns
}