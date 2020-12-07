package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.frontend.GameHandler
import com.asd2.beerdistributiongame.frontend.GameManagerHandler
import com.asd2.beerdistributiongame.frontend.Handler
import com.asd2.beerdistributiongame.gamelogic.configuration.GameMode
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.controller.StorageController
import com.asd2.beerdistributiongame.gamelogic.game.BeerDistributionGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order
import com.asd2.beerdistributiongame.network.Client
import com.asd2.beerdistributiongame.network.Host
import com.asd2.beerdistributiongame.network.NextTurnDto
import com.asd2.beerdistributiongame.replay.SavedGamePath.playerFileName
import com.asd2.beerdistributiongame.replay.SavedGamePath.savedGamesDir
import com.asd2.beerdistributiongame.replay.SupplyChainLinkDetails
import com.asd2.beerdistributiongame.settings.Settings
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mapJSONToFile
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class GameState : State() {

    lateinit var handler: Handler<GameState>

    lateinit var retailerDetails: SupplyChainLinkDetails
    lateinit var wholesalerDetails: SupplyChainLinkDetails
    lateinit var distributorDetails: SupplyChainLinkDetails
    lateinit var factoryDetails: SupplyChainLinkDetails

    override fun onEnter() {
        if (Context.isHost) {
            Host.onPlaceOrder = ::onPlaceOrder

            Context.statesOfGame.add(createGame())
            if (Context.isGameManager) getTurnDetails()

            GlobalScope.launch {
                Host.sendAllRoles()
                nextTurn()
            }

            handler = GameManagerHandler()
        } else {
            Client.onNextTurn = ::onNextTurn
            Client.onEndGame = ::onEndGame

            handler = GameHandler()

            Context.beerDistributionGame!!.startTimer(Context.gameConfig.turnDuration)
        }

        Context.loadScene(handler.getViewFile())
    }

    fun createGame(): StateOfGame {
        Context.beerDistributionGame = BeerDistributionGame()
        Context.beerDistributionGame!!.supplyChain.createCustomerDemand(Context.gameConfig.minCustomerDemand, Context.gameConfig.maxCustomerDemand)
        Context.beerDistributionGame!!.initialSave(Context.gameConfig)
        mapJSONToFile(playerFileName, savedGamesDir, Context.players)
        Context.statesOfGame.add(Context.beerDistributionGame!!.getStateOfGame())

        Context.gameConfig.startGameTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        return Context.statesOfGame.last()
    }

    override fun onExit() {
        Client.onNextTurn = {}
        Client.onEndGame = {}
        Host.onPlaceOrder = {}

        Context.beerDistributionGame!!.cancelTimer()
        Context.isHost = false
        Context.isGameManager = false
    }

    fun notifyToGoBackToMainMenu() {
        if (Context.isHost) {
            Host.migrateHost()
        } else {
            Client.disconnect()
        }
        setState(MainMenuState())
    }

    /**
     * This function handles the next turn in the game on the client side
     * @nextTurnDto data object containing stateOfGame and beerDistributionGame
     */
    private fun onNextTurn(nextTurnDto: NextTurnDto) {
        if(nextTurnDto.beerDistributionGame != null) {
            Context.beerDistributionGame!!.cancelTimer()
            Context.beerDistributionGame = nextTurnDto.beerDistributionGame
        }
        Context.statesOfGame.add(nextTurnDto.stateOfGame)
        StorageController.saveTurn(Context.gameConfig.gameName, Context.statesOfGame)
        StorageController.savePlayers(Context.gameConfig.gameName)
        Context.beerDistributionGame!!.startTimer(Context.gameConfig.turnDuration)
        GlobalScope.launch(Dispatchers.Main) {
            if (handler is GameHandler) (handler as GameHandler).nextTurn()
        }
    }

    fun getCurrentTurnNumber() = Context.beerDistributionGame!!.getCurrentTurn()

    fun notifyToPlaceOrder(orderAmount: Int) = handler.process {
        val order = createOrder(orderAmount)

        val response = withContext(Dispatchers.Default) { Client.placeOrder(order) }
        if (!response.success)
            handler.showAlert(response.message)
    }

    private fun createOrder(orderAmount: Int): Order {
        val roleKey = Context.rolesInGame.playerRoles.filter{ it.value.playerId == Settings.uuid }.keys.first()
        var orderDestination = ""

        if ((Context.gameConfig.gameMode == GameMode.LINEAR || Context.gameConfig.gameMode == GameMode.PYRAMID) && Context.beerDistributionGame!!.supplyChain.chainStructure.playerRoles.containsKey(roleKey)) {
            orderDestination = Context.beerDistributionGame!!.supplyChain.chainStructure.playerRoles[roleKey]!!.parents[0]
        }

        return Order(roleKey, orderDestination, orderAmount)
    }

    fun notifyToPersonalResults() = setState(PersonalStatisticsState())

    suspend fun notifyToDeactivateGameAgent() = withContext(Dispatchers.Default) { Client.deactivateGameAgent() }

    suspend fun notifyToAcceptGameAgent(selectedGameAgent: String) = withContext(Dispatchers.Default) { Client.activateGameAgent(File("data/gameagents/$selectedGameAgent").readText(Charsets.UTF_8)) }

    fun loadPreviousTurnInfo() = Context.beerDistributionGame!!.getPreviousTurnInfo(Context.gameConfig)

    fun notifyToEndGame() {
        Client.endGame()
        setState(GraphStatisticsState())
    }

    private fun onPlaceOrder(order: Order) {
        Context.beerDistributionGame!!.gameFlow.receiveOrder(order)
    }

    private fun onEndGame() {
        handlerToMainMenu()
    }

    private fun handlerToMainMenu() = handler.process {
        if (handler is GameHandler) (handler as GameHandler).activateGameStoppedAlert()
    }

    private fun getTurnDetails() = Context.rolesInGame.playerRoles.forEach { getLinkTurnDetails(Context.statesOfGame.last(), it.key) }

    /**
     * Updates the data that is needed to fill the game overview from the game manager
     * @turnDetails the state of game object from the previous turn
     * @roleKey the UUID key from the given role
     */
    private fun getLinkTurnDetails(turnDetails: StateOfGame, roleKey: String) {
        val supplyChainLinkDetails = turnDetails.playerStates[roleKey]
        val supplyChainLinkPlayer = Context.players.firstOrNull { player -> player.uuid == supplyChainLinkDetails!!.player }

        if (supplyChainLinkDetails != null) {
            val details = SupplyChainLinkDetails(supplyChainLinkPlayer?.username ?: roleKey,
                    supplyChainLinkDetails.stock, supplyChainLinkDetails.incomingOrders.values.sum(),
                    supplyChainLinkDetails.outgoingOrder, supplyChainLinkDetails.incomingDelivery,
                    supplyChainLinkDetails.outgoingDeliveries.values.sum(), supplyChainLinkDetails.productionDelay)

            when (Context.rolesInGame.playerRoles[roleKey]!!.role) {
                Roles.RETAILER -> retailerDetails = details
                Roles.WHOLESALE -> wholesalerDetails = details
                Roles.WAREHOUSE -> distributorDetails = details
                Roles.FACTORY -> factoryDetails = details
            }
        }
    }

    suspend fun nextTurn() {
        Context.beerDistributionGame!!.startTimer(Context.gameConfig.turnDuration)
        Context.beerDistributionGame!!.gameFlow.currentTurn++
        Host.nextTurn(Context.statesOfGame.last())

        if (handler is GameManagerHandler){
            getTurnDetails()
            (handler as GameManagerHandler).setProperties()
        }
    }

    private fun onDisconnect(@Suppress("UNUSED_PARAMETER") player: Player?) = setState(MainMenuState())
}