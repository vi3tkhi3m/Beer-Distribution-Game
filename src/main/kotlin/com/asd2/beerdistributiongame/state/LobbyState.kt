package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.frontend.LobbyHandler
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.network.ChooseRoleEvent
import com.asd2.beerdistributiongame.network.Client
import com.asd2.beerdistributiongame.network.Host
import com.asd2.beerdistributiongame.network.NextTurnDto
import com.asd2.beerdistributiongame.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.NetworkInterface
import java.net.SocketException

class LobbyState : State() {

    var handler = LobbyHandler()

    override fun onEnter() {
        Context.loadScene(handler.getViewFile())
        Client.onConnect = ::onConnect
        Client.onDisconnect = ::onDisconnect
        Client.onEndGame = ::onEndGame

        if (!Context.isHost) {
            Client.onNextTurn = ::onNextTurn
        }

        Client.onLostConnection = ::onLostConnection
        Client.onChooseRole = ::onChooseRole
    }

    override fun onExit() {
        Client.onConnect = {}
        Client.onDisconnect = {}
        Client.onEndGame = {}
        Client.onChooseRole = {}
        Host.onConnect = {}

        if (!Context.isHost) {
            Client.onNextTurn = {}
        }
    }

    fun hostStartGame() {
        setState(GameState())
    }

	private fun notifyToStartGame(nextTurnDto: NextTurnDto) = GlobalScope.launch {
        withContext(Dispatchers.Main) {
            Context.beerDistributionGame = nextTurnDto.beerDistributionGame
            Context.statesOfGame.add(nextTurnDto.stateOfGame)
            setState(GameState())
        }
    }

    fun notifyToGoBackToMainMenu() {
        if (Context.isConnected) {
            if (Context.isGameManager) {
                Client.endGame()
            } else
                Client.disconnect()
        }
        Context.isGameManager = false
        Context.isHost = false
        Context.players.clear()
        setState(MainMenuState())
    }

    fun notifyToChooseRole(newRole: Roles) = handler.process {
        val player = Context.players.single { it.uuid == Settings.uuid }
        val oldRole = player.role
        val response = withContext(Dispatchers.Default) { Client.chooseRole(newRole) }
        if (response.success)
            handler.onChooseRole(ChooseRoleEvent(player, oldRole, newRole, Context.rolesInGame))
        else
            handler.showAlert(response.message)
    }

    private fun onConnect(newPlayer: Player) {
        handler.addPlayer(newPlayer)
    }

    private fun onDisconnect(disconnectedPlayer: Player?) {
        handler.removePlayer(disconnectedPlayer!!)
    }

    private fun onEndGame() {
        handler.onEndGame()
    }

    private fun onNextTurn(nextTurnDto: NextTurnDto) {
        notifyToStartGame(nextTurnDto)
    }

    private fun onLostConnection() {
        handler.onLostConnection()
        handler.showAlert("Lost connection to host!")
    }

    private fun onChooseRole(roleEvent: ChooseRoleEvent) {
        Context.rolesInGame = roleEvent.rolesInGame
        handler.updatePlayer(roleEvent.player.uuid, roleEvent.oldRole, roleEvent.newRole)
    }

    @Throws(SocketException::class)
    fun getCorrectIpAddress() : String? {
        try {
            // gets all network interfaces
            val interfaces = NetworkInterface.getNetworkInterfaces()
            // for all network interfaces it does a check
            while (interfaces.hasMoreElements()) {
                // get's a certain network interface
                val iface = interfaces.nextElement()
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback || !iface.isUp) continue
                val addresses = iface.inetAddresses
                while (addresses.hasMoreElements()) {
                    val address = addresses.nextElement()
                    // if the network interface contain's eth, it means it's the local ip address connected to a switch, that is the correct ip
                    if (iface.name.contains("eth")) {
                        return address.hostAddress
                    }
                }
            }
        } catch (e: SocketException) {
            throw RuntimeException(e)
        }
        return "IP could not be found."
    }
}