package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.frontend.LeaderboardHandler
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.replay.UserRanking
import java.util.*
import kotlin.collections.LinkedHashMap

class LeaderboardState : State() {

    private val handler = LeaderboardHandler()

    override fun onEnter() = Context.loadScene(handler.getViewFile())

    override fun onExit() {}

    fun getGameRankings(): List<UserRanking> {
        val playerStates : HashMap<String, StateOfPlayer> = hashMapOf()
        playerStates.putAll(Context.statesOfGame.last().playerStates)
        val unsortedGameRankings : MutableList<Pair<String?, Int>> = mutableListOf()
        playerStates.forEach {
            unsortedGameRankings.add(Pair( it.value.player, it.value.budget))
        }
        val rankings = sortGameRankings(unsortedGameRankings)
        return gameRankingsByUsername(rankings)
    }

    fun sortGameRankings(gameRankings: MutableList<Pair<String?, Int>>): LinkedHashMap<String?, Int> {
        gameRankings.radixSort()
        gameRankings.reverse()
        val finalRankings : LinkedHashMap<String?, Int> = linkedMapOf()
        finalRankings.putAll(gameRankings)
        return finalRankings
    }

    fun gameRankingsByUsername(sortedGameRankings: LinkedHashMap<String?, Int>) : List<UserRanking> {
        val users = Context.players
        val userHashmap = LinkedHashMap<String, String>()
        users.forEach {
            userHashmap[it.uuid] = it.username
        }
        val uuids = sortedGameRankings.keys
        val userRankings = mutableListOf<UserRanking>()
        var points = 0
        var rank = 0
        uuids.forEach {
            if (points != sortedGameRankings[it]) {
                points = sortedGameRankings[it]!!
                rank++
            }
            val name = if(userHashmap[it].isNullOrEmpty())
                "No Username"
            else userHashmap[it]!!

            userRankings.add(UserRanking(rank, name, points, it))
        }
        return userRankings.toList()
    }

    private fun MutableList<Pair<String?, Int>>.radixSort() {
        val n = this.size

        val m = this.map { it -> it.second }.max()

        var exp = 1
        while (m!! / exp > 0) {
            val output : MutableList<Pair<String?, Int>> = mutableListOf() // output array
            output.addAll(this)
            var i = 0
            val count = IntArray(10)
            Arrays.fill(count, 0)

            // Store count of occurrences in count[]
            while (i < n) {
                count[this[i].second / exp % 10]++
                i++
            }

            // Change count[i] so that count[i] now contains
            // actual position of this digit in output[]
            i = 1
            while (i < 10) {
                count[i] += count[i - 1]
                i++
            }

            // Build the output array
            i = n - 1
            while (i >= 0) {
                val number = count[this[i].second / exp % 10] - 1
                output[number] = Pair(this[i].first, this[i].second)
                count[this[i].second / exp % 10]--
                i--
            }

            // Copy the output array to arr[], so that arr[] now
            // contains sorted numbers according to curent digit
            i = 0
            while (i < n) {
                this[i] = (output[i])
                i++
            }
            exp *= 10
        }
    }

    fun notifyToGoBackToMainMenu() = setState(MainMenuState())

    fun notifyGoToGraph() = setState(GraphStatisticsState())

    fun notifyToPersonalResult(uuid: String) = setState(PersonalStatisticsState(uuid))

    fun notifyGoToReplayLinear() = setState(ReplayLinearState())

}