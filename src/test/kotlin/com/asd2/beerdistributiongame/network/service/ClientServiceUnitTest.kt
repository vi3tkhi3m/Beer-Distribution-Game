package com.asd2.beerdistributiongame.network.service

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.RolesInGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.network.*
import com.asd2.beerdistributiongame.network.broker.ClientBroker
import com.asd2.beerdistributiongame.network.util.LOCALHOST
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.state.MainMenuState
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class ClientServiceUnitTest {

    private var player = Player("uuid", "Username", "123.123.12.12", true)
    var stateOfGame=StateOfGame(1)
    var statesOfGame= arrayListOf(stateOfGame)
    private var connectResponseDTO = ConnectResponseDto(GameConfig(), listOf(player), statesOfGame,true,RolesInGame(),"3")
    private var onConnectDto: OnConnectDto = OnConnectDto(player)

    init {
        Context.players.clear()
    }

    @Before
    fun before() {
        mockkObject(ClientBroker)
        mockkObject(Client)
        NetworkContext.hostIp = "127.0.0.1"
    }

    @After
    fun after() {
        unmockkAll()
        Context.players.clear()
    }

    @Test
    fun `connect Success - Connected and data has been changed`() = runBlocking {
        connectResponseDTO.gameConfig.isFinished = true
        Context.gameConfig.isFinished = false
        Context.isConnected = false
        coEvery { ClientBroker.connect(any()) } returns NetworkDataResponse(true, "", connectResponseDTO)
        ClientService.connect("")
        assertTrue(Context.isConnected)
        assertTrue(Context.gameConfig.isFinished)
        assertTrue(Context.players.isNotEmpty())
    }

    @Test
    fun `connect Failure - Not connected and no data has been changed`() = runBlocking {
        connectResponseDTO.gameConfig.isFinished = true
        Context.gameConfig.isFinished = false
        Context.isConnected = false
        coEvery { ClientBroker.connect(any()) } returns NetworkDataResponse(false)
        ClientService.connect("")
        assertFalse(Context.isConnected)
        assertFalse(Context.gameConfig.isFinished)
        assertTrue(Context.players.isEmpty())
    }

    @Test
    fun `disconnect Success - The client has been disconnected`() {
        Context.isConnected = true
        ClientService.disconnect()
        assertFalse(Context.isConnected)
    }

    @Test
    fun `handleOnConnect - Player has been added`() {
        ClientService.handleOnConnect(onConnectDto)
        val insertedPlayer = Context.players.last()
        assertEquals(player.connected, insertedPlayer.connected)
        assertEquals(player.ip, insertedPlayer.ip)
        assertEquals(player.isGameManager, insertedPlayer.isGameManager)
        assertEquals(player.role, insertedPlayer.role)
        assertEquals(player.username, insertedPlayer.username)
        assertEquals(player.uuid, insertedPlayer.uuid)
    }
    @Test
    fun `handleOnConnect - Player reconnects`() {
        Context.players.add(player)
        ClientService.handleOnConnect(onConnectDto)
        val insertedPlayer = Context.players.single { it.uuid == player.uuid }
        assertEquals(player.connected, insertedPlayer.connected)
        assertEquals(player.ip, insertedPlayer.ip)
        assertEquals(player.isGameManager, insertedPlayer.isGameManager)
        assertEquals(player.role, insertedPlayer.role)
        assertEquals(player.username, insertedPlayer.username)
        assertEquals(player.uuid, insertedPlayer.uuid)
    }

    @Test
    fun `handleOnDisconnect - Player has been disconnected`() {
        Context.players.add(player)
        val mainMenuStateMock = mockkClass(MainMenuState::class)
        every { mainMenuStateMock.onExit() } just Runs
        every { mainMenuStateMock.onEnter() } just Runs
        Context.activeState = mainMenuStateMock
        ClientService.handleOnDisconnect(DisconnectDto(player.uuid))
        assertFalse(Context.players.last().connected)
    }

    @Test
    fun `endGame - Disconnected when host`() {
        Context.isHost = true
        Context.isConnected = true
        ClientService.endGame()
        assertFalse(Context.isConnected)
    }

    @Test
    fun `endGame - Disconnected when not host`() {
        Context.isHost = false
        Context.isConnected = true
        ClientService.endGame()
        assertFalse(Context.isConnected)
    }

    @Test
    fun `handleEndGame - Client is disconnected`() {
        Context.isConnected = true
        ClientService.handleEndGame()
        assertFalse(Context.isConnected)
    }

    @Test
    fun `checkIfIpIsHostIp - Returns true`() {
        val isHostIp = ClientService.checkIfIpIsHostIp(LOCALHOST)
        assertTrue(isHostIp)
    }

    @Test
    fun `checkIfIpIsHostIp - Returns false`() {
        val isHostIp = ClientService.checkIfIpIsHostIp("notlocalhost")
        assertFalse(isHostIp)
    }
    @Test
    fun `pauseGame - Pauses game`() {
        ClientService.pauseGame(true)
        assertTrue(Context.isPaused)
    }

    @Test
    fun `pauseGame - Unpauses game`() {
        ClientService.pauseGame(false)
        assertFalse(Context.isPaused)
    }
}