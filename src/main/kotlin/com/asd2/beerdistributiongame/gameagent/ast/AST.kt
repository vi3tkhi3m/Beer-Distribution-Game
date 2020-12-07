package com.asd2.beerdistributiongame.gameagent.ast

import com.asd2.beerdistributiongame.gameagent.checker.SemanticError
import java.util.*

class AST(var root: BehaviourFormula = BehaviourFormula()) {

    val errors: ArrayList<SemanticError>
        get() {
            val errors = ArrayList<SemanticError>()
            collectErrors(errors, root)
            return errors
        }

    private fun collectErrors(errors: ArrayList<SemanticError>, node: ASTNode) {
        if (node.hasError()) {
            errors.add(node.getError())
        }
        for (child in node.getChildren()) {
            collectErrors(errors, child)
        }
    }

    override fun toString() = root.toString()

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val ast = o as AST?
        return root == ast!!.root
    }

    override fun hashCode() = Objects.hash(root)
}