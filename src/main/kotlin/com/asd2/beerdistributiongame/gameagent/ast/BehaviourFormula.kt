package com.asd2.beerdistributiongame.gameagent.ast

import java.util.*

class BehaviourFormula : ASTNode() {
    private var body: ArrayList<ASTNode> = ArrayList()

    override fun getChildren() = body

    override fun addChild(child: ASTNode): ASTNode {
        body.add(child)
        return this
    }

    override fun removeChild(child: ASTNode): ASTNode {
        body.remove(child)
        return this
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || this.javaClass !== o.javaClass) return false
        if (!super.equals(o)) return false
        val that = o as BehaviourFormula?
        return body == that!!.body
    }

    override fun hashCode() = Objects.hash(body)
}