package com.asd2.beerdistributiongame.network

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.util.LOCALHOST
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.settings.Settings
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import io.mockk.unmockkObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ClientIntegrationTest {

    private val username = "Jantje Janssen"
    private val uuid = "123-123-123-123-125"
    private val insertedPlayer = Player(uuid, username, "123.123.12.12", true, Roles.FACTORY)

    @Before
    fun before() {
        mockkObject(Settings)
        every { Settings.uuid } returns uuid
        Context.isHost = true
        NetworkContext.hostIp = "127.0.0.1"
    }

    @After
    fun after() {
        Context.players.clear()
        unmockkAll()
    }

    @Test
    fun `onChooseRole Success - Reached client and role is applied`() = runBlocking {
        val uuid = Settings.uuid
        val username = "Jantje Janssen"
        val insertedPlayer = Player(uuid, username, "1.1.1.1", true, Roles.FACTORY)
        Context.players.add(insertedPlayer)
        val result = withContext(Dispatchers.Default) { Client.chooseRole(Roles.WHOLESALE) }
        assertTrue(result.success)
    }

    @Test
    fun `checkAlive Success - Reached client is host`() = runBlocking {
        val result = withContext(Dispatchers.Default) { Client.checkAlive() }
        assertTrue(result)
    }

    @Test
    fun `check if Connect works`() = runBlocking {
        Client.connect(NetworkContext.hostIp)
        Assert.assertFalse(Context.players.isEmpty())
    }

    @Test
    fun `check if Disconnect works`() = runBlocking {
        unmockkObject(Settings)
        val uuid = Settings.uuid
        val insertedPlayer = Player(uuid, username, "1.1.1.1", true, Roles.FACTORY)
        Context.players.add(insertedPlayer)
        Client.disconnect()
        Thread.sleep(1000)
        assertFalse(Context.players[0].connected)
    }

    @Test
    fun `placeOrder Success - Reached client is host and order was placed`() = runBlocking {
        val order = Order("origin", "destination", 2)
        val placeOrderDTO = PlacerOrderDto(order)
        val result = Client.placeOrder(placeOrderDTO.order)
        assertTrue(result.success)
    }

    @Test
    fun `activateGameAgent Success - tries the full implementation`() = runBlocking {
        val formula = "gameformula"
        insertedPlayer.gameAgentEnabled = false
        Context.players.add(insertedPlayer)
        val result = Client.activateGameAgent(formula)
        Assert.assertTrue(result.success)
        Assert.assertTrue(Context.players.first { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
        Assert.assertTrue(Context.players.first { it.uuid == insertedPlayer.uuid }.gameAgent == formula)
    }

    @Test
    fun `deactivateGameAgent Success - tries the full implementation`() = runBlocking {
        insertedPlayer.gameAgentEnabled = true
        Context.players.add(insertedPlayer)
        Client.deactivateGameAgent()
        Assert.assertFalse(Context.players.first { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
    }

    @Test
    fun `deactivateGameAgent Success - tries the full implementation for activate and deactivate`() = runBlocking {
        Context.players.add(insertedPlayer)
        Client.activateGameAgent("gameformula")
        Assert.assertTrue(Context.players.first { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
        Client.deactivateGameAgent()
        Assert.assertFalse(Context.players.first { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
    }

    @Test
    fun `sendHeartbeat Success - Reached client is host and heartbeat was received`() = runBlocking {
        NetworkContext.hostIp = LOCALHOST

        val result = Client.sendHeartbeat()

        assertTrue(result)
        NetworkContext.lobbyPlayerHeartbeats.clear()
    }

    @Test
    fun `pauseGame Success - Called pause game and context isPaused is true`() {
        Client.pauseGame(true)

        assertTrue(Context.isPaused)
    }

    @Test
    fun `pauseGame Success - Called pause game and context isPaused is false`() {
        Client.pauseGame(false)

        assertFalse(Context.isPaused)
    }
}