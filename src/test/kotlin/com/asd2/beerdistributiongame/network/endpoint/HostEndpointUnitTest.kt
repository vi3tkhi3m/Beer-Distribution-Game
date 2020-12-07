package com.asd2.beerdistributiongame.network.endpoint

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.ChooseRoleDto
import com.asd2.beerdistributiongame.network.HeartbeatDto
import com.asd2.beerdistributiongame.network.PlacerOrderDto
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.chooseRolePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.endGamePath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.placeOrderPath
import com.asd2.beerdistributiongame.network.endpoint.HostEndpoint.Companion.sendHeartbeatPath
import com.asd2.beerdistributiongame.network.service.HostService
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.hostUrl
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@ExtendWith(SpringExtension::class)
@WebMvcTest(HostEndpoint::class)
@RunWith(SpringRunner::class)
class HostEndpointUnitTest {

    private val player = Player("uuid", "Username", "192", true)
    private val chooseRoleDTO = ChooseRoleDto(player.uuid, Roles.FACTORY)
    private val heartbeatDTO = HeartbeatDto("123-123-123-123-123")

    private lateinit var mockMvc: MockMvc

    @InjectMocks
    lateinit var controller: HostEndpoint

    @Before
    fun before() {
        NetworkContext.hostIp = "127.0.0.1"
        mockkObject(HostService)
        every { HostService.handlePlaceOrder(any()) } just runs
        mockMvc = MockMvcBuilders.standaloneSetup(controller).setMessageConverters(MappingJackson2HttpMessageConverter()).build()
        Context.rolesInGame.playerRoles.clear()
        Context.isHost = true
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun `chooseRole Success -  Returns Ok `() {
        Context.players.add(player)
        mockMvc.perform(post(hostUrl(chooseRolePath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(chooseRoleDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Context.players.clear()
    }

    @Test
    fun `chooseRole Fails -  Returns Service Unavailable `() {
        Context.players.add(player)
        Context.isHost = false
        mockMvc.perform(post(hostUrl(chooseRolePath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(chooseRoleDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isServiceUnavailable)
                .andReturn()
        Context.players.clear()
    }

    @Test
    fun `chooseRole Fails -  Returns Forbidden `() {
        mockMvc.perform(post(hostUrl(chooseRolePath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(chooseRoleDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isForbidden)
                .andReturn()
    }

    @Test
    fun `placeOrder Success - Returns Ok`() {
        val order = Order("origin", "destination", 2)
        val placeOrderDTO = PlacerOrderDto(order)
        mockMvc.perform(post(hostUrl(placeOrderPath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(placeOrderDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
    }

    @Test
    fun `endGame Success - Returns Ok`() {
        every { HostService.handleEndGame() } just runs
        mockMvc.perform(post(hostUrl(endGamePath)))
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()
        verify { HostService.handleEndGame() }
    }

    @Test
    fun `sendHeartbeat Success - Returns Ok`() {
        mockMvc.perform(post(hostUrl(sendHeartbeatPath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(heartbeatDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isOk)
                .andReturn()

        Context.players.clear()
        NetworkContext.lobbyPlayerHeartbeats.clear()
    }

    @Test
    fun `sendHeartbeat Fails - Reached client is not host`() {
        Context.isHost = false
        mockMvc.perform(post(hostUrl(sendHeartbeatPath))
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(heartbeatDTO))
                .accept(MediaType.APPLICATION_JSON)
        )
                .andExpect(MockMvcResultMatchers.status().isServiceUnavailable)
                .andReturn()
    }
}