package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.JavaFXApplication
import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.state.GameConfigState
import com.asd2.beerdistributiongame.state.LobbyState
import com.asd2.beerdistributiongame.state.MainMenuState
import javafx.scene.image.Image
import javafx.stage.Stage
import org.junit.Assert
import org.junit.Test
import org.testfx.api.FxRobot
import org.testfx.framework.junit.ApplicationTest

class GameConfigHandlerE2ETest : ApplicationTest() {

    private val fxRobot = FxRobot()

    override fun start(stage: Stage) {
        Context.stage = stage

        Context.stage.title = "The Beer Game"
        Context.stage.icons.add(Image(JavaFXApplication::class.java.classLoader.getResourceAsStream("images/application_icon.png")))
        Context.activeState = GameConfigState()
        Context.stage.show()
    }

    @Test
    fun back_to_main_menu_pressed() {
        clickOn("#backToMenu")
        assert(Context.activeState is MainMenuState)
    }

    @Test
    fun open_lobby_pressed_after_inputting_name() {
        clearAndWriteInField("#gameName", "The name of the game")
        clickOn("#openLobby")
        assert(Context.activeState is LobbyState)
    }

    @Test
    fun game_config_happy_flow() {
        val gameName = "Lorem"
        val initialStock = 14
        val initialBudget = 500
        val turnDuration = 60 * 30
        val backlogCost = 50
        val stockCost = 632
        val productionCost = 1
        val maxTurns = 32

        val quantityOfFactories = 2
        val quantityOfWarehouses = 4
        val quantityOfWholesales = 8
        val quantityOfRetailers = 16

        val retailerProfit = 12

        val minCustomerDemand = 30
        val maxCustomerDemand = 80

        clearAndWriteInField("#gameName", gameName)

        clickOn("#gameMode")
        clickOn("Pyramid")

        clickOn("#overviewType")
        clickOn("Global")

        clearAndWriteInField("#initialStock", initialStock.toString())
        clearAndWriteInField("#initialBudget", initialBudget.toString())
        clearAndWriteInField("#turnDuration", turnDuration.toString())
        clearAndWriteInField("#backlogCost", backlogCost.toString())
        clearAndWriteInField("#stockCost", stockCost.toString())
        clearAndWriteInField("#productionCost", productionCost.toString())
        clearAndWriteInField("#maxTurns", maxTurns.toString())
        clickOn("#infiniteTurns")

        clearAndWriteInField("#quantityOfFactories", quantityOfFactories.toString())
        clearAndWriteInField("#quantityOfWarehouses", quantityOfWarehouses.toString())
        clearAndWriteInField("#quantityOfWholesales", quantityOfWholesales.toString())
        clearAndWriteInField("#quantityOfRetailers", quantityOfRetailers.toString())

        clearAndWriteInField("#retailProfit", retailerProfit.toString())

        clearAndWriteInField("#minCustomerDemand", minCustomerDemand.toString())
        clearAndWriteInField("#maxCustomerDemand", maxCustomerDemand.toString())

        Assert.assertEquals(gameName, Context.gameConfig.gameName)

        Assert.assertEquals(initialStock, Context.gameConfig.initialStock)
        Assert.assertEquals(initialBudget, Context.gameConfig.initialBudget)
        Assert.assertEquals(turnDuration, Context.gameConfig.turnDuration)
        Assert.assertEquals(backlogCost, Context.gameConfig.backlogCost)
        Assert.assertEquals(stockCost, Context.gameConfig.stockCost)
        Assert.assertEquals(productionCost, Context.gameConfig.productionCost)
        Assert.assertEquals(maxTurns, Context.gameConfig.maxTurns)
        Assert.assertEquals(true, Context.gameConfig.infiniteTurns)

        Assert.assertEquals(quantityOfFactories, Context.gameConfig.quantityOfFactories)
        Assert.assertEquals(quantityOfWarehouses, Context.gameConfig.quantityOfWarehouses)
        Assert.assertEquals(quantityOfWholesales, Context.gameConfig.quantityOfWholesales)
        Assert.assertEquals(quantityOfRetailers, Context.gameConfig.quantityOfRetailers)

        Assert.assertEquals(retailerProfit, Context.gameConfig.retailProfit)

        Assert.assertEquals(minCustomerDemand, Context.gameConfig.minCustomerDemand)
        Assert.assertEquals(maxCustomerDemand, Context.gameConfig.maxCustomerDemand)
    }

    @Test
    fun non_numeric_write_in_numeric_field() {
        clearAndWriteInField("#initialStock", "12abc")
        Assert.assertEquals(12, Context.gameConfig.initialStock)
    }

    private fun clearAndWriteInField(field: String, value: String) {
        doubleClickOn(field)
        write(value)
    }
}