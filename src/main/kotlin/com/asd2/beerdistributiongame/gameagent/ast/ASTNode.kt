package com.asd2.beerdistributiongame.gameagent.ast

import com.asd2.beerdistributiongame.gameagent.checker.SemanticError
import java.util.*

open class ASTNode {
    private var error: SemanticError? = null

    open fun getChildren() = ArrayList<ASTNode>()

    /*
    By implementing this method in a subclass you can easily create AST nodes
      incrementally.
    */
    open fun addChild(child: ASTNode) = this

    /*
    * By implementing this method you can easily make transformations that prune the AST.
    */
    open fun removeChild(child: ASTNode) = this

    fun getError() = this.error!!

    fun setError(description: String) {
        this.error = SemanticError(description)
    }

    fun hasError() = (error != null)

    override fun toString(): String {
        val result = StringBuilder()
        toString(result)
        return result.toString()
    }

    private fun toString(builder: StringBuilder) {
        builder.append("[")
        builder.append("|")
        for (child in getChildren()) {
            child.toString(builder)
        }
        builder.append("]")
    }

    override fun equals(o: Any?): Boolean {
        if (o !is ASTNode)
            return false
        //Compare all children
        val thisChildren = this.getChildren()
        val otherChildren = o.getChildren()
        if (otherChildren.size != thisChildren.size)
            return false
        for (i in thisChildren.indices) {
            if (thisChildren[i] != otherChildren[i]) {
                return false
            }
        }
        return true
    }

    override fun hashCode() = error?.hashCode() ?: 0
}