package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.JavaFXApplication
import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.network.Client
import com.asd2.beerdistributiongame.network.NetworkDataResponse
import com.asd2.beerdistributiongame.state.JoinGameState
import com.asd2.beerdistributiongame.state.LobbyState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockkObject
import io.mockk.unmockkAll
import javafx.scene.image.Image
import javafx.stage.Stage
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest

class GameConfigToLobbyE2ETest : ApplicationTest() {

    init {
        unmockkAll()
        mockkObject(Client)
    }

    @Before
    fun before() {
        unmockkAll()
        mockkObject(Client)
    }

    override fun start(stage: Stage) {
        Context.stage = stage

        Context.stage.title = "The Beer Game"
        Context.stage.icons.add(Image(JavaFXApplication::class.java.classLoader.getResourceAsStream("images/application_icon.png")))
        Context.activeState = JoinGameState()
        Context.stage.show()
    }

    @Test
    fun `join lobby with valid ip`() {
        coEvery { Client.connect(any()) } returns NetworkDataResponse(true)
        // Click on the ip field, enter a valid ip and join the lobby
        doubleClickOn("#ipAddress")
        write("127.0.0.1")
        clickOn("#joinButton")
        // Check if the network function is called correctly and if the state is switched to the lobby state
        coVerify { Client.connect(any()) }
        assertTrue(Context.activeState is LobbyState)
    }

    @Test
    fun `join lobby with network exception`() {
        coEvery { Client.connect(any()) } returns NetworkDataResponse(false, "Netwerk error")
        // Click on the ip field, enter a valid ip and join the lobby
        doubleClickOn("#ipAddress")
        write("127.0.0.1")
        clickOn("#joinButton")
        // Check if the network function is called correctly and if the state is not switched to lobby
        coVerify { Client.connect(any()) }
        assertTrue(Context.activeState is JoinGameState)
    }

    @Test
    fun `join lobby with invalid ip`() {
        coEvery { Client.connect(any()) } returns NetworkDataResponse(false, "Netwerk error")
        // Click on the ip field, enter a valid ip and join the lobby
        doubleClickOn("#ipAddress")
        write("127.0.0")
        clickOn("#joinButton")
        // Check if the network function is not called because the IP is invalid and if the state is not switched to the lobby state
        coVerify(exactly = 0) { Client.connect(any()) }
        assertTrue(Context.activeState is JoinGameState)
    }
}