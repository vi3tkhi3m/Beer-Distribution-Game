package com.asd2.beerdistributiongame.context

import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class Player(
        @get:JsonProperty val uuid: String,
        @get:JsonProperty var username: String,
        @get:JsonProperty var ip: String,
        @get:JsonProperty var connected: Boolean,
        @get:JsonProperty var role: Roles = Roles.NONE,
        @get:JsonProperty var isGameManager: Boolean = false,
        @get:JsonProperty var gameAgent: String = "",
        @get:JsonProperty var gameAgentEnabled: Boolean = false,
        @get:JsonProperty var isHost: Boolean = false,
        @get:JsonProperty var indexNumber: Int = 1
)