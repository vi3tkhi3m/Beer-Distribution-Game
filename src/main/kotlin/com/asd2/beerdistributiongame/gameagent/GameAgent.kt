package com.asd2.beerdistributiongame.gameagent

import com.asd2.beerdistributiongame.gameagent.ast.AST

class GameAgent(val formula: String) {
    /**
     * function used to calculate the number of beer to request as an order
     * @param roleKey Key identifying which node the orders will be placed for, the key will be used to identify the correct variables in the StateOfGame
     * @return Int representing the order that will be placed for the supply chain link
     */
    fun calculateOrder(roleKey: String): Int {
        val ast = AST()
        val agentBuilder = AgentBuilder(ast)
        agentBuilder.parseString(formula)

        return agentBuilder.calculateOrder(roleKey).toInt()
    }
}