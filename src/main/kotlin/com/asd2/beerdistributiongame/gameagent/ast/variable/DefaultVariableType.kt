package com.asd2.beerdistributiongame.gameagent.ast.variable

enum class DefaultVariableType(val variableName: String) {
    INCOMING_ORDER("IncomingOrder"),
    INCOMING_DELIVERY("IncomingDelivery"),
    STOCK("Stock"),
    BUDGET("Budget"),
    BACKLOG("Backlog"),
    ORDER_COST("ProductionCost"),
    STOCK_COST("StockCost"),
    BACKLOG_COST("BacklogCost"),
    DELIVERY_GAIN("DeliveryGain"),
    ORDER("Order")
}