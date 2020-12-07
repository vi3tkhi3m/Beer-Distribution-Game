package com.asd2.beerdistributiongame.network.broker

import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.hostMigratePath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onChooseRolePath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onConnectPath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onDisconnectPath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onEndGamePath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onNextTurnPath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onPausePath
import com.asd2.beerdistributiongame.network.util.*
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object HostBroker {

    /**
     * This function sends a player to a certain ipaddress, to inform that ip that a new player has connected.
     *
     * @param ipAddress: ipAddress of the to connect computer
     * @param player: the player that needs to be transmitted
     * @return boolean, if it was successful
     */
    suspend fun onConnect(ipAddress: String, onConnectDto: OnConnectDto): NetworkResponse {
        return try {
            val response = withContext(Dispatchers.Default) {
                Unirest.post(clientUrl(ipAddress, onConnectPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(onConnectDto))
                        .asJson()
            }

            getNetworkResponse(response)
        } catch (exception: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }

    /** Sends gamestates to all connected clients on next turn
     * @param ipAddress: ipAddress of the to connect computer
     * @param nextTurnDto: an object which contains the gamestate and optionally contains an instance of the beerdistributiongame
     */
    suspend fun onNextTurn(ipAddress: String, nextTurnDto: NextTurnDto) : NetworkResponse{
        return try{
            Unirest.setTimeouts(OTHER_CONNECTION_TIMEOUT,OTHER_SOCKET_TIMEOUT)
            val response = withContext(Dispatchers.Default) {
                Unirest.post(clientUrl(ipAddress, onNextTurnPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(nextTurnDto))
                        .asJson()
            }
            getNetworkResponse(response)
        }
        catch (exception: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }

    /** Sends gamestates to all connected clients on next turn
     * @param ipAddress: ipAddress of the to connect computer
     * @param nextTurnDto: an object which contains the gamestate and optionally contains an instance of the beerdistributiongame
     */
    suspend fun sendAllRoles(ipAddress: String, rolesInGame: RolesInGame) : NetworkResponse{
        return try{
            val response = withContext(Dispatchers.Default) {
                Unirest.post(clientUrl(ipAddress, ClientEndpoint.sendAllRolesPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(rolesInGame))
                        .asJson()
            }

            getNetworkResponse(response)
        }
        catch (exception: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }

    /**
     * This function sends a disconnectDto to a certain ipaddress, to inform a ip that a player has disconnected
     *
     * @param ipAddress: ipAddress of the to connect computer
     * @param disconnectDto: an object with the uuid of a player
     */
    suspend fun onDisconnect(ipAddress: String, disconnectDto: DisconnectDto): NetworkResponse {
        return try {
            val response = withContext(Dispatchers.Default) {
                Unirest.post(clientUrl(ipAddress, onDisconnectPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(disconnectDto))
                        .asJson()
            }

            getNetworkResponse(response)
        } catch (e: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Sends a request to the onEndGamePath of the client
     *
     * @param clientIp The ip of the client, default port is 8080
     * @return A NetworkResponse with a success and a message
     */
    fun onEndGame(clientIp: String): NetworkResponse {
        return try {
            val response = Unirest.get(clientUrl(clientIp, onEndGamePath))
                    .asJson()

            getNetworkResponse(response)
        } catch (exception: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Sends a request to the onChooseRolePath of the client
     *
     * @param clientIp The ip of the client
     * @param chooseRole The ChooseRoleDto
     * @return A NetworkResponse with a success and a message
     */
    fun onChooseRole(clientIp: String, chooseRoleDto: ChooseRoleDto): NetworkResponse {
        return try {
            val response = Unirest.post(clientUrl(clientIp, onChooseRolePath))
                    .header(CONTENT_TYPE, MEDIA_TYPE)
                    .body(toJson(chooseRoleDto))
                    .asJson()

            getNetworkResponse(response)
        } catch (exception: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Sends a request to all clients
     *
     * @param clientIp The ip of the client, default port is 8080
     * @return A NetworkResponse with a success and a message
     */
    fun pauseGame(clientIp: String, paused: Boolean): NetworkResponse {
        return try {
            val response = Unirest.post(clientUrl(clientIp, onPausePath))
                    .header(CONTENT_TYPE, MEDIA_TYPE)
                    .body(toJson(paused))
                    .asJson()

            getNetworkResponse(response)
        } catch (exception: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Sends a request to a clients
     *
     * @param clientIp The ip of the client
     * @return A NetworkResponse with a success and a message
     */
    fun hostMigrate(clientIp: String): NetworkResponse {
        return try {
            val response = Unirest.get(clientUrl(clientIp, hostMigratePath)).asJson()
            getNetworkResponse(response)
        } catch (exception: UnirestException) {
            getClientCannotBeReachedNetworkResponse()
        }
    }
}