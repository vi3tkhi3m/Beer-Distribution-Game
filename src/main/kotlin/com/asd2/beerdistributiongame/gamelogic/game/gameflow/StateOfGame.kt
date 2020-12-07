package com.asd2.beerdistributiongame.gamelogic.game.gameflow

import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Delivery
import com.fasterxml.jackson.annotation.JsonProperty

data class StateOfGame(@get:JsonProperty("turnNumber") var turnNumber: Int) {
    @get:JsonProperty("playerStates")var playerStates: HashMap<String, StateOfPlayer> = HashMap()
    @get:JsonProperty("factoryQueue")var factoryQueue: ArrayList<Delivery> = ArrayList()
}