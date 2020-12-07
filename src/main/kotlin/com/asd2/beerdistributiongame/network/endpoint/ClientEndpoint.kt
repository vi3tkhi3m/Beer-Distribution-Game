package com.asd2.beerdistributiongame.network.endpoint

import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.service.ClientService
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
@RequestMapping("/client/")
class ClientEndpoint {

    companion object {
        const val onConnectPath       = "onconnect"
        const val onDisconnectPath    = "ondisconnect"
        const val onEndGamePath       = "onendgame"
        const val onChooseRolePath    = "onchooserole"
        const val onNextTurnPath      = "nextturn"
        const val onPausePath         = "onpausegame"
        const val hostMigratePath     = "hostmigrate"
        const val sendAllRolesPath    = "sendroles"
    }

    /**
     * The host will access this endpoint to update the client's state of game.
     *
     * @RequestBody The state of game in a NextTurnDto.
     * @return Will always return an OK
     */
    @PostMapping(onNextTurnPath, consumes = [MEDIA_TYPE])
    fun onNextTurn(@RequestBody nextTurnDto: NextTurnDto, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, onNextTurnPath, nextTurnDto)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.handleNextTurn(nextTurnDto)
        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * The host will access this endpoint to let the client know that a client has connected.
     *
     * @RequestBody player the player that is connected in a OnConnectDto.
     * @return Will always return an OK
     */
    @PostMapping(onConnectPath, consumes = [MEDIA_TYPE])
    fun onConnect(@RequestBody onConnectDto: OnConnectDto, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name,onConnectPath, onConnectDto)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.handleOnConnect(onConnectDto)
        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * The host will access this endpoint to let the client know that a client has disconnected.
     *
     * @RequestBody disconnectDto the user that is disconnected
     * @return Will always return an OK
     */
    @PostMapping(onDisconnectPath, consumes = [MEDIA_TYPE])
    fun onDisconnect(@RequestBody disconnectDto: DisconnectDto, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name,onDisconnectPath, disconnectDto)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.handleOnDisconnect(disconnectDto)
        return ResponseEntity(HttpStatus.OK)
    }

    /**
     * The host will access this endpoint to let the client know that the game had been ended.
     *
     * @return Will always return an OK
     */
    @RequestMapping(onEndGamePath)
    fun onEndGame(request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name,onEndGamePath)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.handleEndGame()
        return ResponseEntity.ok().build()
    }

    /**
     * The host will access this endpoint to sync the player role.
     *
     * @RequestBody The ChooseRoleDto.
     * @return Will always return an OK
     */
    @PostMapping(onChooseRolePath, consumes = [MEDIA_TYPE])
    fun onChooseRole(@RequestBody chooseRoleDto: ChooseRoleDto, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name,onChooseRolePath, chooseRoleDto)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.handleOnChooseRole(chooseRoleDto)
        return ResponseEntity.ok().build()
    }

    /**
     * The host will access this endpoint to sync the player role.
     *
     * @RequestBody The ChooseRoleDto.
     * @return Will always return an OK
     */
    @PostMapping(sendAllRolesPath, consumes = [MEDIA_TYPE])
    fun sendAllRoles(@RequestBody rolesInGame: RolesInGame, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, sendAllRolesPath, rolesInGame)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.onSendAllRoles(rolesInGame)
        return ResponseEntity.ok().build()
    }

    /**
     * The host will access this endpoint to pause or unpause the game
     *
     * @RequestBody The PauseGameDto.
     * @return Will always return an OK
     */
    @PostMapping(onPausePath, consumes = [MEDIA_TYPE])
    fun onPause(@RequestBody pauseGameDto: PauseGameDto, request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, onPausePath, pauseGameDto)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.handlePause(pauseGameDto.paused)
        return ResponseEntity.ok().build()
    }

    /**
     * The host will access this endpoint to let the client know that the game had been ended.
     *
     * @return Will always return an OK, except when the connecting host is not actually the host of the client.
     */
    @RequestMapping(hostMigratePath)
    fun hostMigrate(request: HttpServletRequest): ResponseEntity<Unit> {
        logEndpointCall(javaClass.name, hostMigratePath)
        if (!ClientService.checkIfIpIsHostIp(request.remoteAddr)) return ResponseEntity.status(HttpStatus.FORBIDDEN).build()
        ClientService.handleMigrateHost()
        return ResponseEntity.ok().build()
    }
}