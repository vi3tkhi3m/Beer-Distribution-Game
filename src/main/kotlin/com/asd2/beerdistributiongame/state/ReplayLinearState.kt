package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.ReplayLinearViewHandler
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.replay.SupplyChainLinkDetails

class ReplayLinearState : State() {

	lateinit var retailerDetails: SupplyChainLinkDetails
	lateinit var wholesalerDetails: SupplyChainLinkDetails
	lateinit var distributorDetails: SupplyChainLinkDetails
	lateinit var factoryDetails: SupplyChainLinkDetails

	var turnNumber: Int = 0
	var maxTurns: Int = 0

	private val handler = ReplayLinearViewHandler()

	override fun onEnter() {
		getTurnDetails()
		Context.loadScene(handler.getViewFile())
	}

	override fun onExit() {
		//TODO() Functie hoeft niet gebruikt te worden
	}

	fun notifyToGoBackToMainMenu() = setState(MainMenuState())

	fun notifyToGoToFinalResults() = setState(LeaderboardState())

	fun notifyToGoToNextTurn() {
		if (turnNumber < Context.statesOfGame[Context.statesOfGame.size - 1].turnNumber) {
			Context.replayTurnNumber++
			setState(this)
		}
	}

	fun notifyToGoToPreviousTurn() {
		if (turnNumber > 1) {
			Context.replayTurnNumber--
			setState(this)
		}
	}

	fun notifyToGoToSupplyChainEntityView() = {
		//TODO wachten op implementatie van Mitch
	}

    fun notifyToGoToTurn(turn: Int) {
        if (turn < Context.statesOfGame[Context.statesOfGame.size - 1].turnNumber) {
            Context.replayTurnNumber = turn
            setState(this)
        }
    }

	private fun getTurnDetails() {
		turnNumber = Context.replayTurnNumber
		maxTurns = Context.gameConfig.maxTurns
		val turnDetails = Context.statesOfGame[turnNumber - 1]
        for (roleKey in Context.rolesInGame.playerRoles) {
            getLinkTurnDetails(turnDetails, roleKey.key)
        }
	}

	private fun getLinkTurnDetails(turnDetails: StateOfGame, roleKey: String) {
		var name: String
		val supplyChainLinkDetails = turnDetails.playerStates[roleKey]
		val supplyChainLinkPlayer = Context.players.filter { player -> player.uuid == supplyChainLinkDetails!!.player }.firstOrNull()
		if (supplyChainLinkPlayer != null) {
			name = supplyChainLinkPlayer.username
		} else {
			name = roleKey
		}
        if (supplyChainLinkDetails != null) {
            when (Context.rolesInGame.playerRoles[roleKey]!!.role) {
                Roles.RETAILER -> retailerDetails = SupplyChainLinkDetails(name, supplyChainLinkDetails.stock, supplyChainLinkDetails.incomingOrders.values.sum(), supplyChainLinkDetails.outgoingOrder, supplyChainLinkDetails.incomingDelivery, supplyChainLinkDetails.outgoingDeliveries.values.sum(), supplyChainLinkDetails.productionDelay)
                Roles.WHOLESALE -> wholesalerDetails = SupplyChainLinkDetails(name, supplyChainLinkDetails.stock, supplyChainLinkDetails.incomingOrders.values.sum(), supplyChainLinkDetails.outgoingOrder, supplyChainLinkDetails.incomingDelivery, supplyChainLinkDetails.outgoingDeliveries.values.sum(), supplyChainLinkDetails.productionDelay)
                Roles.WAREHOUSE -> distributorDetails = SupplyChainLinkDetails(name, supplyChainLinkDetails.stock, supplyChainLinkDetails.incomingOrders.values.sum(), supplyChainLinkDetails.outgoingOrder, supplyChainLinkDetails.incomingDelivery, supplyChainLinkDetails.outgoingDeliveries.values.sum(), supplyChainLinkDetails.productionDelay)
                Roles.FACTORY -> factoryDetails = SupplyChainLinkDetails(name, supplyChainLinkDetails.stock, supplyChainLinkDetails.incomingOrders.values.sum(), supplyChainLinkDetails.outgoingOrder, supplyChainLinkDetails.incomingDelivery, supplyChainLinkDetails.outgoingDeliveries.values.sum(), supplyChainLinkDetails.productionDelay)
                else -> println("Failed")
            }
        }
	}
}