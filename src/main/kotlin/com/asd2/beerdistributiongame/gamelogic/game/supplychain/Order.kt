package com.asd2.beerdistributiongame.gamelogic.game.supplychain

data class Order(val origin: String, val destination: String, val amount: Int = 0)