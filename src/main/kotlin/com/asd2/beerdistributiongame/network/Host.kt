package com.asd2.beerdistributiongame.network

import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.service.HostService


object Host {

    /**
     * Invoked after a player successfully joins the game
     */
    var onConnect: (Player) -> Unit = {}

    /**
     * Invoked when a player chooses a role
     */
    var onChooseRole: (ChooseRoleEvent) -> Unit = {}

    /**
     * Gets invoked when a player has placed an order
     */
    var onPlaceOrder: (Order) -> Unit = {}

    /**
     * Invoked when a player activates a game agent
     */
    var onActivateGameAgent: (ActivateGameAgentEvent) -> Unit = {}

    /**
     * Invoked when a player deactivates his game agent
     */
    var onDeactivateGameAgent: (Player) -> Unit = {}

    /**
     * Invoked when the game manager ends the game.
     */
    var onEndGame: (List<Player>) -> Unit = {}

    /**
     * Informs all connected players that the host is leaving the game, making them select a new host.
     */
    fun disconnect() {
        //todo
    }

    /**
     * Invoked when a player is disconnected by the host after not receiving a heartbeat in the programmed amount of time
     */
    var onHostRemovesPlayerFromLobby: (Player) -> Unit = {}

    /**
     * Sends all the roles from the host context to the clients
     */
    suspend fun sendAllRoles() = HostService.sendAllRoles()

    /**
     * Sends the next game state to all the connected clients, informing them that the next turn has started
     */
    suspend fun nextTurn(stateOfGame: StateOfGame?) = HostService.handleNextTurn(stateOfGame!!)

    /**
     * This function loops through all connected players and calls that they should host migrate.
     */
    fun migrateHost() = HostService.hostMigrate()
}