package com.asd2.beerdistributiongame.network.endpoint

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.service.HostService
import com.asd2.beerdistributiongame.network.util.MEDIA_TYPE
import com.asd2.beerdistributiongame.network.util.logEndpointCall
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/host/")
class HostEndpoint {

    companion object {
        const val connectPath = "connect"
        const val disconnectPath = "disconnect"
        const val chooseRolePath = "chooserole"
        const val placeOrderPath = "placeorder"
        const val activateGameAgentPath = "activategameagent"
        const val disableGameAgentPath = "disablegameagent"
        const val endGamePath = "endgame"
        const val checkAlivePath = "checkalive"
        const val sendHeartbeatPath = "sendheartbeat"
        const val pauseGamePath = "pausegame"
    }

    /**
     * Will handle the connect of a client, only if Context.isHost is true
     *
     * @RequestBody connectDto the uuid and username of a user, in a data transfer object.
     * @return A response entity
     */
    @PostMapping(connectPath, consumes = [MEDIA_TYPE])
    fun connect(@RequestBody connectDto: ConnectDto, request: HttpServletRequest): ResponseEntity<ConnectResponseDto> {
        logEndpointCall(javaClass.name, connectPath, connectDto)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        val isConnected = HostService.handleConnect(connectDto, request.remoteAddr)
        if (isConnected) {
            val response = HostService.getConnectData()
            return ResponseEntity(response, HttpStatus.OK)
        }

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    /**
     * Will handle the disconnect of a client, only if Context.isHost is true
     *
     * @RequestBody disconnectDto the uuid of a disconnectDto, in a data transfer object.
     * @return A response entity
     */
    @PostMapping(disconnectPath, consumes = [MEDIA_TYPE])
    fun disconnect(@RequestBody disconnectDto: DisconnectDto, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, disconnectPath, disconnectDto)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()
        HostService.handleDisconnect(disconnectDto)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    /**
     * Will handle the chooseRole of a client, only if Context.isHost is true
     *
     * @RequestBody The chooseRoleDto (the client's chosen role).
     * @return a status code of 200 (OK) when role is successfully changed.
     * If user is not found it will return a 403
     */
    @PostMapping(chooseRolePath, consumes = [MEDIA_TYPE])
    fun chooseRole(@RequestBody chooseRoleDto: ChooseRoleDto): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, chooseRolePath, chooseRoleDto)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        if (HostService.handleChooseRole(chooseRoleDto)) return ResponseEntity.ok().build()

        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    /**
     * Will handle the order of a client, only if Context.isHost is true
     *
     * @RequestBody The PlacerOrderDto.
     * @return A response entity
     */
    @PostMapping(placeOrderPath, consumes = [MEDIA_TYPE])
    fun placeOrder(@RequestBody placerOrderDto: PlacerOrderDto): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, placeOrderPath, placerOrderDto)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        HostService.handlePlaceOrder(placerOrderDto)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    /**
     * Checks if the host is still alive.
     *
     * @return a statuscode of 200 (OK) when asked, which can be used to determine that the host can still be connected to.
     * If reached client is not the host, it will return a service unavailable.
     */
    @RequestMapping(checkAlivePath)
    fun checkAlive(): ResponseEntity<Unit> {
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        return ResponseEntity.status(HttpStatus.OK).build()
    }

    /**
     * Will handle the end game request, only if Context.isHost is true
     *
     * @return A response entity
     */
    @RequestMapping(endGamePath)
    fun endGame(): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, endGamePath)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        HostService.handleEndGame()
        return ResponseEntity.ok().build()
    }

    /**
     * Will handle the activate game agent, only if Context.isHost is true
     *
     * @RequestBody GameAgentDto the uuid and gameagent of a user, in a data transfer object.
     * @return A response entity
     */
    @PostMapping(activateGameAgentPath, consumes = [MEDIA_TYPE])
    fun activateGameAgent(@RequestBody gameAgentDto: GameAgentDto): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, activateGameAgentPath, gameAgentDto)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        HostService.handleActivateGameAgent(gameAgentDto)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    /**
     * The endpoint for disabling a game agent for a user.
     *
     * @RequestBody DisableGameAgentDto this object contains the UUID of a user.
     * @return A response entity
     */
    @PostMapping(disableGameAgentPath, consumes = [MEDIA_TYPE])
    fun disableGameAgent(@RequestBody disableGameAgentDto: DisableGameAgentDto, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, disableGameAgentPath, disableGameAgentDto)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        HostService.handleDisableGameAgent(disableGameAgentDto)
        return ResponseEntity.status(HttpStatus.OK).build()
    }

    /**
     * Will handle the pause game request, only if Context.isHost is true
     *
     * @RequestBody The PauseGameDto.
     * @return A response entity
     */
    @PostMapping(pauseGamePath, consumes = [MEDIA_TYPE])
    fun pauseGame(@RequestBody pauseGameDto: PauseGameDto): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, pauseGamePath, pauseGameDto)
        if (!Context.isHost) return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        if (HostService.handlePauseGame(pauseGameDto)) return ResponseEntity.ok().build()
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
    }

    /**
     * Handles receiving a heartbeat from a connected client.
     *
     * @RequestBody The HeartbeatDto.
     * @returns a statuscode of 200 (OK) when receiving a heartbeat
     */
    @PostMapping(sendHeartbeatPath, consumes = [MEDIA_TYPE])
    fun sendHeartbeat(@RequestBody heartbeatDto: HeartbeatDto, request: HttpServletRequest): ResponseEntity<Unit> {
        if (!Context.isHost)
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build()

        HostService.handleHeartbeat(heartbeatDto)
        return ResponseEntity.ok().build()
    }
}