package com.asd2.beerdistributiongame.gameagent.ast

abstract class Operator : Expression() {
    open lateinit var left: Expression
    open lateinit var right: Expression

    override fun getChildren() = arrayListOf<ASTNode>(left, right)

    override fun addChild(child: ASTNode): ASTNode {
        when (child) {
            is Expression -> when {
                !::left.isInitialized -> left = child
                !::right.isInitialized -> right = child
            }
        }
        return this
    }

    abstract fun doOperation(left: Float, right: Float) : Float
}