package com.asd2.beerdistributiongame.network.util

import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order

object NetworkContext {

    const val HOST_PORT = 8080
    var hostIp: String = LOCALHOST
    var isReconnected: Boolean = false

    /**
     * bufferedOrder is a order buffer for when host migration needs to happen and the already placed order needs to be sent again
     */
    var bufferedOrder: Order? = null

    /**
     * lobbyPlayerHeartbeats stores the player's uuid as a String and the timestamp of their last heartbeat as a Long.
     */
    var lobbyPlayerHeartbeats: HashMap<String, Long> = HashMap()
}