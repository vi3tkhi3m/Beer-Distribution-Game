package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfGame
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import com.asd2.beerdistributiongame.replay.UserRanking
import io.mockk.clearAllMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Test

class LeaderboardStateUnitTest {

    @After
    fun after(){
        Context.players.clear()
        unmockkAll()
        clearAllMocks()
    }

    @Test
    fun `gameRankingsByUsername Success - test the unit gameRankingsByUsername`(){
        val contextMock = mockk<Context>()
        every { contextMock.players } returns players
        Assert.assertEquals(correctList,LeaderboardState().gameRankingsByUsername(sortedHashmap))
    }

    @Test
    fun `getGameRankings Success - test the unit getGameRankings`(){
        val contextMock = mockk<Context>()
        val leaderboardMock = mockk<LeaderboardState>()
        every { contextMock.statesOfGame } returns arrayListOf(stateOfGame)
        every { leaderboardMock.sortGameRankings(unsortedGameRankings) } returns sortedRankings
        every { leaderboardMock.gameRankingsByUsername(sortedRankings) } returns userRankings
        Assert.assertEquals(userRankings,LeaderboardState().getGameRankings())
    }


    private val userRankings = listOf(UserRanking(1,"No Username",50,"PlayerB"),
            UserRanking(2,"No Username",20,"PlayerC"),
            UserRanking(3,"No Username",5,"PlayerA"))
    private val stateOfGame = StateOfGame(1).apply {
        val backlogs = HashMap<String,Int>()
        val incomingOrders = HashMap<String, Int>()
        val outgoingDeliveries = HashMap<String, Int>()
        backlogs["Test2"] = 1
        incomingOrders["Wholesale0"] = 10
        outgoingDeliveries["Wholesale0"] = 2
        playerStates = hashMapOf("PlayerA" to StateOfPlayer("PlayerA", false,"Test",backlogs,10,5,incomingOrders,14,53,outgoingDeliveries),
                "PlayerB" to StateOfPlayer("PlayerB", false,"Test",backlogs,10,50,incomingOrders,14,53,outgoingDeliveries),
                "PlayerC" to StateOfPlayer("PlayerC", false,"Test",backlogs,10,20,incomingOrders,14,53,outgoingDeliveries))
    }
    private val unsortedGameRankings : MutableList<Pair<String?, Int>> = mutableListOf(Pair("PlayerA", 5),Pair("PlayerB", 20),Pair("PlayerC", 50))
    private val sortedRankings : LinkedHashMap<String?, Int> = linkedMapOf("PlayerA" to 5, "PlayerC" to 20, "PlayerB" to 50)
    private val players = mutableListOf(Player("PlayerA", "Kevin", "", false),
            Player("PlayerB", "Koen", "", false),
            Player("PlayerC", "Cas", "", false),
            Player("PlayerD", "Jelle", "", false))
    private val sortedHashmap = LinkedHashMap<String?, Int>().apply {
        put("PlayerA", 40)
        put("PlayerB", 20)
        put("PlayerC", 10)
    }
    private val correctList = listOf(UserRanking(1, "Kevin", 40,"PlayerA"),
            UserRanking(2, "Koen", 20,"PlayerB"),
            UserRanking(3,"Cas",10,"PlayerC"))

}
