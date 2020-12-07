package com.asd2.beerdistributiongame.gameagent.ast

import java.util.*

class Comparison(var operator: Char) : ASTNode() {
    lateinit var left: Expression
    lateinit var right: Expression

    override fun getChildren() = arrayListOf<ASTNode>(left, right)

    override fun addChild(child: ASTNode): ASTNode {
        when (child) {
            is Expression -> when {
                !::left.isInitialized -> left = child
                !::right.isInitialized -> right = child
            }
        }
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as Comparison?
        return left == that!!.left && right == that.right
    }

    override fun hashCode() = Objects.hash(left, operator, right)
}