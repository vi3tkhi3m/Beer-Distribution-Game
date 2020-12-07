package com.asd2.beerdistributiongame.gamelogic.game.gameflow

class PreviousTurnModel {
    var previousIncomingOrders: ArrayList<PreviousOrder> = ArrayList()
    var previousOutgoingOrders: ArrayList<PreviousOrder> = ArrayList()

    data class PreviousOrder(val turn: Int, val amount: Int, val cost: Double) {
    }
}