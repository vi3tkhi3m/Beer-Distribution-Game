package com.asd2.beerdistributiongame.network.service

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.controller.StorageController
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.broker.HostBroker
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.getCurrentMilliseconds
import com.asd2.beerdistributiongame.settings.Settings
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


object HostService {

    /**
     * Informs connected players of the next turn
     *
     * @param stateOfGame contains the current gamestate
     */
    @Synchronized
    suspend fun handleNextTurn(stateOfGame: StateOfGame){
        val nextTurnDto = NextTurnDto(stateOfGame, Context.beerDistributionGame)
        Context.players.filter {it.connected && !it.isHost}.forEach {
            if (!HostBroker.onNextTurn(it.ip, nextTurnDto).success) {
                it.connected = false
                it.gameAgentEnabled = true
            }
        }
    }

    /**
     * Sends the rolesInGame object from the host's context to the clients context.
     */
    suspend fun sendAllRoles() = Context.players.filter {it.uuid != Settings.uuid && it.connected && !it.isGameManager || !it.isHost}.forEach { HostBroker.sendAllRoles(it.ip,Context.rolesInGame) }

    /**
     * Informs connected players of the next turn by sending the gamestate and optionally sends an instance of the game at the start of a game.
     *
     * @param connectDto contains the uuid and username
     * @param remoteAddress the ip address of the user that sended the request
     * @return a boolean if the user is connected or not
     */
    @Synchronized
    fun handleConnect(connectDto: ConnectDto, remoteAddress: String): Boolean {
        val reconnectedPlayer = Context.players.singleOrNull { it.uuid == connectDto.uuid }
        if (reconnectedPlayer != null) {
            NetworkContext.isReconnected = true
            reconnectedPlayer.ip = remoteAddress
            reconnectedPlayer.connected = true
            reconnectedPlayer.username = connectDto.username
            informAllPlayersOfConnectedPlayer(reconnectedPlayer)
            Client.onConnect(reconnectedPlayer)
            return true
        }

        val totalSpots = Context.gameConfig.quantityOfFactories +
                Context.gameConfig.quantityOfRetailers +
                Context.gameConfig.quantityOfWarehouses +
                Context.gameConfig.quantityOfWholesales
        val anySpotsLeft = Context.players.size < totalSpots

        if (!anySpotsLeft) return false

        val newPlayer = Player(connectDto.uuid, connectDto.username, remoteAddress, true)
        Context.players.add(newPlayer)
        informAllPlayersOfConnectedPlayer(newPlayer)
        Client.onConnect(newPlayer)
        NetworkContext.lobbyPlayerHeartbeats[connectDto.uuid] = getCurrentMilliseconds()
        NetworkContext.isReconnected = false
        return true
    }
    /**
     * Sets the DTO values when a player is connecting
     */
    fun getConnectData() = ConnectResponseDto(Context.gameConfig, Context.players, Context.statesOfGame, NetworkContext.isReconnected, Context.rolesInGame, Context.beerDistributionGame?.remainingTime)

    private fun informAllPlayersOfConnectedPlayer(player: Player) {
        val onConnectDto = OnConnectDto(player)
        Context.players.filter {
            it.uuid != player.uuid && it.connected
        }.forEach {
            GlobalScope.launch {
                HostBroker.onConnect(it.ip, onConnectDto)
            }
        }
    }

    /**
     * The chosen role of the player will be added/replaced to rolesInGame in the context
     *
     * @param chooseRole the role that is chosen
     * @return Returns boolean of success
     */
    @Synchronized
    fun handleChooseRole(chooseRole: ChooseRoleDto): Boolean {
        if (!doesPlayerExist(chooseRole.uuid) || !isSpotAvailableForRole(chooseRole.role)) return false

        val player = Context.players.single { it.uuid == chooseRole.uuid }
        val oldRole = player.role
        player.role = chooseRole.role
        informAllPlayersOfPlayerChooseRole(chooseRole)
        Client.onChooseRole(ChooseRoleEvent(player, oldRole, chooseRole.role, Context.rolesInGame))
        for ((roleKey, roleOwner) in Context.rolesInGame.playerRoles) {
            if (roleOwner.playerId == chooseRole.uuid) Context.rolesInGame.playerRoles[roleKey]!!.playerId = null
            if (roleOwner.role == chooseRole.role && roleOwner.playerId.isNullOrEmpty()) Context.rolesInGame.playerRoles[roleKey]!!.playerId = chooseRole.uuid
        }
        StorageController.saveRolesInGame(Context.gameConfig.gameID, Context.rolesInGame)
        return true
    }

    /**
     * Sync new player roles to all connected clients
     *
     * @param chooseRole the ChooseRoleDTO
     */
    private fun informAllPlayersOfPlayerChooseRole(chooseRole: ChooseRoleDto) {
        Context.players.filter {
            it.connected && it.uuid != chooseRole.uuid
        }.forEach {
            GlobalScope.launch {
                HostBroker.onChooseRole(it.ip, chooseRole)
            }
        }
    }

    /**
     * Checks if the player exists in the context
     *
     * @param uuid the uuid of the user
     * @return Returns boolean of success
     */
    private fun doesPlayerExist(uuid: String): Boolean = Context.players.find { it.uuid == uuid } != null

    /**
     * Checks if there is a spot available for the given role.
     *
     * @param role The role
     * @return Returns true if there are spots available, else false.
     */
    private fun isSpotAvailableForRole(role: Roles): Boolean = when (role) {
        Roles.FACTORY -> Context.gameConfig.quantityOfFactories - (countPlayersWithRole(role)) > 0
        Roles.WAREHOUSE -> Context.gameConfig.quantityOfWarehouses - (countPlayersWithRole(role)) > 0
        Roles.WHOLESALE -> Context.gameConfig.quantityOfWholesales - (countPlayersWithRole(role)) > 0
        Roles.RETAILER -> Context.gameConfig.quantityOfRetailers - (countPlayersWithRole(role)) > 0
        else -> false
    }

    /**
     * Counts the amount of players with the same role
     *
     * @param role The role
     * @return Returns amount of players with the given role
     */
    private fun countPlayersWithRole(role: Roles) = Context.players.filter { it.role == role && it.connected }.count()


    /**
     * Handles the disconnect of a player. Informs all connected clients that the player has disconnected.
     *
     * @param disconnectDto contains information about the disconnected player
     */
    @Synchronized
    fun handleDisconnect(disconnectDto: DisconnectDto) {
        ClientService.handleOnDisconnect(disconnectDto)
        NetworkContext.lobbyPlayerHeartbeats.remove(disconnectDto.uuid)
        informAllPlayersOfDisconnectedPlayer(disconnectDto)
    }

    /**
     * Informs all the other players that one disconnects
     *
     * @param disconnectDto Disconnect data object
     */
    private fun informAllPlayersOfDisconnectedPlayer(disconnectDto: DisconnectDto) {
        Context.players.filter {
            it.uuid != disconnectDto.uuid && it.connected
        }.forEach {
            GlobalScope.launch {
                HostBroker.onDisconnect(it.ip, disconnectDto)
            }
        }
    }

    /**
     * Notifies all the players that the game has ended, but not himself, nor the game manager.
     */
    @Synchronized
    fun handleEndGame() {
        Context.players.filter {
            it.uuid != Settings.uuid && it.connected && !it.isGameManager
        }.forEach {
            GlobalScope.launch {
                HostBroker.onEndGame(it.ip)
            }
        }
    }

    /**
     * This function handles the placement of an order.
     *
     * @param placerOrderDto the order that was placed
     */
    @Synchronized
    fun handlePlaceOrder(placerOrderDto: PlacerOrderDto) = Host.onPlaceOrder(placerOrderDto.order)

    /**
     * Handles the received heartbeat
     */
    @Synchronized
    fun handleHeartbeat(user: HeartbeatDto) {
        NetworkContext.lobbyPlayerHeartbeats[user.uuid] = getCurrentMilliseconds()
    }

    /**
     * Handles the enabling of a gameagent.
     *
     * @param gameAgentDto the gameAgent needs to be activated
     */
    @Synchronized
    fun handleActivateGameAgent(gameAgentDto: GameAgentDto) {
        val player = Context.players.single { it.uuid == gameAgentDto.uuid }
        player.gameAgent = gameAgentDto.gameAgent
        player.gameAgentEnabled = true
        Host.onActivateGameAgent
    }

    /**
     * Disables a game agent for a user.
     *
     * @param user this object contains the UUID of a user.
     */
    @Synchronized
    fun handleDisableGameAgent(user: DisableGameAgentDto) {
        Context.players.single { it.uuid == user.uuid }.gameAgentEnabled = false
    }

    /**
     * Pauses or unpauses the game for all players
     *
     * @param params this object contains the uuid of a use and if the game should be paused or unpaused
     * @return true if successful and false if not
     */
    @Synchronized
    fun handlePauseGame(pauseGameDto: PauseGameDto): Boolean {
        Context.isPaused = pauseGameDto.paused
        val senderIsGameManager = Context.players.singleOrNull { it.uuid == pauseGameDto.uuid && it.isGameManager } != null
        val senderIsHost = pauseGameDto.uuid == Settings.uuid
        if (Context.isHost && (senderIsGameManager || senderIsHost)) {
            informAllPlayersOfGamePause(pauseGameDto.paused)
            return true
        }
        return false
    }

    /**
     * informs the players the game is paused.
     *
     * @param paused boolean true or false
     */
    private fun informAllPlayersOfGamePause(paused: Boolean) = Context.players.filter { it.uuid != Settings.uuid && it.connected }.forEach { GlobalScope.launch { HostBroker.pauseGame(it.ip, paused) } }

    /**
     * Gets the next index number for a new player
     *
     * @return next index number
     */
    private fun nextPlayerIndex(): Int {
        if (Context.players.isEmpty()) return 1
        return Context.players.maxBy { it.indexNumber }?.indexNumber!! + 1
    }

    /**
     * This function loops through all connected players and calls that they should host migrate.
     */
    fun hostMigrate() = Context.players.filter { it.uuid != Settings.uuid && it.connected }.forEach { GlobalScope.launch { HostBroker.hostMigrate(it.ip) } }
}