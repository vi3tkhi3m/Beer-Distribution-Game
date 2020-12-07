package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.GameConfigHandler
import com.asd2.beerdistributiongame.gamelogic.configuration.RoleOwner
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.settings.Settings
import java.util.*

class GameConfigState : State() {

    private val handler = GameConfigHandler()

    override fun onEnter() {
        Context.loadScene(handler.getViewFile())
    }

    override fun onExit() = Unit

    fun notifyToGoBackToMainMenu() {
        setState(MainMenuState())
    }

    fun notifyToOpenLobby() {
        Context.isGameManager = true
        Context.isHost = true
        Context.gameConfig.hostName = Settings.username
        createPlayerRoles()
        setState(LobbyState())
    }

    fun createPlayerRoles() {
        for (i in 1 .. Context.gameConfig.quantityOfFactories) Context.rolesInGame.playerRoles[UUID.randomUUID().toString()] = RoleOwner(Roles.FACTORY, null)
        for (i in 1 .. Context.gameConfig.quantityOfWarehouses) Context.rolesInGame.playerRoles[UUID.randomUUID().toString()] = RoleOwner(Roles.WAREHOUSE, null)
        for (i in 1 .. Context.gameConfig.quantityOfWholesales) Context.rolesInGame.playerRoles[UUID.randomUUID().toString()] = RoleOwner(Roles.WHOLESALE, null)
        for (i in 1 .. Context.gameConfig.quantityOfRetailers) Context.rolesInGame.playerRoles[UUID.randomUUID().toString()] = RoleOwner(Roles.RETAILER, null)
    }
}