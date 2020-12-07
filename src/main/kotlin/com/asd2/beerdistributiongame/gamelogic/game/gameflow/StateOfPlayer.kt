package com.asd2.beerdistributiongame.gamelogic.game.gameflow

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
class StateOfPlayer(
        @get:JsonProperty("player") val player: String?,
        @get:JsonProperty("gameAgentActive") val gameAgentActive: Boolean,
        @get:JsonProperty("gameAgentFormula") var gameAgentFormula: String,
        @get:JsonProperty("backlogs") val backlogs: HashMap<String, Int> = HashMap(),
        @get:JsonProperty("stock") val stock: Int = 0,
        @get:JsonProperty("budget") val budget: Int = 0,
        @get:JsonProperty("incomingOrders") val incomingOrders: HashMap<String, Int> = HashMap(),
        @get:JsonProperty("outgoingOrder") val outgoingOrder: Int = 0,
        @get:JsonProperty("incomingDelivery") val incomingDelivery: Int = 0,
        @get:JsonProperty("outgoingDeliveries") val outgoingDeliveries: HashMap<String, Int> = HashMap(),
        @get:JsonProperty("productionDelay") val productionDelay: Int = 0
)