package com.asd2.beerdistributiongame.gameagent.ast.expression

import com.asd2.beerdistributiongame.gameagent.ast.ASTNode
import com.asd2.beerdistributiongame.gameagent.ast.Expression
import java.util.*

class Priority : Expression() {
    lateinit var expression: Expression

    override fun getChildren() = arrayListOf<ASTNode>(expression)

    override fun addChild(child: ASTNode): ASTNode {
        when (child) {
            is Expression -> expression = child
        }
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as Priority?
        return expression == that!!.expression
    }

    override fun hashCode() = Objects.hash(expression)
}