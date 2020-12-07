package com.asd2.beerdistributiongame

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.network.util.CONNECTION_TIMEOUT
import com.asd2.beerdistributiongame.network.util.SOCKET_TIMEOUT
import com.asd2.beerdistributiongame.state.CloseAppState
import com.mashape.unirest.http.Unirest
import javafx.application.Application
import javafx.application.Application.launch
import javafx.event.EventHandler
import javafx.scene.image.Image
import javafx.stage.Stage
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan
import org.springframework.scheduling.annotation.EnableScheduling

fun main(args: Array<String>) {
    // This is used to start the Spring Boot App.
    Context.springContext = runApplication<JavaFXApplication>(*args)
    // This is used to start the JavaFX App.
    launch(JavaFXApplication::class.java)
    // Sets the Unirest timeouts so the heartbeat functionality works on Mac.
    Unirest.setTimeouts(CONNECTION_TIMEOUT, SOCKET_TIMEOUT)
}

@ComponentScan("com.asd2.beerdistributiongame.network")
@EnableScheduling
@SpringBootApplication
class JavaFXApplication : Application() {

    override fun start(stage: Stage) {
        Context.stage = stage
        Context.stage.onCloseRequest = EventHandler { Context.activeState = CloseAppState() }
        Context.stage.title = "The Beer Game"
        Context.stage.icons.add(Image(JavaFXApplication::class.java.classLoader.getResourceAsStream("images/application_icon.png")))
        Context.activeState.onEnter()
        Context.stage.show()
    }
}