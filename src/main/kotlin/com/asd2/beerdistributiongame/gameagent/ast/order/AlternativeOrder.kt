package com.asd2.beerdistributiongame.gameagent.ast.order

import com.asd2.beerdistributiongame.gameagent.ast.ASTNode
import com.asd2.beerdistributiongame.gameagent.ast.IfClause
import com.asd2.beerdistributiongame.gameagent.ast.ThenClause
import java.util.*

class AlternativeOrder : ASTNode() {
    lateinit var ifClause: IfClause
    lateinit var thenClause: ThenClause

    override fun getChildren() = arrayListOf(ifClause, thenClause)


    override fun addChild(child: ASTNode): ASTNode {
        when (child) {
            is IfClause -> ifClause = child
            is ThenClause -> thenClause = child
        }
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as AlternativeOrder?
        return ifClause == that!!.ifClause && thenClause == that.thenClause
    }

    override fun hashCode() = Objects.hash(ifClause, thenClause)
}