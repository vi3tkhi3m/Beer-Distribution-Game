package com.asd2.beerdistributiongame.gameagent.checker

import com.asd2.beerdistributiongame.gameagent.ast.*
import com.asd2.beerdistributiongame.gameagent.ast.expression.Root
import com.asd2.beerdistributiongame.gameagent.ast.scalar.Decimal
import com.asd2.beerdistributiongame.gameagent.ast.variable.VariableAssignment
import com.asd2.beerdistributiongame.gameagent.ast.variable.VariableReference

class Checker {

    lateinit var variableTypes: HashMap<String, VariableAssignment>

    fun check(ast: AST) {
        //Checks each children node
        variableTypes = HashMap()
        checkNode(ast.root)
    }

    fun checkNode(node: ASTNode) {
        when {
            node is VariableAssignment -> checkVariables(node)
            node is VariableReference -> checkReference(node)
            node is IfClause -> checkIfClause(node)
            node is Operator -> checkOperator(node)
        }
        node.getChildren().forEach(this::checkNode)
    }

    fun checkReference(node: VariableReference) {
        if(!variableTypes.contains(node.name)){
            node.setError(String.format("Variable used but not assigned", node.name))
        }
    }

    fun checkVariables(node: VariableAssignment) {
        val nodeName = node.name

        //Check if the assignment would cause a loop
        if (node.expression is VariableReference) {
            val reference: VariableReference = node.expression as VariableReference
            if (reference.name.equals(nodeName))
                node.setError("It is not possible to assign a variable to itself")
        }

        if (!node.hasError())
            variableTypes.put(nodeName, node)
    }

    fun checkOperator(node: Expression) {
        //Check if the operator is a root
        if (node is Root) {
            //Check if root is used on a scalar type
            when {
                node.right !is Scalar -> node.setError("You can only use a root on a number or decimal")
                    //Check if root is a negative number
                node.right is Scalar -> {
                    val decimal = node.right as Decimal
                    if (decimal.value < 0)
                        node.setError("A root cant have a negative number")
                }
            }
        }
    }

    fun checkIfClause(node: IfClause) {
        //Check if left and right are not the same
        if (node.comparison.left == node.comparison.right)
            node.setError("In an alternative order the left and right values can not be the same. For a rule that has to always work use default order.")
    }
}
