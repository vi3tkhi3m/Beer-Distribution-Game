package com.asd2.beerdistributiongame.network.endpoint

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.activateGameAgentPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.checkAlivePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.chooseRolePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.connectPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.disableGameAgentPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.disconnectPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.placeOrderPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.sendHeartbeatPath
import com.asd2.beerdistributiongame.network.util.LOCALHOST
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.hostUrl
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.GameState
import io.mockk.*
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.test.context.junit4.SpringRunner


@RunWith(SpringRunner::class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class HostEndpointIntegrationTest {

    private val uuid = "123-123-123-123-123"
    private val username = "Jantje Janssen"
    private val ip = "123.123.12.12"
    private val player = Player(uuid, username, ip, true)
    private val order = Order("2", "2", 2)
    private val placeOrderDTO = PlacerOrderDto(order)
    private val heartbeatDTO = HeartbeatDto(uuid)

    @Autowired
    lateinit var testRestTemplate: TestRestTemplate

    init {
        unmockkAll()
    }

    @Before
    fun before() {
        NetworkContext.hostIp = LOCALHOST
        Context.isHost = true
        Context.players.clear()
        NetworkContext.lobbyPlayerHeartbeats.clear()
    }

    @After
    fun after() {
        Context.players.clear()
        Context.gameConfig = GameConfig()
    }

    @Test
    fun checkAliveSuccess() {
        Context.isHost = false
        val result = testRestTemplate.getForEntity(hostUrl(checkAlivePath), String::class.java)
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, result.statusCode)
    }

    @Test
    fun `checkAlive Success - Reached client is host`() {
        val result = testRestTemplate.getForEntity(hostUrl(checkAlivePath), String::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)
    }

    @Test
    fun `disconnect Success - Host updates player to not be connected`() {
        val insertedPlayer = Player(uuid, username, ip, true, Roles.FACTORY)
        Context.players.add(insertedPlayer)
        val result = testRestTemplate.postForEntity(hostUrl(disconnectPath), DisconnectDto(uuid), Unit::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertFalse(Context.players.single { it.uuid == uuid }.connected)
    }

    @Test
    fun `disconnect Success - Host removes player because current state is lobby`() {
        val insertedPlayer = Player(uuid, username, ip, true, Roles.FACTORY)
        Context.players.add(insertedPlayer)
        val result = testRestTemplate.postForEntity(hostUrl(disconnectPath), DisconnectDto(uuid), Unit::class.java)
        assertEquals(HttpStatus.OK, result.statusCode)
        assertFalse(Context.players[0].connected)
    }

    @Test
    fun `connect Success - Host returns game config`() {
        val backlogCost = 100
        Context.gameConfig.backlogCost = backlogCost
        val connectDTO = ConnectDto("a", "a")
        val result: ResponseEntity<ConnectResponseDto> = testRestTemplate.postForEntity(hostUrl(connectPath), connectDTO, ConnectResponseDto::class.java)
        assertNotNull(result.body)
        assertEquals(backlogCost, result.body?.gameConfig?.backlogCost)
    }

    @Test
    fun `connect Success - Host returns game config when reconnecting`() {
        val test = mockkClass(GameState::class)
        every { test.onEnter() } just runs
        every { test.onExit() } just runs
        Context.gameConfig.quantityOfFactories = 1
        Context.gameConfig.quantityOfRetailers = 0
        Context.gameConfig.quantityOfWarehouses = 0
        Context.gameConfig.quantityOfWholesales = 0
        val player = Player(uuid, username, ip, false, Roles.FACTORY, false, "", true, true)
        val stateOfGame = StateOfGame(1)
        val statesOfGame = arrayListOf(stateOfGame)
        Context.activeState = test
        Context.statesOfGame = statesOfGame
        Context.players.add(player)
        val connectDTO = ConnectDto(uuid, username)
        val result: ResponseEntity<ConnectResponseDto> = testRestTemplate.postForEntity(hostUrl(connectPath), connectDTO, ConnectResponseDto::class.java)
        assertTrue(result.body!!.playerIsReconnected)
    }

    @Test
    fun `connect Success - Host returns doesn't game config when reconnecting`() {
        val test = mockkClass(GameState::class)
        every { test.onEnter() } just runs
        every { test.onExit() } just runs
        Context.gameConfig.quantityOfFactories = 1
        Context.gameConfig.quantityOfRetailers = 0
        Context.gameConfig.quantityOfWarehouses = 0
        Context.gameConfig.quantityOfWholesales = 0
        val stateOfGame = StateOfGame(1)
        val statesOfGame = arrayListOf(stateOfGame)
        Context.activeState = test
        Context.statesOfGame = statesOfGame
        val connectDTO = ConnectDto(uuid, username)
        val result: ResponseEntity<ConnectResponseDto> = testRestTemplate.postForEntity(hostUrl(connectPath), connectDTO, ConnectResponseDto::class.java)
        assertFalse(result.body!!.playerIsReconnected)
    }

    @Test
    fun `connect Fail - Host returns forbidden because the game is full`() {
        val qtyFactories = Context.gameConfig.quantityOfFactories
        val qtyRetailers = Context.gameConfig.quantityOfRetailers
        val qtyWarehouses = Context.gameConfig.quantityOfWarehouses
        val qtyWholesales = Context.gameConfig.quantityOfWholesales
        Context.gameConfig.quantityOfFactories = 0
        Context.gameConfig.quantityOfRetailers = 0
        Context.gameConfig.quantityOfWarehouses = 0
        Context.gameConfig.quantityOfWholesales = 0
        val connectDTO = ConnectDto("a", "a")
        val result = testRestTemplate.postForEntity(hostUrl(connectPath), connectDTO, ConnectResponseDto::class.java)
        assertEquals(HttpStatus.FORBIDDEN, result.statusCode)
        Context.gameConfig.quantityOfFactories = qtyFactories
        Context.gameConfig.quantityOfRetailers = qtyRetailers
        Context.gameConfig.quantityOfWarehouses = qtyWarehouses
        Context.gameConfig.quantityOfWholesales = qtyWholesales
        Context.players.clear()
    }

    @Test
    fun `placeOrder Success - Reached client is host and order was placed`() {
        val order = Order("2", "2", 2)
        val result = testRestTemplate.postForEntity(hostUrl(placeOrderPath), placeOrderDTO, String::class.java)
        assertNotNull(result.statusCodeValue)
        assertEquals(HttpStatus.OK.value(), result.statusCodeValue)
    }

    @Test
    fun `placeOrder Failure - Reached client is not host and therefore order wasn't placed`() {
        Context.isHost = false
        val order = Order("2", "2", 2)
        val result = testRestTemplate.postForEntity(hostUrl(placeOrderPath), placeOrderDTO, String::class.java)
        assertNotNull(result.statusCodeValue)
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), result.statusCodeValue)
    }

    @Test
    fun `placeOrder Failure - Reached client who is the host but wrong object was sent`() {
        val user = ConnectDto("2", "2")
        val result = testRestTemplate.postForEntity(hostUrl(placeOrderPath), user, String::class.java)
        assertNotNull(result.statusCodeValue)
        assertEquals(HttpStatus.BAD_REQUEST.value(), result.statusCodeValue)
    }

    @Test
    fun `sendHeartbeat Success - Reached client is host and heartbeat was received`() {
        val result = testRestTemplate.postForEntity(hostUrl(sendHeartbeatPath), heartbeatDTO, String::class.java)

        assertEquals(HttpStatus.OK.value(), result.statusCodeValue)
    }

    @Test
    fun `sendHeartbeat Failure - Reached client isn't host and heartbeat wasn't received`() {
        Context.isHost = false

        val result = testRestTemplate.postForEntity(hostUrl(sendHeartbeatPath), heartbeatDTO, String::class.java)

        assertEquals(HttpStatus.SERVICE_UNAVAILABLE.value(), result.statusCodeValue)
    }

    @Test
    fun `enableGameAgent - success - calls the end point, if the player has his game agent enabled this function is successfull`() {
        val insertedPlayer = Player(uuid, username, "123.123.12.12", true, Roles.FACTORY)
        Context.players.add(insertedPlayer)
        testRestTemplate.postForEntity(hostUrl(activateGameAgentPath), GameAgentDto(insertedPlayer.uuid, "GameAgentString"), String::class.java)
        assertTrue(Context.players.single { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
    }

    @Test
    fun `disableGameAgent - success - calls the end point, if the player has his game agent disabled this function is successfull`() {
        val insertedPlayer = Player(uuid, username, "123.123.12.12", true, Roles.FACTORY)
        insertedPlayer.gameAgentEnabled = true
        Context.players.add(insertedPlayer)
        testRestTemplate.postForEntity(hostUrl(disableGameAgentPath), DisableGameAgentDto(insertedPlayer.uuid), String::class.java)
        assertFalse(Context.players.single { it.uuid == insertedPlayer.uuid }.gameAgentEnabled)
    }

    @Test
    fun `chooseRole Success - Changes the role of the player to factory`() {
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.FACTORY)
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(chooseRolePath), chooseRoleDTO, String::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(Context.players.single { it.uuid == player.uuid }.role, Roles.FACTORY)
    }

    @Test
    fun `chooseRole Success - Changes the role of the player to retailer`() {
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.RETAILER)
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(chooseRolePath), chooseRoleDTO, String::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(Context.players.single { it.uuid == player.uuid }.role, Roles.RETAILER)
    }

    @Test
    fun `chooseRole Success - Changes the role of the player to warehouse`() {
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.WAREHOUSE)
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(chooseRolePath), chooseRoleDTO, String::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(Context.players.single { it.uuid == player.uuid }.role, Roles.WAREHOUSE)
    }

    @Test
    fun `chooseRole Success - Changes the role of the player to wholesaler`() {
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.WHOLESALE)
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(chooseRolePath), chooseRoleDTO, String::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertEquals(Context.players.single { it.uuid == player.uuid }.role, Roles.WHOLESALE)
    }

    @Test
    fun `chooseRole Succes - Changes the role of the player to NONE`() {
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.NONE)
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(chooseRolePath), chooseRoleDTO, String::class.java)
        assertEquals(result.statusCode, HttpStatus.FORBIDDEN)
        assertEquals(Context.players.single { it.uuid == player.uuid }.role, Roles.NONE)
    }

    @Test
    fun `chooseRole Failure - Player does not exist in context`() {
        val chooseRoleDTO = ChooseRoleDto("other-uuid", Roles.FACTORY)
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(chooseRolePath), chooseRoleDTO, String::class.java)
        assertEquals(result.statusCode, HttpStatus.FORBIDDEN)
    }

    @Test
    fun `pauseGame Success - Pauses the game`() {
        val pauseGameDto = PauseGameDto(Settings.uuid, true)
        Context.players.add(player)
        Context.isHost = true
        val result = testRestTemplate.postForEntity(hostUrl(HostEndpoint.pauseGamePath), pauseGameDto, String::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertTrue(Context.isPaused)
    }

    @Test
    fun `pauseGame Success - Unpauses the game`() {
        val pauseGameDto = PauseGameDto(Settings.uuid, false)
        Context.players.add(player)
        Context.isHost = true
        val result = testRestTemplate.postForEntity(hostUrl(HostEndpoint.pauseGamePath), pauseGameDto, String::class.java)
        assertEquals(result.statusCode, HttpStatus.OK)
        assertFalse(Context.isPaused)
    }

    @Test
    fun `pauseGame Service unavailable - Can't unpause game when not host`() {
        val pauseGameDto = PauseGameDto(Settings.uuid, false)
        Context.isPaused = true
        Context.isHost = false
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(HostEndpoint.pauseGamePath), pauseGameDto, String::class.java)
        assertEquals(result.statusCode, HttpStatus.SERVICE_UNAVAILABLE)
    }

    @Test
    fun `pauseGame Forbidden - Can't unpause game when the request did not come from the game manager`() {
        val pauseGameDto = PauseGameDto(player.uuid, false)
        Context.isPaused = true
        Context.isHost = true
        Context.players.add(player)
        val result = testRestTemplate.postForEntity(hostUrl(HostEndpoint.pauseGamePath), pauseGameDto, String::class.java)
        assertEquals(result.statusCode, HttpStatus.FORBIDDEN)
    }
}