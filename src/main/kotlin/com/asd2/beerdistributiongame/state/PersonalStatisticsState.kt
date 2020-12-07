package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.PersonalStatisticsHandler
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.replay.PersonStatistics

class PersonalStatisticsState(var uuid: String = Context.statesOfGame.first().playerStates.values.first().player!!) : State() {

    private val handler = PersonalStatisticsHandler()

    override fun onEnter() = Context.loadScene(handler.getViewFile())

	override fun onExit(){
        //todo
    }

    fun notifyGoToReplayLinear() = setState(ReplayLinearState())

	fun notifyGoToMainMenu() = setState(MainMenuState())

	fun notifyGoToRankings() = setState(LeaderboardState())

	fun notifyGoToGraph() = setState(GraphStatisticsState())

    fun getUsername() = if(Context.players.filter { it.uuid == uuid }.isEmpty()) "No username" else Context.players.first { it.uuid == uuid }.username

    fun getUUID() = uuid

    fun getInitialBudget() = Context.gameConfig.initialBudget

	fun getPersonalStatistics(uuid: String): List<PersonStatistics> {
        val personalStatisticsList =  mutableListOf<PersonStatistics>()
        Context.statesOfGame.forEach{
            val playerStates = it.playerStates.filter { value -> value.value.player == uuid }.values.toList()
            if(playerStates.isEmpty())
                return getPersonalStatistics(it.playerStates.keys.first())
            val playerState = playerStates.first()
            personalStatisticsList.add(getPersonalStatisticFromPlayerState(it.turnNumber,playerState))
        }
        return personalStatisticsList
    }

    private fun getPersonalStatisticFromPlayerState(turnNumber: Int, playerState: StateOfPlayer): PersonStatistics{
        return PersonStatistics(turnNumber,
                playerState.gameAgentActive,
                playerState.backlogs.values.sum(),
                playerState.stock,
                playerState.budget,
                playerState.incomingOrders.values.sum(),
                playerState.outgoingOrder,
                playerState.incomingDelivery,
                playerState.outgoingDeliveries.values.sum())
    }
}