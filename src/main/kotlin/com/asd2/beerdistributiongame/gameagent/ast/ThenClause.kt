package com.asd2.beerdistributiongame.gameagent.ast

import java.util.*

class ThenClause(var hasOrderAssignment: Boolean = false) : ASTNode() {
    lateinit var expression: Expression

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
        val that = o as ThenClause?
        return hasOrderAssignment == that!!.hasOrderAssignment && expression == that.expression
    }

    override fun hashCode() = Objects.hash(hasOrderAssignment, expression)
}