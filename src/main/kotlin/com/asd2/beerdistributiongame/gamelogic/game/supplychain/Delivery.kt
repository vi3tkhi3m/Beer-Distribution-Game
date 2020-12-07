package com.asd2.beerdistributiongame.gamelogic.game.supplychain

data class Delivery(val origin: String, val destination: String, val amount: Int = 0, var turnsInQueue: Int = 2)