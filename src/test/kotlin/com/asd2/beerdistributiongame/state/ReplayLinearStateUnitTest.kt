package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import io.mockk.*
import org.junit.*

class ReplayLinearStateUnitTest: GameConfigMock() {

    private val state: ReplayLinearState = mockk()
    private val stateSpy: ReplayLinearState = spyk(recordPrivateCalls = true)
    private val contextMock: Context = mockk(relaxed = true)

    @Before
    fun init() {
        clearMocks(state, stateSpy, contextMock)
    }

    @After
    fun clear() {
        clearAllMocks()
        unmockkAll()
        Context.gameConfig = GameConfig()
    }

    @Test
    fun getTurnDetailsCallsGetLinkTurnDetails() {
        val stateOfGameMock = mockk<StateOfGame>()
        val playerStateMockk = mockk<StateOfPlayer>()
        val playerStates = HashMap<String, StateOfPlayer>()
        playerStates["1"] = playerStateMockk
        val playerRoles = rolesInGameLinear.playerRoles
        every { contextMock.replayTurnNumber } returns 1
        every { contextMock.gameConfig.maxTurns } returns 40
        every { contextMock.statesOfGame[0] } returns stateOfGameMock
        every { contextMock.rolesInGame.playerRoles } returns playerRoles
        for (roleKey in playerRoles) {
            every { stateSpy["getLinkTurnDetails"](stateOfGameMock, roleKey.key) } returns "Nothing"
        }
        every { stateOfGameMock.playerStates } returns  playerStates

        stateSpy.onEnter()
        Assert.assertEquals(1, stateSpy.turnNumber)
        Assert.assertEquals(40, stateSpy.maxTurns)

        verify {
            for (roleKey in playerRoles) {
                stateSpy["getLinkTurnDetails"](stateOfGameMock, roleKey.key)
            }
        }
    }
}