package com.asd2.beerdistributiongame.gameagent

import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.io.File
import java.io.InputStream

@RunWith(JUnit4::class)
class StorageManagerIntegrationTest {

    @Test
    fun createAgentIntegrationTest() {
        val filename = "testAgent"
        val rules = "backlog + 20"
        val fileLocation = "data/gameagents/testAgent"
        StorageManager.createAgent(filename, rules)
        val inputStream: InputStream = File("data/gameagents/testAgent").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        val testFile = File(fileLocation)
        testFile.deleteRecursively()

        Assert.assertEquals(rules, inputString)
    }

    @Test
    fun editAgentIntegrationTest() {
        val fileLocation = "data/gameagents/testAgent"
        val testFile = File(fileLocation)
        val intialTestRules = "backlog + 20"
        val editedTestRules = "backlog + 30"
        testFile.printWriter().use { out -> out.println(intialTestRules) }
        StorageManager.editAgent("testAgent", editedTestRules)
        val inputStream: InputStream = File("data/gameagents/testAgent").inputStream()
        val inputString = inputStream.bufferedReader().use { it.readText() }
        testFile.deleteRecursively()

        Assert.assertEquals(editedTestRules, inputString)
    }

    @Test
    fun agentExistsIntegrationTest() {
        val fileLocation = "data/gameagents/testAgent"
        val testFile = File(fileLocation)
        testFile.printWriter().use { out -> out.println("test") }
        val expected = testFile.exists()
        val actual = StorageManager.agentExists("testAgent")
        testFile.deleteRecursively()

        Assert.assertEquals(expected, actual)
    }

    @Test
    fun getAgentNamesIntegrationTest() {
        var testList: MutableList<String> = mutableListOf()
        File("data/gameagents/").walkBottomUp().forEach {
            if (it.name != ".gitignore") {
                testList.add(it.name)
            }
        }
        testList.remove("gameagents")
        var i = 0

        while (i <= testList.size - 1) {
            Assert.assertEquals(testList.get(i), StorageManager.getAgentNames().get(i))
            i++
        }
    }

    @Test
    fun deleteAgentIntegrationTest() {
        val fileLocation = "data/gameagents/deleteTestAgent"
        val testFile = File(fileLocation)
        testFile.printWriter().use { out -> out.println("test") }
        StorageManager.deleteAgent("deleteTestAgent")

        Assert.assertEquals(false, testFile.exists())
    }

    @Test
    fun getRulesIntegrationTest() {
        val fileLocation = "data/gameagents/testAgent"
        val testFile = File(fileLocation)
        val testRules = "testRulesForATestAgent1231243532696968745232"
        testFile.printWriter().use { out -> out.print(testRules) }
        val expected = testRules
        val actual = StorageManager.getRules("testAgent")
        testFile.deleteRecursively()
        
        Assert.assertEquals(expected,actual)
    }
}