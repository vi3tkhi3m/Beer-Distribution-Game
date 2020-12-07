package com.asd2.beerdistributiongame.network.endpoint

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.network.ChooseRoleDto
import com.asd2.beerdistributiongame.network.DisconnectDto
import com.asd2.beerdistributiongame.network.OnConnectDto
import com.asd2.beerdistributiongame.network.PauseGameDto
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onChooseRolePath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onConnectPath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onDisconnectPath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onPausePath
import com.asd2.beerdistributiongame.network.util.LOCALHOST
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.clientUrl
import com.asd2.beerdistributiongame.state.LobbyState
import com.asd2.beerdistributiongame.state.MainMenuState
import io.mockk.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
internal class ClientEndpointIntegrationTest {

    private val player = Player("1", "username", "0.0.0.0", false)
    private val onConnectDTO = OnConnectDto(player)
    private val disconnectDTO = DisconnectDto(player.uuid)

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    init {
        unmockkAll()
    }

    @Before
    fun before() {
        NetworkContext.hostIp = "127.0.0.1"
        Context.players.clear()
        Context.gameConfig= GameConfig()
    }

    @Test
    fun `onConnect Success - Endpoint is reached, onConnect has been handled and Ok status is returned`() {
        val response = testRestTemplate.postForEntity(clientUrl(LOCALHOST, onConnectPath), onConnectDTO, String::class.java)
        assertNotNull(Context.players.single { it.uuid == player.uuid })
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `onDisconnect Success - Endpoint is reached, onDisconnect has been handled when not in lobby state and Ok status is returned`() {
        val mainMenuStateMock = mockkClass(MainMenuState::class)
        every { mainMenuStateMock.onEnter() } just Runs
        every { mainMenuStateMock.onExit() } just Runs
        Context.activeState = mainMenuStateMock
        testRestTemplate.postForEntity(clientUrl(LOCALHOST, onConnectPath), onConnectDTO, String::class.java)
        assertNotNull(Context.players.single { it.uuid == player.uuid })
        val response = testRestTemplate.postForEntity(clientUrl(LOCALHOST, onDisconnectPath), disconnectDTO, String::class.java)
        assertFalse(Context.players.single { it.uuid == player.uuid }.connected)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `onDisconnect Success - Endpoint is reached, onDisconnect has been handled when in lobby state and Ok status is returned`() {
        val lobbyStateMock = mockkClass(LobbyState::class)
        every { lobbyStateMock.onEnter() } just Runs
        every { lobbyStateMock.onExit() } just Runs
        Context.activeState = lobbyStateMock
        testRestTemplate.postForEntity(clientUrl(LOCALHOST, onConnectPath), onConnectDTO, String::class.java)
        assertNotNull(Context.players.single { it.uuid == player.uuid })
        val response = testRestTemplate.postForEntity(clientUrl(LOCALHOST, onDisconnectPath), disconnectDTO, String::class.java)
        assertTrue(Context.players.isEmpty())
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `endGame Success - Endpoint is reached and Ok status is returned`() {
        val response = testRestTemplate.getForEntity(clientUrl(LOCALHOST, ClientEndpoint.onEndGamePath), String::class.java)
        assertFalse(Context.isConnected)
        assertEquals(HttpStatus.OK, response.statusCode)
    }

    @Test
    fun `onChooseRole Success - Endpoint is reached and Ok status is returned`() {
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.FACTORY)
        Context.players.add(player)
        val response = testRestTemplate.postForEntity(clientUrl(LOCALHOST, onChooseRolePath), chooseRoleDTO, String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode)
        assertEquals(Context.players.single { it.uuid == player.uuid }.role, Roles.FACTORY)
    }

    @Test
    fun `onPlayPause Success - Endpoint is reached and Ok status is returned and paused is set to true in context`() {
        val pause = PauseGameDto("", true)
        Context.players.add(player)

        val response = testRestTemplate.postForEntity(clientUrl(LOCALHOST, onPausePath), pause, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertTrue(Context.isPaused)
    }

    @Test
    fun `onPlayPause Success - Endpoint is reached and Ok status is returned and paused is set to false in context`() {
        val pause = PauseGameDto("", false)
        Context.players.add(player)

        val response = testRestTemplate.postForEntity(clientUrl(LOCALHOST, onPausePath), pause, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode)
        assertFalse(Context.isPaused)
    }
}