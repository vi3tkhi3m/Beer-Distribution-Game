package com.asd2.beerdistributiongame.gameagent.ast.variable

import com.asd2.beerdistributiongame.gameagent.ast.Expression
import java.util.*

class VariableReference(var name: String) : Expression() {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as VariableReference?
        return name == that!!.name
    }

    override fun hashCode(): Int {
        return Objects.hash(name)
    }
}