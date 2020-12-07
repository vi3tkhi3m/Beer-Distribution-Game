package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.JavaFXApplication
import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import com.asd2.beerdistributiongame.state.EditGameAgentState
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

class EditGameAgentHandlerE2ETest : ApplicationTest() {
    private val fxRobot = FxRobot()

    override fun start(stage: Stage) {
        StorageManager.path = "src/test/resources/E2EtestGameagents"
        StorageManager.createAgent("testAgent", "NewOrder = Stock")

        stage.title = "The Beer Game"
        Context.stage = stage
        Context.stage.icons.add(Image(JavaFXApplication::class.java.classLoader.getResourceAsStream("images/application_icon.png")))
        Context.activeState = EditGameAgentState()
        stage.show()
        stage.toFront()
    }

    @Test
    fun editAgentSuccess() {
        fxRobot.moveTo("#comboBoxSelectAgent")
        fxRobot.clickOn("#comboBoxSelectAgent")
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)

        fxRobot.doubleClickOn("#defaultOrder")
        fxRobot.write("100")

        fxRobot.moveTo("#buttonSaveAgent")
        fxRobot.clickOn("#buttonSaveAgent")

        val result = StorageManager.getRules("testAgent")
        Assert.assertEquals("NewOrder = 100", result)
    }

    @Test
    fun deleteAgentSuccess() {
        fxRobot.moveTo("#comboBoxSelectAgent")
        fxRobot.clickOn("#comboBoxSelectAgent")
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)

        fxRobot.moveTo("#buttonDeleteAgent")
        fxRobot.clickOn("#buttonDeleteAgent")

        assert(!StorageManager.agentExists("testAgent"))
    }

    @Test
    fun insertSymbolSuccess() {
        fxRobot.moveTo("#comboBoxSelectAgent")
        fxRobot.clickOn("#comboBoxSelectAgent")
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)

        fxRobot.doubleClickOn("#defaultOrder")
        type(KeyCode.BACK_SPACE)
        fxRobot.clickOn("#defaultOrder")
        type(KeyCode.BACK_SPACE)

        fxRobot.moveTo("#comboBoxInsertSymbol")
        fxRobot.clickOn("#comboBoxInsertSymbol")
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)

        fxRobot.moveTo("#buttonSaveAgent")
        fxRobot.clickOn("#buttonSaveAgent")

        val result = StorageManager.getRules("testAgent")
        Assert.assertEquals("NewOrder = +", result)
    }

    @Test
    fun editingAgentWithIllegalCharsShowsError() {
        fxRobot.moveTo("#comboBoxSelectAgent")
        fxRobot.clickOn("#comboBoxSelectAgent")
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)

        fxRobot.clickOn("#defaultOrder")
        fxRobot.write("%")

        fxRobot.moveTo("#buttonSaveAgent")
        fxRobot.clickOn("#buttonSaveAgent")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
    }

    @Test
    fun editingAgentWithWrongUsageOfVariablesShowsError() {
        fxRobot.moveTo("#comboBoxSelectAgent")
        fxRobot.clickOn("#comboBoxSelectAgent")
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)

        fxRobot.clickOn("#defaultOrder")
        fxRobot.write("test3f")

        fxRobot.moveTo("#buttonSaveAgent")
        fxRobot.clickOn("#buttonSaveAgent")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
    }

    @Test
    fun editingAgentWithoutRulesShowsError() {
        fxRobot.moveTo("#comboBoxSelectAgent")
        fxRobot.clickOn("#comboBoxSelectAgent")
        type(KeyCode.DOWN)
        type(KeyCode.ENTER)

        fxRobot.moveTo("#defaultOrder")
        fxRobot.doubleClickOn("#defaultOrder")
        type(KeyCode.DELETE)
        type(KeyCode.DELETE)

        fxRobot.moveTo("#buttonSaveAgent")
        fxRobot.clickOn("#buttonSaveAgent")

        FxAssert.verifyThat("#alertOK", NodeMatchers.isVisible())
        fxRobot.moveTo("#alertOK")
        fxRobot.clickOn("#alertOK")
    }

    @After
    fun removeAgent() {
        StorageManager.deleteAgent("testAgent")
        StorageManager.path = "data/gameagents"
    }
}