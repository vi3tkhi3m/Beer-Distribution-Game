package com.asd2.beerdistributiongame.gameagent.parser

import com.asd2.beerdistributiongame.gameagent.AgentBuilder
import com.asd2.beerdistributiongame.gameagent.ast.AST
import org.junit.Assert
import org.junit.Test
import java.io.InputStream
import java.nio.charset.Charset

class ParserListenerTest {
    private fun parseTestFile(resource: String): AST {
        val ast = AST()
        val agentBuilder = AgentBuilder(ast)
        val inputStream = this.javaClass.classLoader.getResourceAsStream(resource)
        fun InputStream.readTextAndClose(charset: Charset = Charsets.UTF_8): String {
            return this.bufferedReader(charset).use { it.readText() }
        }
        val inputAsString = inputStream.readTextAndClose()
        agentBuilder.parseString(inputAsString)

        return agentBuilder.ast
    }

    @Test
    fun testAgent1() {
        val parsed = parseTestFile("testagents/testagent1")
        val expected = Fixtures.testAgent1()
        Assert.assertEquals(expected, parsed)
    }

    @Test
    fun testAgent2() {
        val parsed = parseTestFile("testagents/testagent2")
        val expected = Fixtures.testAgent2()
        Assert.assertEquals(expected, parsed)
    }
}