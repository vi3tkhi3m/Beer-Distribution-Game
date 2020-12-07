package com.asd2.beerdistributiongame.gameagent

object GameAgentCreator {
    /**
     * Creates a usable instance of Gameagent to be used for calculating orders
     * @param formula The formula used to calculate number of beer placed as the order
     * @return GameAgent that will be used to place orders for a certain node
     */
    fun createGameAgent(formula: String): GameAgent {
        return GameAgent(formula)
    }
}