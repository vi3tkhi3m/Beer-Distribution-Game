package com.asd2.beerdistributiongame.gameagent.ast.variable

import com.asd2.beerdistributiongame.gameagent.ast.ASTNode
import com.asd2.beerdistributiongame.gameagent.ast.Expression
import java.util.*
import kotlin.collections.ArrayList

class VariableAssignment(var name: String) : Expression() {
    lateinit var expression: Expression

    override fun getChildren(): ArrayList<ASTNode> {
        return arrayListOf(expression)
    }

    override fun addChild(child: ASTNode): ASTNode {
        expression = child as Expression

        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as VariableAssignment?
        return name == that!!.name && expression == that.expression
    }

    override fun hashCode(): Int {
        return Objects.hash(name, expression)
    }
}