package com.asd2.beerdistributiongame.network.service

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.broker.HostBroker
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.settings.Settings
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.collection.IsMapContaining
import org.junit.After
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull

class HostServiceUnitTest {

    private val user = ConnectDto("uuid", "username")
    private val address = "123.123.123.123"
    private val insertedPlayer = Player(user.uuid, user.username, address, true, Roles.FACTORY)
    private val uuid = "123-123-123-123-123"

    @Before
    fun before() {
        mockkObject(Host)
        mockkObject(Client)
        mockkObject(HostBroker)
        NetworkContext.lobbyPlayerHeartbeats.clear()
        NetworkContext.hostIp = "127.0.0.1"
    }

    @After
    fun after() {
        Context.players.clear()
        unmockkAll()
    }

    @Test
    fun `handleConnect Failed - There are no spots left`() {
        Context.gameConfig.quantityOfFactories = 0
        Context.gameConfig.quantityOfRetailers = 0
        Context.gameConfig.quantityOfWarehouses = 0
        Context.gameConfig.quantityOfWholesales = 0
        val result = HostService.handleConnect(user, address)
        assertFalse(result)
    }

    @Test
    fun `handleConnect Success - The new player has been added`() {
        Context.gameConfig.quantityOfFactories = 1
        Context.gameConfig.quantityOfRetailers = 1
        Context.gameConfig.quantityOfWarehouses = 1
        Context.gameConfig.quantityOfWholesales = 1
        val result = HostService.handleConnect(user, address)
        assertTrue(result)
    }

    @Test
    fun `handleConnect Success - The player reconnects`() {
        Context.players.add(insertedPlayer)
        val result = HostService.handleConnect(user, address)
        assertTrue(result)
    }

    @Test
    fun `handleActivateGameAgent - succes - Adds a game agent to the context and calls the function, if the game agent is enabled for the user it returns success`() {
        insertedPlayer.gameAgentEnabled = false
        Context.players.add(insertedPlayer)
        HostService.handleActivateGameAgent(GameAgentDto(insertedPlayer.uuid, "GameAgentString"))
        assertTrue(Context.players.single { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
    }

    @Test
    fun `handleDeactivateGameAgent - succes - Adds a game agent to the context and calls the function, if the game agent is disabled for the user it returns success`() {
        insertedPlayer.gameAgentEnabled = true
        Context.players.add(insertedPlayer)
        HostService.handleDisableGameAgent(DisableGameAgentDto(insertedPlayer.uuid))
        assertFalse(Context.players.single { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
    }

    @Test
    fun `playPauseGame pause game - succes - Can play and pause the game`() {
        Context.players.add(insertedPlayer)
        val paused = true

        HostService.handlePauseGame(PauseGameDto(Settings.uuid, paused))

        assertTrue(Context.isPaused)
    }

    @Test
    fun `playPauseGame resume game - succes - Can play and pause the game`() {
        Context.players.add(insertedPlayer)
        val paused = false

        HostService.handlePauseGame(PauseGameDto(Settings.uuid, paused))

        assertFalse(Context.isPaused)
    }

    @Test
    fun `handleHeartbeat Success - Received heartbeat is inserted into HashMap`() {
        assertThat(NetworkContext.lobbyPlayerHeartbeats.size, `is`(0))

        HostService.handleHeartbeat(HeartbeatDto(uuid))

        assertThat(NetworkContext.lobbyPlayerHeartbeats.size, `is`(1))
        assertThat(NetworkContext.lobbyPlayerHeartbeats, IsMapContaining.hasKey(uuid))
        assertNotNull(NetworkContext.lobbyPlayerHeartbeats[uuid])
    }

    @Test
    fun `handleHeartbeat Success - Received heartbeat replaces existing playerHeartbeat in HashMap`() {
        NetworkContext.lobbyPlayerHeartbeats[uuid] = 200
        assertThat(NetworkContext.lobbyPlayerHeartbeats, IsMapContaining.hasKey(uuid))

        HostService.handleHeartbeat(HeartbeatDto(uuid))

        assertNotEquals(NetworkContext.lobbyPlayerHeartbeats[uuid], 200)
    }
}