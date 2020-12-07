package com.asd2.beerdistributiongame.gameagent.ast

import java.util.*

class IfClause : ASTNode() {
    lateinit var comparison: Comparison

    override fun getChildren() = arrayListOf<ASTNode>(comparison)

    override fun addChild(child: ASTNode): ASTNode {
        if (child is Comparison)
            comparison = child
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as IfClause?
        return comparison == that!!.comparison
    }

    override fun hashCode() = Objects.hash(comparison)
}