package com.asd2.beerdistributiongame.gameagent.ast.scalar

import com.asd2.beerdistributiongame.gameagent.ast.Scalar
import java.util.*

class Number(var value: Int) : Scalar() {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as Number?
        return value == that!!.value
    }

    override fun hashCode() = Objects.hash(value)
}