package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.JavaFXApplication
import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.EnterNameState
import com.asd2.beerdistributiongame.state.MainMenuState
import javafx.scene.image.Image
import javafx.stage.Stage
import org.junit.AfterClass
import org.junit.Test
import org.testfx.framework.junit.ApplicationTest
import java.io.File
import java.nio.file.Files

class EnterNameHandlerE2ETest : ApplicationTest() {

	override fun start(stage: Stage) {
        Context.stage = stage

        Context.stage.title = "The Beer Game"
        Context.stage.icons.add(Image(JavaFXApplication::class.java.classLoader.getResourceAsStream("images/application_icon.png")))
        Context.activeState = EnterNameState()
        Context.stage.show()
	}

	companion object {
		@AfterClass
		fun teardown() {
			Files.deleteIfExists(File("data/setting.properties").toPath())
		}
	}

	@Test
	fun `enter correct player name`() {
		val username = "test user"
		clickOn("#playerName")
		write(username)
		clickOn("#chooseNameButton")

		assert(Settings.fileExists())
		assert(Settings.username == username)
		assert(Context.activeState is MainMenuState)
	}

	@Test
	fun `enter player name too long`() {
		val username = "thisistoolongofaplayername"

		clickOn("#playerName")
		write(username)
		clickOn("#chooseNameButton")

		assert(Context.activeState is EnterNameState)
	}

	@Test
	fun `exit game`() {
		clickOn("#quitGameButton")
	}
}