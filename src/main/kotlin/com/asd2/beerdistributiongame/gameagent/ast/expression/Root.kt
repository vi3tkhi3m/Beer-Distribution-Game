package com.asd2.beerdistributiongame.gameagent.ast.expression

import com.asd2.beerdistributiongame.gameagent.ast.ASTNode
import com.asd2.beerdistributiongame.gameagent.ast.Expression
import java.util.*
import kotlin.math.sqrt

class Root : Expression() {
    lateinit var right: Expression

    override fun getChildren() = arrayListOf<ASTNode>(right)

    override fun addChild(child: ASTNode): ASTNode {
        when (child) {
            is Expression -> right = child
        }
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as Root?
        return right == that!!.right
    }

    override fun hashCode() = Objects.hash(right)

    fun doOperation(right: Float) = sqrt(right)
}