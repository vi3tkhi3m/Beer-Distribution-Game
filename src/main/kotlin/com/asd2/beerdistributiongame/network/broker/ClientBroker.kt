package com.asd2.beerdistributiongame.network.broker

import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.activateGameAgentPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.checkAlivePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.chooseRolePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.connectPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.disableGameAgentPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.disconnectPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.endGamePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.pauseGamePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.placeOrderPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.sendHeartbeatPath
import com.asd2.beerdistributiongame.network.util.*
import com.asd2.beerdistributiongame.settings.Settings
import com.mashape.unirest.http.Unirest
import com.mashape.unirest.http.exceptions.UnirestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import mapJSON
import org.springframework.http.HttpStatus

object ClientBroker {

    /**
     * Connects to the host. The client will send user information, see model.User.
     * Also sets the hostIp in the network.context
     *
     * @param ipAddress The ip address of the host
     * @return A NetworkDataResponse with a success and a message
     */
    suspend fun connect(ipAddress: String): NetworkDataResponse<ConnectResponseDto> {
        try {
            NetworkContext.hostIp = ipAddress
            Unirest.setTimeouts(CONNECTION_TIMEOUT, SOCKET_TIMEOUT)
            val response = withContext(Dispatchers.Default) {
                Unirest.post(hostUrl(connectPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(ConnectDto(Settings.uuid, Settings.username)))
                        .asJson()
            }

            if (response.status != HttpStatus.OK.value())
                return NetworkDataResponse(false, "Could not connect. Status code: ${response.status} ${response.statusText}")

            val connectResponseDTO = mapJSON<ConnectResponseDto>(response.body.toString())
            return NetworkDataResponse(true, data = connectResponseDTO)
        } catch (e: UnirestException) {
            return NetworkDataResponse(false, HOST_CANNOT_BE_REACHED)
        }
    }

    /**
     * Wraps an uuid in a DisconnectDto to send it to the host, saying, it wants to disconnect.
     *
     * @return NetworkResponse with a success or a failure with a message
     **/
    fun disconnect(): NetworkResponse {
        return try {
            val response = Unirest.post(hostUrl(disconnectPath))
                    .header(CONTENT_TYPE, MEDIA_TYPE)
                    .body(toJson(DisconnectDto(Settings.uuid)))
                    .asJson()

            getNetworkResponse(response)
        } catch (e: UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Sends the chosen role to the host.
     *
     * @param role the selected role
     * @return A NetworkResponse with a success and a message
     */
    suspend fun chooseRole(role: Roles): NetworkResponse {
        return try {
            val response = withContext(Dispatchers.Default) {
                Unirest.post(hostUrl(chooseRolePath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(ChooseRoleDto(Settings.uuid, role)))
                        .asJson()
            }

            getNetworkResponse(response)
        } catch (e: UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }

    }

    /**
     * Checks if the host is still alive. Makes an asynchronous call because other functionalities should still work while this is
     * being executed.
     *
     * @returns true when the host can be reached, returns false when it can't.
     */
    suspend fun checkAlive(): Boolean {
        return try {
            val response = withContext(Dispatchers.Default) {
                Unirest.get(hostUrl(checkAlivePath))
                        .asJson()
            }
            response.status == HttpStatus.OK.value()
        } catch (e: UnirestException) {
            false
        }
    }

    /**
     * Calls the host to end the game
     *
     * @return NetworkResponse with a success or a failure with a message
     */
    fun endGame(): NetworkResponse {
        return try {
            val response = Unirest.get(hostUrl(endGamePath)).asJson()

            getNetworkResponse(response)
        } catch (exception: UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Places an order at the host.
     *
     * @param order The order object being sent to the host.
     * @return NetworkResponse with a success or a failure with a message
     */
    suspend fun placeOrder(order: Order): NetworkResponse {
        return try {
            val response = withContext(Dispatchers.Default) {
                Unirest.post(hostUrl(placeOrderPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(PlacerOrderDto(order)))
                        .asJson()
            }

            getNetworkResponse(response)
        } catch (e: UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Sends the heartbeat to the host to signify the player is still connected.
     * @return A NetworkResponse with a succes or a failure with a message
     */
    fun sendHeartbeat(): NetworkResponse {
        return try {
            val response =   Unirest.post(hostUrl(sendHeartbeatPath))
                    .header(CONTENT_TYPE, MEDIA_TYPE)
                    .body(toJson(HeartbeatDto(Settings.uuid)))
                    .asJson()

            getNetworkResponse(response)
        } catch (e : UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Activates a game agent.
     *
     * @param gameAgentDto The gameAgent object being sent to the host.
     * @return NetworkResponse with a success or a failure with a message
     */
    suspend fun activateGameAgent(gameAgentDto: GameAgentDto): NetworkResponse {
        return try {
            val response = withContext(Dispatchers.Default) {
                Unirest.post(hostUrl(activateGameAgentPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(gameAgentDto))
                        .asJson()
            }

            getNetworkResponse(response)
        } catch (e: UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Sends his uuid to the host to say that he wants to disable the game agent
     *
     * @return NetworkResponse with a success or a failure with a message
     */
    suspend fun disableGameAgent(): NetworkResponse {
        return try {
            val response = withContext(Dispatchers.Default) {
                Unirest.post(hostUrl(disableGameAgentPath))
                        .header(CONTENT_TYPE, MEDIA_TYPE)
                        .body(toJson(DisableGameAgentDto(Settings.uuid)))
                        .asJson()
            }

            getNetworkResponse(response)
        } catch (e: UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }
    }

    /**
     * Calls the host to pause the game
     * @param paused pause or unpause game
     * @return NetworkResponse with a success or a failure with a message
     */
    fun pauseGame(paused: Boolean): NetworkResponse {
        return try {
            val response = Unirest.post(hostUrl(pauseGamePath))
                    .header(CONTENT_TYPE, MEDIA_TYPE)
                    .body(toJson(PauseGameDto(Settings.uuid, paused)))
                    .asJson()

            getNetworkResponse(response)
        } catch (exception: UnirestException) {
            getHostCannotBeReachedNetworkResponse()
        }
    }
}