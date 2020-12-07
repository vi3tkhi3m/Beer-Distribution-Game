package com.asd2.beerdistributiongame.network.service

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.gamelogic.controller.StorageController
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.broker.ClientBroker
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.NetworkContext.bufferedOrder
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.LobbyState
import kotlinx.coroutines.*
import sun.plugin.dom.exception.InvalidStateException

object ClientService {

    private const val TIMEOUT_ON_FAILURE: Long = 1000
    private const val RETRY_ON_FAILURE_NR: Int = 3
    private const val DELAY_BEFORE_NEXT_ROUND: Long = 3000

    /**
     * Handles the business logic to connect a client with a host.
     *
     * @param ipAddress the ip address to connect with
     * @throws InvalidStateException when the client is already connected with a host
     * @return the network response containing success and an optional error message when not successful
     */
    suspend fun connect(ipAddress: String): NetworkDataResponse<ConnectResponseDto> {
        if (Context.isConnected) throw InvalidStateException("Already connected to a host!")
        val response = ClientBroker.connect(ipAddress)
        Context.isConnected = response.success
        if (response.data != null) {
            Context.gameConfig = response.data.gameConfig
            Context.rolesInGame = response.data.rolesInGame
            Context.isConnected = true
            Context.players.clear()
            Context.players = response.data.players.toMutableList()
        }
        return response

    }

    /**
     * Connects to host. Retries given number of times if connection fails.
     * @param ipAddress the ip address to connect with
     * @param retryNr the number of times to connect
     * @param waitTime the time to wait to connect again in ms
     * @throws InvalidStateException when the client is already connected with a host
     * @return the network response containing success and an optional error message when not successful
     */
    suspend fun attemptToConnect(ipAddress: String, retryNr: Int, waitTime: Long = TIMEOUT_ON_FAILURE): NetworkDataResponse<ConnectResponseDto> {
        val response = connect(ipAddress)
        return if (retryNr > 1 && !response.success) attemptToConnect(ipAddress, retryNr - 1, waitTime) else response
    }

    /**
     * Notifies the host that the game is over
     */
    fun endGame() {
        if (Context.isHost) HostService.handleEndGame() else ClientBroker.endGame()
        Context.isConnected = false
    }

    /**
     * The game manager informs that the game is over.
     */
    fun handleEndGame() {
        Context.isConnected = false
        Client.onEndGame()
    }

    /**
     * Informs the host that we are disconnecting.
     * @throws InvalidStateException when the client is not connected
     */
    fun disconnect() {
        GlobalScope.launch { ClientBroker.disconnect() }
        Context.isConnected = false
        Context.gameConfig = GameConfig()
    }

    /**
     * Handles the logic when another player connects to the game
     *
     * @param player the player that joined the game
     */
    fun handleOnConnect(onConnectDto: OnConnectDto) {
        val reconnectedPlayer = Context.players.singleOrNull { it.uuid == onConnectDto.player.uuid }
        if (reconnectedPlayer != null) {
            reconnectedPlayer.ip = onConnectDto.player.ip
            reconnectedPlayer.username = onConnectDto.player.username
            reconnectedPlayer.connected = true
            Client.onConnect(reconnectedPlayer)
        } else {
            Context.players.add(onConnectDto.player)
            Client.onConnect(onConnectDto.player)
        }
    }

    /**
     * Handles the business logic of the turn
     * @throws InvalidStateException when game has already ended
     */
    fun handleNextTurn(nextTurnDto: NextTurnDto) = Client.onNextTurn(nextTurnDto)

    /**
     * Handles the logic when another player disconnects from the game
     *
     * @param disconnectDto contains information about the player that disconnected
     */
    fun handleOnDisconnect(disconnectDTO: DisconnectDto) {
        val disconnectedPlayer = Context.players.single { it.uuid == disconnectDTO.uuid }
        if (Context.activeState is LobbyState) Context.players.removeIf { it.uuid == disconnectDTO.uuid } else disconnectedPlayer.connected = false
        Client.onDisconnect(disconnectedPlayer)
    }

    /**
     * Handles the business logic to choose a role.
     *
     * @param role the chosen role
     * @return the network response containing success and an optional error message when not successful
     */
    suspend fun chooseRole(role: Roles): NetworkResponse {
        val networkResponse = ClientBroker.chooseRole(role)
        if (networkResponse.success) Context.players.single { it.uuid == Settings.uuid }.role = role
        return networkResponse
    }

    /**
     * Sync the role that the client receives from the host to the client's context.
     *
     * @param chooseRole the ChooseRoleDto
     */
    fun handleOnChooseRole(chooseRole: ChooseRoleDto) {
        val player = Context.players.single { it.uuid == chooseRole.uuid }
        val oldRole = player.role
        player.role = chooseRole.role
        for ((roleKey, roleOwner) in Context.rolesInGame.playerRoles) {
            if (roleOwner.playerId == chooseRole.uuid) {
                Context.rolesInGame.playerRoles[roleKey]!!.playerId = null
            }
            if (roleOwner.role == chooseRole.role && roleOwner.playerId.isNullOrEmpty()) {
                Context.rolesInGame.playerRoles[roleKey]!!.playerId = chooseRole.uuid
            }
        }
        StorageController.saveRolesInGame(Context.gameConfig.gameID, Context.rolesInGame)
        Client.onChooseRole(ChooseRoleEvent(player, oldRole, chooseRole.role, Context.rolesInGame))
    }

    /**
     * Pauses or resumes the game
     *
     * @param paused true if game should be paused, false if game should be unpaused
     */
    fun handlePause(paused: Boolean) {
        Context.isPaused = paused
        Client.onPause(paused)
    }

    /**
     * Handles the business logic to check whether the host can be reached.
     *
     * @return a boolean of whether or not the host was reached.
     */
    suspend fun checkAlive() = ClientBroker.checkAlive()

    /**
     * Handles the business logic for placing an order.
     *
     * @return the network response containing success or failure with a message.
     */
    suspend fun placeOrder(order: Order): NetworkResponse{
        bufferedOrder = order
        return ClientBroker.placeOrder(order)
    }

    /**
     * Handles the business logic for activating a gameagent.
     *
     * @param gameAgentFormula the formula of a gameagent
     * @return the network response containing success or failure with a message.
     */
    suspend fun activateGameAgent(gameAgentFormula: String): NetworkResponse {
        val gameAgentDTO = GameAgentDto(Settings.uuid, gameAgentFormula)
        return ClientBroker.activateGameAgent(gameAgentDTO)
    }

    /**
     * The player informs that he wants to disable his game agent.
     *
     * @return the network response containing success or failure with a message.
     */
    suspend fun disableGameAgent() = ClientBroker.disableGameAgent()


    /**
     * This function checks if the current player is the next one to be host,
     * if that is true the current player transforms into the host. If it is not
     * true the current player tries to connect to the next player that is possibly the host.
     */
    suspend fun migrateHost() {
        Context.isConnected = false
        for (i in Context.players.filter {it.connected}.sortedByDescending { it.indexNumber }) {
            val amINextHost = i.uuid == Settings.uuid
            if (amINextHost) {
                becomeNewHost(i.ip)
                delay(DELAY_BEFORE_NEXT_ROUND)
                Context.statesOfGame.last().turnNumber = Context.statesOfGame.last().turnNumber -1
                HostService.handleNextTurn(Context.statesOfGame.last())
                return
            } else if (attemptToConnect(i.ip, RETRY_ON_FAILURE_NR).success) {
                if (NetworkContext.bufferedOrder != null) placeOrder(NetworkContext.bufferedOrder!!)
                return
            }
        }
    }

    /**
     * When the host manually disconnects, this function is used to call the real migrateHost function.
     */
    fun handleMigrateHost() = GlobalScope.launch { withContext(Dispatchers.Default) { migrateHost() } }

    /**
     * This function is called when a player is chosen to be host. This function handles all things
     * that are needed to be done when a new player is chosen to be host.
     *
     * @param ip the ip address
     */
    private fun becomeNewHost(ip: String) {
        Context.isConnected = true
        Context.players
                .filter { it.uuid != Settings.uuid }
                .forEach {
                    it.connected = false
                    it.gameAgentEnabled = true
                }
        Context.isHost = true
        NetworkContext.hostIp = ip
    }

    /**
     * Notifies the host that the game is paused or unpaused.
     *
     * @param paused if game should be paused, false if game should be unpaused
     */
    fun pauseGame(paused: Boolean) {
        if (Context.isHost) HostService.handlePauseGame(PauseGameDto(Settings.uuid, paused)) else ClientBroker.pauseGame(paused)
        Context.isPaused = paused
    }

    /**
     * Handles the business logic for sending a heartbeat.
     *
     * @return true or false, corresponding to the fact whether the heartbeat was successful or not.
     */
    fun sendHeartbeat(): Boolean = ClientBroker.sendHeartbeat().success

    /**
     * Checks if the given IP matches the Host IP.
     *
     * @return true or false, corresponding to the fact whether ip matches the host ip or not.
     */
    fun checkIfIpIsHostIp(possibleHostIp: String) = possibleHostIp == NetworkContext.hostIp

    /**
     * Updates the RolesInGame object in the context.
     *
     * @param rolesInGame the RolesInGame object
     */
    fun onSendAllRoles(rolesInGame: RolesInGame) {
        Context.rolesInGame = rolesInGame
    }
}