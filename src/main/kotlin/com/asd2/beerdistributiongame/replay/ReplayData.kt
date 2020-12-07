package com.asd2.beerdistributiongame.replay

import java.io.File

data class ReplayGameData(val gameID: String, val name: String, val turns: Int, val type: String)

data class PersonStatistics(
        val turnNumber: Int,
        val gameAgentActive: Boolean,
        val backlog: Int = 0,
        val stock: Int = 0,
        val budget: Int = 0,
        val incomingOrders: Int = 0,
        val outgoingOrder: Int = 0,
        val incomingDelivery: Int = 0,
        val outgoingDeliveries: Int = 0)

data class UserRanking(val ranking: Int, val username: String, val points: Int, val uuid: String?)

data class SupplyChainLinkDetails(var name: String, var inventory: Int, var incomingOrders: Int, var outgoingOrders: Int, var incomingBeer: Int, var outgoingBeer: Int, var productionDelay: Int?)

object SavedGamePath {
    var savedGamesDir = ".${File.separator}data${File.separator}savedgames${File.separator}"
    const val playerFileName = "players.json"
    const val gameConfigFileName = "gameConfig.json"
    const val rolesInGameFileName = "rolesInGame.json"
    const val turnFileName = "turns.json"
}
