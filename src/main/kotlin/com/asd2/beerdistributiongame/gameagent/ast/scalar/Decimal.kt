package com.asd2.beerdistributiongame.gameagent.ast.scalar

import com.asd2.beerdistributiongame.gameagent.ast.Scalar
import java.util.*

class Decimal(var value: Float) : Scalar() {
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as Decimal?
        return value == that!!.value
    }

    override fun hashCode() = Objects.hash(value)
}