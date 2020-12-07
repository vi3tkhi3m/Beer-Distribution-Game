package com.asd2.beerdistributiongame.gameagent.Behaviour

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gameagent.ast.*
import com.asd2.beerdistributiongame.gameagent.ast.expression.Priority
import com.asd2.beerdistributiongame.gameagent.ast.expression.Root
import com.asd2.beerdistributiongame.gameagent.ast.order.AlternativeOrder
import com.asd2.beerdistributiongame.gameagent.ast.order.DefaultOrder
import com.asd2.beerdistributiongame.gameagent.ast.scalar.Decimal
import com.asd2.beerdistributiongame.gameagent.ast.scalar.Number
import com.asd2.beerdistributiongame.gameagent.ast.variable.DefaultVariable
import com.asd2.beerdistributiongame.gameagent.ast.variable.OrderVariable
import com.asd2.beerdistributiongame.gameagent.ast.variable.VariableAssignment
import com.asd2.beerdistributiongame.gameagent.ast.variable.VariableReference
import com.asd2.beerdistributiongame.gameagent.ast.variable.*
import com.asd2.beerdistributiongame.gamelogic.game.gameflow.StateOfPlayer
import kotlin.collections.HashMap

class RuleCalculator {
    private lateinit var assignedVariables: HashMap<String, PersonalVariable>
    private var defaultVariables: HashMap<String, Int> = HashMap()
    var order: DefaultOrder = DefaultOrder()
    private var alternativeOrderCompleted: Boolean = false

    fun calculateOrder(ast: AST, roleKey: String) {
        assignedVariables = HashMap()
        retrieveDefaultVariables(roleKey)
        walkTree(ast.root)
        determineOrder()
    }

    private fun determineOrder() {
        if (!order.reassigned)
            order.value = getExpressionValue(order.expression)
        if (order.value < 0)
            order.value = 0.0f
    }

    private fun retrieveDefaultVariables(roleKey: String) {
        val playerState = Context.statesOfGame.last().playerStates[roleKey]

        val backlogSum = getValuesSum(playerState!!.backlogs)
        val incomingOrderSum = getValuesSum(playerState.incomingOrders)

        defaultVariables["StockCost"] = Context.gameConfig.stockCost
        defaultVariables["BacklogCost"] = Context.gameConfig.backlogCost
        defaultVariables["ProductionCost"] = Context.gameConfig.productionCost
        defaultVariables["Stock"] = playerState.stock
        defaultVariables["Budget"] = playerState.budget
        defaultVariables["Backlog"] = backlogSum
        defaultVariables["IncomingOrder"] = incomingOrderSum
        defaultVariables["IncomingDelivery"] = playerState.incomingDelivery
    }

    private fun getValuesSum(map: HashMap<String, Int>): Int {
        var sum = 0
        for (value in map.values) {
            sum += value
        }
        return sum
    }

    private fun walkTree(node: ASTNode) {
        if (alternativeOrderCompleted) return
        when (node) {
            is VariableAssignment -> createVariable(node)
            is DefaultOrder -> setDefaultOrder(node)
            is AlternativeOrder -> checkAlternativeOrder(node)
        }

        for (child in node.getChildren()) {
            walkTree(child)
        }
    }

    private fun createVariable(child: VariableAssignment) {
        val value = getExpressionValue(child.expression)

        if (assignedVariables[child.name] == null) {
            val personalVariable = PersonalVariable(child.name, child.expression)
            assignedVariables[child.name] = personalVariable
            personalVariable.value = value
        } else {
            assignedVariables[child.name]!!.value = value
            assignedVariables[child.name]!!.expression = child.expression
        }
    }

    private fun checkAlternativeOrder(child: AlternativeOrder) {
        if (alternativeOrderCompleted) return
        if (isComparisonTrue(child.ifClause.comparison)) {
            alternativeOrderCompleted = true
            if (child.thenClause.hasOrderAssignment) {
                order.value = getExpressionValue(child.thenClause.expression)
                order.reassigned = true
            } else if (child.thenClause.expression is VariableAssignment) {
                createVariable(child.thenClause.expression as VariableAssignment)
            }
        }

    }

    private fun isComparisonTrue(comparison: Comparison): Boolean {
        when (comparison.operator) {
            '>' -> if (getExpressionValue(comparison.left) > getExpressionValue(comparison.right))
                return true

            '<' -> if (getExpressionValue(comparison.left) < getExpressionValue(comparison.right))
                return true

            '=' -> if (getExpressionValue(comparison.left) == getExpressionValue(comparison.right))
                return true
        }
        return false
    }

    private fun setDefaultOrder(child: DefaultOrder) {
        order = child
        order.value = getExpressionValue(child.expression)
    }

    private fun getExpressionValue(expression: Expression): Float {
        when (expression) {
            is Priority -> return getExpressionValue(expression.expression)
            is Operator -> return execOperation(expression)
            is Root -> return execRootOperation(expression)
            is Scalar -> return getScalarValue(expression)
            is Variable -> return getVariableValue(expression)
            is VariableReference -> {
                assignedVariables[expression.name]!!.value = getExpressionValue(assignedVariables[expression.name]!!.expression)
                return getExpressionValue(assignedVariables[expression.name]!!.expression)
            }
        }
        return 0.0F
    }

    private fun execRootOperation(expression: Root) = expression.doOperation(getExpressionValue(expression.right))

    private fun execOperation(operator: Operator): Float {
        val left: Float = getExpressionValue(operator.left)
        val right: Float = getExpressionValue(operator.right)

        return operator.doOperation(left, right)
    }

    private fun getScalarValue(scalar: Scalar): Float {
        when (scalar) {
            is Number -> return scalar.value.toFloat()
            is Decimal -> return scalar.value
        }
        return 0.0F
    }

    private fun getDefaultVarValue(expression: DefaultVariable) = defaultVariables[expression.type.variableName]!!.toFloat()

    private fun getVariableValue(expression: Variable): Float {
        when (expression) {
            is DefaultVariable -> return getDefaultVarValue(expression)
            is OrderVariable -> return getExpressionValue(order.expression)
        }
        return 0.0F
    }
}