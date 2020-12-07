package com.asd2.beerdistributiongame.gameagent.transforms

import com.asd2.beerdistributiongame.gameagent.ast.AST

interface Transform {
    fun apply(ast: AST)
}