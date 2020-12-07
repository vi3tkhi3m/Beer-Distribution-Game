package com.asd2.beerdistributiongame.network.broker


import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.network.ChooseRoleDto
import com.asd2.beerdistributiongame.network.DisconnectDto
import com.asd2.beerdistributiongame.network.OnConnectDto
import com.asd2.beerdistributiongame.network.util.LOCALHOST
import com.asd2.beerdistributiongame.network.util.NetworkContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.junit.After
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HostBrokerIntegrationTest {

    private val player = Player("111", "username", "0.0.0.0", false)

    @After
    fun after() {
        NetworkContext.hostIp = "127.0.0.1"
        Context.players.clear()
    }


    @Test
    fun `endGame Success - Ok response is received`() {
        val response = HostBroker.onEndGame(LOCALHOST)
        assertTrue(response.success)
    }

    @Test
    fun `onChooseRole Success - Ok response is received`() {
        Context.players.add(player)
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.FACTORY)
        val response = HostBroker.onChooseRole(LOCALHOST, chooseRoleDTO)
        assertTrue(response.success)
    }

    @Test
    fun `onDisconnect Success - Ok response is received`() = runBlocking {
        Context.players.add(player)
        val disconnectDto = DisconnectDto(player.uuid)
        val response = withContext(Dispatchers.Default) { HostBroker.onDisconnect(LOCALHOST, disconnectDto) }
        assertTrue(response.success)
    }

    @Test
    fun `onConnect Success - Ok response is received`() = runBlocking{
        val onConnectDto = OnConnectDto(player)
        val response = withContext(Dispatchers.Default) { HostBroker.onConnect(LOCALHOST, onConnectDto) }
        assertTrue(response.success)
    }
}