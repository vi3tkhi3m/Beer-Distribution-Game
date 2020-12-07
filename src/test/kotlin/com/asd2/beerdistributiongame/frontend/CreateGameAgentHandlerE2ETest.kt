package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.JavaFXApplication
import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import com.asd2.beerdistributiongame.state.CreateGameAgentState
import javafx.scene.image.Image
import javafx.scene.input.KeyCode
import javafx.stage.Stage
import org.junit.After
import org.junit.Assert
import org.junit.Test
import org.testfx.api.FxAssert
import org.testfx.api.FxRobot
import org.testfx.framework.junit.ApplicationTest
import org.testfx.matcher.base.NodeMatchers


class CreateGameAgentHandlerE2ETest : ApplicationTest() {
    private val fxRobot = FxRobot()

    override fun start(stage: Stage) {
        StorageManager.path = "src/test/resources/E2EtestGameagents"

        stage.title = "The Beer Game"
        Context.stage = stage
        Context.stage.icons.add(Image(JavaFXApplication::class.java.classLoader.getResourceAsStream("images/application_icon.png")))
        Context.activeState = CreateGameAgentState()
        stage.show()
        stage.toFront()
    }

    @Test
    fun creatingMultipleAgentsWithTheSameNameShowsError() {
        fxRobot.moveTo("#agentNameInput")
        fxRobot.clickOn("#agentNameInput")
        fxRobot.write("testAgent")

        fxRobot.moveTo("#defaultOrder")
        fxRobot.clickOn("#defaultOrder")
        fxRobot.write("1")

        fxRobot.moveTo("#createGameAgentButton")
        fxRobot.clickOn("#createGameAgentButton")
        fxRobot.clickOn("#createGameAgentButton")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
        StorageManager.deleteAgent("testAgent")
    }

    @Test
    fun creatingAgentWithIllegalCharsShowsError() {
        fxRobot.moveTo("#agentNameInput")
        fxRobot.clickOn("#agentNameInput")
        fxRobot.write("testAgent")

        fxRobot.clickOn("#defaultOrder")
        fxRobot.write("%")

        fxRobot.moveTo("#createGameAgentButton")
        fxRobot.clickOn("#createGameAgentButton")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
    }

    @Test
    fun creatingAgentWithWrongVariableUsageShowsError() {
        fxRobot.moveTo("#agentNameInput")
        fxRobot.clickOn("#agentNameInput")
        fxRobot.write("testAgent")

        fxRobot.doubleClickOn("#defaultOrder")
        fxRobot.write("variable")

        fxRobot.moveTo("#createGameAgentButton")
        fxRobot.clickOn("#createGameAgentButton")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
    }

    @Test
    fun creatingAgentWithoutNameShowsError() {
        fxRobot.moveTo("#agentNameInput")
        fxRobot.doubleClickOn("#agentNameInput")
        type(KeyCode.DELETE)

        fxRobot.clickOn("#defaultOrder")
        fxRobot.write("1")

        fxRobot.moveTo("#createGameAgentButton")
        fxRobot.clickOn("#createGameAgentButton")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
    }

    @Test
    fun creatingAgentWithoutRulesShowsError() {
        fxRobot.moveTo("#defaultOrder")
        fxRobot.doubleClickOn("#defaultOrder")
        type(KeyCode.DELETE)
        type(KeyCode.DELETE)

        fxRobot.moveTo("#agentNameInput")
        fxRobot.clickOn("#agentNameInput")
        fxRobot.write("testAgent")

        fxRobot.moveTo("#createGameAgentButton")
        fxRobot.clickOn("#createGameAgentButton")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
    }

    @Test
    fun createAgentSuccess() {
        fxRobot.moveTo("#agentNameInput")
        fxRobot.clickOn("#agentNameInput")
        fxRobot.write("testAgent")

        fxRobot.clickOn("#defaultOrder")
        fxRobot.doubleClickOn("#defaultOrder")
        type(KeyCode.DELETE)
        fxRobot.write("Stock")

        fxRobot.moveTo("#createGameAgentButton")
        fxRobot.clickOn("#createGameAgentButton")

        val result = StorageManager.getRules("testAgent")
        Assert.assertEquals("NewOrder = Stock", result)
    }

    @After
    fun removeAgent() {
        StorageManager.deleteAgent("testAgent")
        StorageManager.path = "data/gameagents"
    }
}