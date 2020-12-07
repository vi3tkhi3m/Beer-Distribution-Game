package com.asd2.beerdistributiongame.network

import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.service.ClientService

object Client {

    /**
     * Invoked after the next turn has been received.
     */
    var onNextTurn: (NextTurnDto) -> Unit = {}

    /**
     * Invoked after a new player successfully joins a game by the host.
     */
    var onConnect: (Player) -> Unit = {}

    /**
     * Invoked after a player disconnects himself or is detected that he has been disconnected
     */
    var onDisconnect: (Player?) -> Unit = {}

    /**
     * Invoked when a player chooses a role.
     */
    var onChooseRole: (ChooseRoleEvent) -> Unit = {}

    /**
     * Invoked when a player pauses or unpauses the game
     */
    var onPause: (Boolean) -> Unit = {}

    /**
     * Invoked when the game manager informs everyone that the game is over.
     */
    var onEndGame: () -> Unit = {}

    /**
     * Attempts to connect with a game where the game host is running on the given ip address.
     *
     * @param ipAddress the ip address of the host to connect with
     * @return the network response containing success and an optional error message when not successful
     */
    suspend fun connect(ipAddress: String) = ClientService.connect(ipAddress)

    /**
     * Informs the host that we are disconnecting from the game.
     */
    fun disconnect() = ClientService.disconnect()

    /**
     * Informs the host that we want to play the specified role in the supply chain.
     *
     * @param role the role to play as
     */
    suspend fun chooseRole(role: Roles) = ClientService.chooseRole(role)

    /**
     * Informs the host that we place an order of the given size this turn.
     *
     * @param order the amount of beer to place an order for
     * @return the network response containing the succes (200) or a failure with a message.
     */
    suspend fun placeOrder(order: Order) = ClientService.placeOrder(order)

    /**
     * Informs the host that we want to activate the provided game agent to play for us.
     *
     * @param gameAgentFormula the string formula of the complete game agent
     */
    suspend fun activateGameAgent(gameAgentFormula: String) = ClientService.activateGameAgent(gameAgentFormula)


    /**
     * Informs the host that we want to deactivate the game agent that is currently playing for us.
     */
    suspend fun deactivateGameAgent() = ClientService.disableGameAgent()

    /**
     * Tells the host to inform all connected players that the game has ended. This can only be executed by the game manager.
     */
    fun endGame() = ClientService.endGame()

    /**
     * Asks if the host is still alive. Will receive a statuscode based on status of the host: 200 when successful, 503 when connection times out.
     *
     * @return true when the host can be reached, false when it can't be reached.
     */
    suspend fun checkAlive(): Boolean = ClientService.checkAlive()

    suspend fun migrateHost() = ClientService.migrateHost()

    /**
     * Tells the host to inform all connected players that the game is paused or unpaused. This can only be executed by the game manager.
     */
    fun pauseGame(paused: Boolean) = ClientService.pauseGame(paused)

    fun sendHeartbeat(): Boolean = ClientService.sendHeartbeat()

    /**
     * Invoked after connection to the host was lost in the lobby, which means the client should return to the main menu.
     */
    var onLostConnection: () -> Unit = {}
}