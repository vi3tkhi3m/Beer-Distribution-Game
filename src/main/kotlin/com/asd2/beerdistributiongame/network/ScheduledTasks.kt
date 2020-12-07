package com.asd2.beerdistributiongame.network

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.network.service.HostService
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.getCurrentMilliseconds
import com.asd2.beerdistributiongame.state.LobbyState
import javafx.application.Platform
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

const val HEARTBEAT_DELAY: Long = 1000 //Time in milliseconds of how far apart each client heartbeat is that is sent to the host.
const val CHECK_HEARTBEAT_DELAY: Long = 2000 //Time in milliseconds of how far apart each check of heartbeats takes place. Should at least be more than half of heatbeatDelay, to prevent choosing disconnect too early.
const val HEARTBEAT_PERIOD: Long = 3000 //Time in milliseconds of how long a client is allowed to not send a heartbeat where they will still be considered connected.

@Component
object ScheduledTasks {

    /**
     * When client is in lobby state and isn't host, it periodically sends a heartbeat to the host to confirm it is still connected.
     * If it fails to reach the host it will try again. When that fails again the player will disconnect.
     * The use of Platform.runLater is necessary to prevent issues using threads.
     */
    @Scheduled(fixedRate = HEARTBEAT_DELAY)
    fun sendHeartbeat() {
        if (Context.activeState is LobbyState && Context.isConnected) {
            if (!Client.sendHeartbeat()) {
                if (!Client.sendHeartbeat()) {
                    Context.isConnected = false
                    Platform.runLater {
                        Client.onLostConnection()
                    }
                }
            }
        }
    }

    /**
     * Checks heartbeats as the host. If a client hasn't sent a heartbeat in the last HEARTBEAT_PERIOD (in seconds), the host decides the client
     * is no longer connected and removes it from the lobby.
     */
    @Scheduled(fixedRate = CHECK_HEARTBEAT_DELAY)
    fun checkHeartbeats() {
        if (Context.activeState is LobbyState && Context.isHost) {
            val it = NetworkContext.lobbyPlayerHeartbeats.entries.iterator()
            while (it.hasNext()) {
                val entry = it.next()
                if (getCurrentMilliseconds() - entry.value >= HEARTBEAT_PERIOD) removeDisconnectedPlayer(it, entry)
            }
        }
    }

    private fun removeDisconnectedPlayer(it: MutableIterator<MutableMap.MutableEntry<String, Long>>, entry: MutableMap.MutableEntry<String, Long>) {
        var disconnectDTO = DisconnectDto(entry.key)
        val disconnectedPlayer = Context.players.singleOrNull { it.uuid == disconnectDTO.uuid }
        if (disconnectedPlayer != null) {
            it.remove()
            HostService.handleDisconnect(disconnectDTO)
            Host.onHostRemovesPlayerFromLobby(disconnectedPlayer)
        }
    }
}