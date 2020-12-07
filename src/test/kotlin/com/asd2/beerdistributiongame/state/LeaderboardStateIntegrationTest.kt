package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.replay.UserRanking
import mapJSONFile
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class LeaderboardStateIntegrationTest {

    @Before
    fun before() {
        Context.players.clear()
        Context.statesOfGame.clear()
        Context.statesOfGame = mapJSONFile("./src/test/resources/replaylobbysavedgames/valid/asdfux32-1234-4651-b6e6-sdkalske2310/turns.json")
        Context.players.add(Player("asdfux32-1234-4651-b6e6-sdkalske2310", "Cas", "", false))
        Context.players.add(Player("asdfux32-7654-1675-b6e6-sdkalske2310", "Kevin", "", false))
        Context.players.add(Player("oiutgx32-1973-4826-fdse-sdkalske0512", "Jelle", "", false))
        Context.players.add(Player("oiutgx32-5678-1234-fdse-sdkalske0512", "Koen", "", false))

    }

    @After
    fun after(){
        Context.players.clear()
        Context.statesOfGame.clear()
    }

    @Test
    fun `getGameRankings Success - get rankings from context`(){
        val userRankings = listOf(UserRanking(1,"Jelle",200,"oiutgx32-1973-4826-fdse-sdkalske0512"),
                UserRanking(2,"Cas",32,"asdfux32-1234-4651-b6e6-sdkalske2310"),
                UserRanking(3,"Kevin",14,"asdfux32-7654-1675-b6e6-sdkalske2310"),
                UserRanking(4,"Koen",10,"oiutgx32-5678-1234-fdse-sdkalske0512"))
        Assert.assertEquals(userRankings,LeaderboardState().getGameRankings())
    }

    @Test
    fun `sortGameRankings Success - sort unsortedHashmap`() {
        val unsortedHashmap : MutableList<Pair<String?, Int>> = mutableListOf(Pair("testid", 40),Pair("testid2", 20),Pair("testid3", 80))
        val correctList = listOf(Pair("testid3", 80), Pair("testid", 40), Pair("testid2", 20))
        Assert.assertEquals(correctList, LeaderboardState().sortGameRankings(unsortedHashmap).toList())
    }

    @Test
    fun `sortGameRankings Success - sort unsortedHashmap with users having same points`(){
        val unsortedHashmap : MutableList<Pair<String?, Int>> = mutableListOf(Pair("testid4", 40),Pair("testid2", 20),Pair("testid3", 80),Pair("testid", 40))
        val correctList = listOf(Pair("testid3", 80), Pair("testid", 40), Pair("testid4", 40), Pair("testid2", 20))
        Assert.assertEquals(correctList, LeaderboardState().sortGameRankings(unsortedHashmap).toList())
    }

    @Test
    fun `gameRankingsByUsername Success - get game rankings with username`() {
        val sortedHashmap = LinkedHashMap<String?, Int>().apply {
            put("asdfux32-7654-1675-b6e6-sdkalske2310", 4)
            put("oiutgx32-5678-1234-fdse-sdkalske0512", 2)
        }
        val correctList = listOf(UserRanking(1, "Kevin", 4,"asdfux32-7654-1675-b6e6-sdkalske2310"),
                UserRanking(2, "Koen", 2,"oiutgx32-5678-1234-fdse-sdkalske0512"))
        Assert.assertEquals(correctList, LeaderboardState().gameRankingsByUsername(sortedHashmap))
    }

    @Test
    fun `gameRankingsByUsername Success - get game rankings with username where users have same points`() {
        val sortedHashmap = LinkedHashMap<String?, Int>().apply {
            put("asdfux32-7654-1675-b6e6-sdkalske2310", 4)
            put("oiutgx32-5678-1234-fdse-sdkalske0512", 4)
            put("asdfux32-1234-4651-b6e6-sdkalske2310", 2)
        }
        val correctList = listOf(UserRanking(1, "Kevin", 4,"asdfux32-7654-1675-b6e6-sdkalske2310"),
                UserRanking(1, "Koen", 4,"oiutgx32-5678-1234-fdse-sdkalske0512"),
                UserRanking(2, "Cas", 2, "asdfux32-1234-4651-b6e6-sdkalske2310"))
        Assert.assertEquals(correctList, LeaderboardState().gameRankingsByUsername(sortedHashmap))
    }

}