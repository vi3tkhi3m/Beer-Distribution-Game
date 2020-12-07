package com.asd2.beerdistributiongame.network

import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame

data class NetworkDataResponse<T>(
        val success: Boolean,
        val message: String = "",
        val data: T? = null
)

data class NetworkResponse(
        val success: Boolean,
        val message: String = ""
)

data class ChooseRoleEvent(
        val player: Player,
        val oldRole: Roles,
        val newRole: Roles,
        val rolesInGame: RolesInGame
)

data class PlaceOrderEvent(
        val player: Player,
        val order: Int
)

data class ActivateGameAgentEvent(
        val player: Player,
        val gameAgentFormula: String
)