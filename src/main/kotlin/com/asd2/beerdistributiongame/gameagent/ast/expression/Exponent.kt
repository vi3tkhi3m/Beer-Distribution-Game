package com.asd2.beerdistributiongame.gameagent.ast.expression

import com.asd2.beerdistributiongame.gameagent.ast.Operator
import java.util.*
import kotlin.math.pow

class Exponent : Operator() {

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as Exponent?
        return left == that!!.left && right == that.right
    }

    override fun hashCode() = Objects.hash(left, right)

    override fun doOperation(left: Float, right: Float) = left.pow(right)
}