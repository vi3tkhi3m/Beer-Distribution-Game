package com.asd2.beerdistributiongame.gamelogic.configuration

import java.util.*

enum class GameMode {
    LINEAR, PYRAMID, GRAPH
}

enum class OverviewType {
    GLOBAL, LOCAL
}

data class GameConfig(
        var gameID: String = UUID.randomUUID().toString(),
        var gameName: String = "",
        var hostName: String = "",
        var startGameTime: String = "",
        var endGameTime: String = "",
        var isFinished: Boolean = false,
        var gameMode: GameMode = GameMode.LINEAR,
        var standardGameAgent: String = "",
        var overviewType: OverviewType = OverviewType.LOCAL,
        var initialStock: Int = 10,
        var initialBudget: Int = 100,
        var turnDuration: Int = 60, // Seconds
        var backlogCost: Int = 3,
        var stockCost: Int = 2,
        var productionCost: Int = 2,
        var maxTurns: Int = 40,
        var infiniteTurns: Boolean = false,
        var quantityOfFactories: Int = 1,
        var quantityOfWarehouses: Int = 1,
        var quantityOfWholesales: Int = 1,
        var quantityOfRetailers: Int = 1,
        var retailProfit: Int = 5,
        var wholesaleProfit: Int = 4,
        var warehouseProfit: Int = 3,
        var factoryProfit: Int = 2,
        var minCustomerDemand: Int = 1,
        var maxCustomerDemand: Int = 20
)