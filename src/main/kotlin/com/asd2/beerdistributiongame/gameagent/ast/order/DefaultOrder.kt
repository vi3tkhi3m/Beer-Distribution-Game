package com.asd2.beerdistributiongame.gameagent.ast.order

import com.asd2.beerdistributiongame.gameagent.ast.ASTNode
import com.asd2.beerdistributiongame.gameagent.ast.Expression
import java.util.*

class DefaultOrder : ASTNode() {
    lateinit var expression: Expression
    var value: Float = 0.0f
    var reassigned: Boolean = false

    override fun getChildren() = arrayListOf<ASTNode>(expression)

    override fun addChild(child: ASTNode): ASTNode {
        if (child is Expression)
            expression = child
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as DefaultOrder?
        return expression == that!!.expression
    }

    override fun hashCode() = Objects.hash(expression)

    override fun toString(): String = "NewOrder"
}