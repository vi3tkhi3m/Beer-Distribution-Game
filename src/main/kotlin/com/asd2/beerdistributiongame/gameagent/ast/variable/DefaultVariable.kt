package com.asd2.beerdistributiongame.gameagent.ast.variable

import com.asd2.beerdistributiongame.gameagent.ast.Variable
import java.util.*

class DefaultVariable(var type: DefaultVariableType) : Variable() {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as DefaultVariable?
        return type == that!!.type
    }

    override fun hashCode(): Int {
        return Objects.hash(type)
    }
}