package com.asd2.beerdistributiongame.gamelogic.game.supplychain.chainrole

open class SupplyChainRole(
        val roleKey: String,
        val playerID: String?,
        val stockCost: Int = 0,
        val backlogCost: Int = 0,
        val productionCost: Int = 0
) {
    val maxCapacity: Int = 999
    val backlogs: HashMap<String, Int> = HashMap()

    var stock: Int = 0
    var budget: Int = 0
    var incomingOrder: HashMap<String, Int> = HashMap()
    var outgoingOrder: Int = 0
    var incomingDelivery: Int = 0
    var outgoingDelivery: HashMap<String, Int> = HashMap()
    var deliveryGain: Int = 0
}