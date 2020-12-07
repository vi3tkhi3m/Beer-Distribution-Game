package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.replay.SavedGamePath
import com.asd2.beerdistributiongame.state.LeaderboardState
import com.asd2.beerdistributiongame.state.ReplayLobbyState
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import javafx.stage.Stage
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.testfx.api.FxAssert
import org.testfx.api.FxRobot
import org.testfx.framework.junit.ApplicationTest
import org.testfx.matcher.base.NodeMatchers

class ReplayLobbyHandlerE2ETest : ApplicationTest() {

    private val fxRobot = FxRobot()

    override fun start(stage: Stage) {
        val mock = mockk<SavedGamePath>()
        every { mock.savedGamesDir } returns "src/test/resources/replaylobbysavedgames/valid/"
        Context.stage = stage
        val customReplayLobbyState = ReplayLobbyState()
        Context.activeState = customReplayLobbyState
        stage.show()
        stage.toFront()
    }

    @After
    fun unmock() {
        unmockkAll()
    }

    @Test
    fun selectGameSuccess() {
        fxRobot.moveTo("#gameId")
        fxRobot.moveBy(0.0, 30.0) // Move from header to first row
        fxRobot.clickOn()

        Assert.assertTrue(Context.activeState is LeaderboardState)
        Assert.assertEquals("asdfux32-1234-4651-b6e6-sdkalske2310",
                Context.gameConfig.gameID)
    }

    @Test
    fun selectGameNoSelect() {
        fxRobot.moveTo("#gameId")
        fxRobot.moveBy(0.0, 100.0) // Move from header to empty row
        fxRobot.clickOn()

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")

        Assert.assertTrue(Context.activeState is ReplayLobbyState)
    }
}