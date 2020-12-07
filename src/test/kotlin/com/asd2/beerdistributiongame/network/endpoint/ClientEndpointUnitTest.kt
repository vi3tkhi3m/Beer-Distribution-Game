package com.asd2.beerdistributiongame.network.endpoint

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.network.ChooseRoleDto
import com.asd2.beerdistributiongame.network.OnConnectDto
import com.asd2.beerdistributiongame.network.PauseGameDto
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onChooseRolePath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onConnectPath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onDisconnectPath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onEndGamePath
import com.asd2.beerdistributiongame.network.endpoint.ClientEndpoint.Companion.onPausePath
import com.asd2.beerdistributiongame.network.service.ClientService
import com.asd2.beerdistributiongame.network.util.LOCALHOST
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.clientUrl
import com.asd2.beerdistributiongame.network.util.toJson
import io.mockk.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(SpringExtension::class)
@WebMvcTest(ClientEndpoint::class)
@RunWith(SpringRunner::class)
internal class ClientEndpointUnitTest {

    private val player = Player("1", "username", "0.0.0.0", false)
    private val onConnectDTO = OnConnectDto(player)
    private lateinit var mockMvc: MockMvc

    @InjectMocks
    lateinit var controller: ClientEndpoint

    @Before
    fun before() {
        Context.rolesInGame.playerRoles.clear()
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setMessageConverters(MappingJackson2HttpMessageConverter()).build()
        mockkObject(ClientService)
        every { ClientService.handleOnDisconnect(any()) } just runs
        NetworkContext.hostIp = "127.0.0.1"
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun `onConnect Success - Response with OK status is returned`() {
        every { ClientService.handleOnConnect(any()) } just runs
        mockMvc.perform(post(clientUrl(LOCALHOST, onConnectPath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(onConnectDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `onDisconnect Success - Response with OK status is returned`() {
        every { ClientService.handleOnDisconnect(any()) } just runs
        mockMvc.perform(post(clientUrl(LOCALHOST, onDisconnectPath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(player))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `endGame Success - Response with OK status is returned`() {
        every { ClientService.endGame() } just runs
        mockMvc.perform(get(clientUrl(LOCALHOST, onEndGamePath))
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun `onChooseRole Success -  Returns Ok`() {
        val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.FACTORY)
        Context.players.clear()
        Context.players.add(player)
        every { ClientService.handleOnChooseRole(chooseRoleDTO) } just runs
        mockMvc.perform(post(clientUrl(LOCALHOST, onChooseRolePath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(chooseRoleDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Context.players.clear()
    }
    @Test
    fun `onPlayPause -  Returns Ok on game pause`() {
        val paused = PauseGameDto("", true)
        Context.players.clear()
        Context.players.add(player)

        mockMvc.perform(post(clientUrl(LOCALHOST, onPausePath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(paused))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Context.players.clear()
    }

    @Test
    fun `onPlayPause -  Returns Ok on game unpause`() {
        val paused = PauseGameDto("", false)
        Context.players.clear()
        Context.players.add(player)

        mockMvc.perform(post(clientUrl(LOCALHOST, onPausePath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(paused))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Context.players.clear()
    }
}