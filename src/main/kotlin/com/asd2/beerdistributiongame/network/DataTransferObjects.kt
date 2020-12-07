package com.asd2.beerdistributiongame.network

import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.gamelogic.game.BeerDistributionGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order

data class ConnectDto(
        val uuid: String,
        val username: String
)


data class OnConnectDto(
        val player: Player
)

data class PlacerOrderDto(
        val order: Order
)

data class ConnectResponseDto(
        val gameConfig: GameConfig = GameConfig(),
        val players: List<Player> = ArrayList(),
        var statesOfGame: ArrayList<StateOfGame> = ArrayList(),
        var playerIsReconnected: Boolean = false,
        val rolesInGame: RolesInGame,
        val remainingTime: String?
)

data class DisconnectDto(
        val uuid: String
)

data class ChooseRoleDto(
        var uuid: String,
        var role: Roles
)

data class NextTurnDto(
        val stateOfGame: StateOfGame,
        val beerDistributionGame: BeerDistributionGame? = null
)
data class GameAgentDto(
        val uuid: String,
        val gameAgent: String
)

data class HeartbeatDto(
        val uuid: String
)

data class DisableGameAgentDto(
        val uuid: String
)

data class PauseGameDto(
        val uuid: String,
        val paused: Boolean
)