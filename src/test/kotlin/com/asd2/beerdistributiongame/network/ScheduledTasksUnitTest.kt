package com.asd2.beerdistributiongame.network

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.network.util.NetworkContext
import com.asd2.beerdistributiongame.network.util.getCurrentMilliseconds
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.LobbyState
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import junit.framework.Assert.assertFalse
import junit.framework.Assert.assertTrue
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
class ScheduledTasksUnitTest {

    private val uuid = Settings.uuid
    private val uuid2 = "123-123-123-123-126"
    private val uuid3 = "123-123-123-123-127"

    private val newPlayer = Player(uuid, Settings.username, "192.168.0.1", true)
    private val newPlayer2 = Player(uuid2, Settings.username, "192.168.0.2", true)
    private val newPlayer3 = Player(uuid3, Settings.username, "192.168.0.3", true)
    private val playerList : MutableList<Player> = mutableListOf(newPlayer, newPlayer2, newPlayer3)


    @InjectMocks
    lateinit var scheduledTasks: ScheduledTasks

    @Before
    fun before() {
        Context.isHost = true
        mockkObject(Context)
        every{ Context.activeState } returns LobbyState()
        mockkObject(LobbyState())
        mockkObject(Settings)
        every { Settings.uuid } returns "123-123-123-123-125"
        Context.players.clear()
        Context.gameConfig= GameConfig()
        NetworkContext.hostIp = "127.0.0.1"
        NetworkContext.lobbyPlayerHeartbeats.clear()
    }

    @After
    fun after() {
        unmockkAll()
    }

    @Test
    fun `checkHeartbeats Success - Heartbeats that are too old are removed and the player is removed from the playerlist`() {
        val uuid = Settings.uuid //Test doesn't work without this line.
        NetworkContext.lobbyPlayerHeartbeats[uuid] = 500
        val newPlayer = Player(Settings.uuid, Settings.username, "192.168.0.1", true)
        Context.players.add(newPlayer)

        scheduledTasks.checkHeartbeats()

        MatcherAssert.assertThat(NetworkContext.lobbyPlayerHeartbeats.size, CoreMatchers.`is`(0))
        assertTrue(Context.players.isEmpty())
    }

    @Test
    fun `checkHeartbeats Success - 1 heartbeat is too old and the player is removed - the other 2 heartbeats are not too old and stay`() {
        NetworkContext.lobbyPlayerHeartbeats[uuid] = getCurrentMilliseconds()
        NetworkContext.lobbyPlayerHeartbeats[uuid2] = getCurrentMilliseconds()
        NetworkContext.lobbyPlayerHeartbeats[uuid3] = 500

        Context.players.addAll(playerList)

        scheduledTasks.checkHeartbeats()

        MatcherAssert.assertThat(NetworkContext.lobbyPlayerHeartbeats.size, CoreMatchers.`is`(2))
        assertFalse(Context.players.isEmpty())
    }

    @Test
    fun `checkHeartbeats Success - Heartbeats that are not too old Stay in the HashMap and the player stays in the playerlist`() {
        val newPlayer = Player(Settings.uuid, Settings.username, "192.168.0.1", true)
        NetworkContext.lobbyPlayerHeartbeats[uuid] = getCurrentMilliseconds()
        Context.players.add(newPlayer)

        scheduledTasks.checkHeartbeats()

        MatcherAssert.assertThat(NetworkContext.lobbyPlayerHeartbeats.size, CoreMatchers.`is`(1))
        assertFalse(Context.players.isEmpty())
    }
}