package com.asd2.beerdistributiongame.network.broker

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.util.LOCALHOST
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.NetworkContext.hostIp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ClientBrokerIntegrationTest {

    private val order = Order("origin", "destination", 2)

    @Before
    fun before() {
        NetworkContext.hostIp = "127.0.0.1"
        hostIp = LOCALHOST
        Context.players.clear()
        NetworkContext.lobbyPlayerHeartbeats.clear()
        Context.isHost = true
    }

    @Test
    fun `checkAlive Success - Host can be reached and is host`() = runBlocking {
        val result = withContext(Dispatchers.Default) { ClientBroker.checkAlive() }
        assertTrue(result)
    }

    @Test
    fun `placeOrder Success - Reached client is host and order was placed`() = runBlocking {
        val result = ClientBroker.placeOrder(order)
        assertTrue(result.success)
    }

    @Test
    fun `placeOrder Failure - Reached client is not host and therefore order wasn't placed`() = runBlocking {
        Context.isHost = false
        val result = ClientBroker.placeOrder(order)
        assertFalse(result.success)
    }

    @Test
    fun `connect Success - Connects succesfully`() = runBlocking {
        val result = ClientBroker.connect(hostIp)
        assertTrue(result.success)
    }

    @Test
    fun `sendHeartbeat Success - Reached client is host and heartbeat was received`() = runBlocking {
        val result = ClientBroker.sendHeartbeat()

        assertTrue(result.success)
    }

    @Test
    fun `sendHeartbeat Failure - Reached client isn't host and heartbeat wasn't received`() = runBlocking {
        Context.isHost = false

        val result = ClientBroker.sendHeartbeat()

        assertFalse(result.success)
    }
}