package com.asd2.beerdistributiongame.gameagent.parser

import com.asd2.beerdistributiongame.gameagent.ast.*
import com.asd2.beerdistributiongame.gameagent.ast.expression.*
import com.asd2.beerdistributiongame.gameagent.ast.order.AlternativeOrder
import com.asd2.beerdistributiongame.gameagent.ast.order.DefaultOrder
import com.asd2.beerdistributiongame.gameagent.ast.scalar.Decimal
import com.asd2.beerdistributiongame.gameagent.ast.scalar.Number
import com.asd2.beerdistributiongame.gameagent.ast.variable.*

class Fixtures {
    companion object {
        fun testAgent1(): AST {
            val behaviourFormula = BehaviourFormula()

            val variableAssignment = VariableAssignment(
                    "@sumMaster"
            )
            variableAssignment.expression = Number(5)

            val defaultOrder = DefaultOrder()
            val multiplication = Multiplication()
            multiplication.left = VariableReference("@sumMaster")
            multiplication.right = Number(2)
            defaultOrder.expression = multiplication

            val alternativeOrder = AlternativeOrder()
            val ifClause = IfClause()
            alternativeOrder.ifClause = ifClause
            val comparison = Comparison(
                    '>'
            )
            ifClause.comparison = comparison
            comparison.left = DefaultVariable(DefaultVariableType.BACKLOG)
            comparison.right = VariableReference("@sumMaster")

            val thenClause = ThenClause()
            alternativeOrder.thenClause = thenClause
            val thenClauseExpression = VariableAssignment("@sumMaster")

            val thenClauseAddition = Addition()
            val additionAddition = Addition()
            additionAddition.left = OrderVariable()
            additionAddition.right = Number(3)
            thenClauseAddition.left = additionAddition
            thenClauseAddition.right = Number(5)
            thenClauseExpression.expression = thenClauseAddition
            thenClause.expression = thenClauseExpression

            behaviourFormula.addChild(variableAssignment)
            behaviourFormula.addChild(defaultOrder)
            behaviourFormula.addChild(alternativeOrder)
            val ast = AST()
            ast.root = behaviourFormula
            return ast
        }

        fun testAgent2(): AST {
            val behaviourFormula = BehaviourFormula()
            behaviourFormula.addChild(variable1())
            behaviourFormula.addChild(variable2())
            behaviourFormula.addChild(variable3())
            behaviourFormula.addChild(variable4())
            behaviourFormula.addChild(variable5())
            behaviourFormula.addChild(variable6())
            behaviourFormula.addChild(defaultOrder())
            behaviourFormula.addChild(alternativeOrder1())
            behaviourFormula.addChild(alternativeOrder2())
            behaviourFormula.addChild(alternativeOrder3())
            behaviourFormula.addChild(alternativeOrder4())
            behaviourFormula.addChild(alternativeOrder5())

            val ast = AST()
            ast.root = behaviourFormula
            return ast
        }

        private fun variable1(): VariableAssignment {
            val variableAssignment = VariableAssignment("@remainingStock")
            val addition = Addition()
            val subtraction1 = Subtraction()
            subtraction1.left = DefaultVariable(DefaultVariableType.STOCK)
            subtraction1.right = DefaultVariable(DefaultVariableType.INCOMING_ORDER)
            val subtraction2 = Subtraction()
            subtraction2.left = DefaultVariable(DefaultVariableType.INCOMING_DELIVERY)
            subtraction2.right = DefaultVariable(DefaultVariableType.BACKLOG)
            addition.left = subtraction1
            addition.right = subtraction2
            variableAssignment.expression = addition

            return variableAssignment
        }

        private fun variable2(): VariableAssignment {
            val variableAssignment = VariableAssignment("@var")

            val multiplication = Multiplication()
            val addition = Addition()
            val defaultVariable = DefaultVariable(DefaultVariableType.INCOMING_ORDER)
            addition.addChild(defaultVariable)
            addition.addChild(multiplication)
            variableAssignment.addChild(addition)
            multiplication.addChild(DefaultVariable(DefaultVariableType.STOCK))
            multiplication.addChild(DefaultVariable(DefaultVariableType.BACKLOG))
            return variableAssignment
        }

        private fun variable3(): VariableAssignment {
            val variableAssignment = VariableAssignment("@var")

            val exponent = Exponent()
            exponent.addChild(Number(5))
            val priority = Priority()
            val division = Division()
            priority.addChild(division)
            division.addChild(Number(1))
            division.addChild(Number(3))
            exponent.addChild(priority)

            variableAssignment.addChild(exponent)
            return variableAssignment
        }

        private fun variable4(): VariableAssignment {
            val variableAssignment = VariableAssignment("@var")

            val addition = Addition()
            addition.addChild(Decimal(7.5F))
            val multiplication = Multiplication()
            addition.addChild(multiplication)
            val exponent = Exponent()
            multiplication.addChild(exponent)
            exponent.addChild(DefaultVariable(DefaultVariableType.STOCK))
            exponent.addChild(Number(8))
            multiplication.addChild(VariableReference("@sum"))

            variableAssignment.addChild(addition)
            return variableAssignment
        }

        private fun variable5(): VariableAssignment {
            val variableAssignment = VariableAssignment("@newVar")

            val multiplication = Multiplication()
            multiplication.addChild(Number(5))
            val exponent = Exponent()
            multiplication.addChild(exponent)
            val priority = Priority()
            val subtraction = Subtraction()
            priority.addChild(subtraction)
            exponent.addChild(priority)
            subtraction.addChild(Number(7))
            subtraction.addChild(DefaultVariable(DefaultVariableType.STOCK))
            exponent.addChild(DefaultVariable(DefaultVariableType.INCOMING_ORDER))

            variableAssignment.addChild(multiplication)
            return variableAssignment
        }

        private fun variable6(): VariableAssignment {
            val variableAssignment = VariableAssignment("@newVar")

            val subtraction = Subtraction()
            val division = Division()
            subtraction.addChild(division)
            division.addChild(DefaultVariable(DefaultVariableType.BUDGET))
            division.addChild(DefaultVariable(DefaultVariableType.ORDER_COST))
            subtraction.addChild(OrderVariable())

            variableAssignment.addChild(subtraction)
            return variableAssignment
        }

        private fun defaultOrder(): DefaultOrder {
            val defaultOrder = DefaultOrder()
            val subtraction = Subtraction()

            val subtraction2 = Subtraction()
            subtraction.left = subtraction2
            subtraction2.left = VariableReference("@remainingStock")
            val exponent = Exponent()
            exponent.left = Number(3)
            exponent.right = Number(5)
            subtraction2.right = exponent

            val root = Root()
            val multiplication = Multiplication()
            multiplication.left = Number(4)
            multiplication.right = root
            subtraction.right = multiplication

            val priority = Priority()
            root.right = priority

            val addition = Addition()
            priority.expression = addition
            addition.left = Number(5)
            addition.right = DefaultVariable(DefaultVariableType.BACKLOG)

            defaultOrder.expression = subtraction

            return defaultOrder
        }

        private fun alternativeOrder1(): AlternativeOrder {
            val alternativeOrder = AlternativeOrder()

            val ifClause = IfClause()
            val comparison = Comparison('<')
            ifClause.addChild(comparison)
            comparison.addChild(OrderVariable())
            comparison.addChild(Number(5))
            val thenClause = ThenClause(true)
            val addition = Addition()
            thenClause.addChild(addition)
            addition.addChild(OrderVariable())
            addition.addChild(VariableReference("@remainingStock"))

            alternativeOrder.addChild(ifClause)
            alternativeOrder.addChild(thenClause)
            return alternativeOrder
        }

        private fun alternativeOrder2(): AlternativeOrder {
            val alternativeOrder = AlternativeOrder()

            val ifClause = IfClause()
            val comparison = Comparison('<')
            ifClause.addChild(comparison)
            comparison.addChild(VariableReference("@remainingStock"))
            comparison.addChild(Number(10))
            val thenClause = ThenClause()
            val variableAssignment = VariableAssignment("@remainingStock")
            thenClause.addChild(variableAssignment)
            val addition = Addition()
            variableAssignment.addChild(addition)
            addition.addChild(VariableReference("@remainingStock"))
            addition.addChild(Number(10))

            alternativeOrder.addChild(ifClause)
            alternativeOrder.addChild(thenClause)
            return alternativeOrder
        }

        private fun alternativeOrder3(): AlternativeOrder {
            val alternativeOrder = AlternativeOrder()

            val ifClause = IfClause()
            val comparison = Comparison('>')
            ifClause.addChild(comparison)

            comparison.addChild(VariableReference("@var"))
            val addition = Addition()
            comparison.addChild(addition)
            addition.addChild(DefaultVariable(DefaultVariableType.INCOMING_ORDER))
            val multiplication = Multiplication()
            addition.addChild(multiplication)
            multiplication.addChild(DefaultVariable(DefaultVariableType.BACKLOG))
            val root = Root()
            multiplication.addChild(root)
            val priority = Priority()
            root.addChild(priority)
            val subtraction = Subtraction()
            priority.addChild(subtraction)
            subtraction.addChild(Number(2))
            val exponent = Exponent()
            subtraction.addChild(exponent)
            exponent.addChild(Number(10))
            exponent.addChild(Number(3))

            val thenClause = ThenClause()
            val variableAssignment = VariableAssignment("@varNew")
            thenClause.addChild(variableAssignment)
            val addition2 = Addition()
            variableAssignment.addChild(addition2)
            val multiplication2 = Multiplication()
            addition2.addChild(multiplication2)
            multiplication2.addChild(Number(5))
            val exponent2 = Exponent()
            multiplication2.addChild(exponent2)
            val priority2 = Priority()
            exponent2.addChild(priority2)
            val subtraction2 = Subtraction()
            priority2.addChild(subtraction2)
            subtraction2.addChild(Number(7))
            subtraction2.addChild(DefaultVariable(DefaultVariableType.STOCK))
            exponent2.addChild(DefaultVariable(DefaultVariableType.INCOMING_ORDER))

            val division = Division()
            addition2.addChild(division)
            division.addChild(Number(7))
            division.addChild(DefaultVariable(DefaultVariableType.BACKLOG))

            alternativeOrder.addChild(ifClause)
            alternativeOrder.addChild(thenClause)
            return alternativeOrder
        }

        private fun alternativeOrder4(): AlternativeOrder {
            val alternativeOrder = AlternativeOrder()

            val ifClause = IfClause()
            val comparison = Comparison('<')
            ifClause.addChild(comparison)
            val addition = Addition()
            comparison.addChild(addition)
            val exponent = Exponent()
            addition.addChild(exponent)
            exponent.addChild(DefaultVariable(DefaultVariableType.STOCK))
            exponent.addChild(Number(3))
            addition.addChild(VariableReference("@newVar"))

            val addition2 = Addition()
            comparison.addChild(addition2)
            val multiplication2 = Multiplication()
            addition2.addChild(multiplication2)
            multiplication2.addChild(DefaultVariable(DefaultVariableType.BACKLOG))
            val root = Root()
            multiplication2.addChild(root)
            root.addChild(Number(2))
            val exponent2 = Exponent()
            addition2.addChild(exponent2)
            exponent2.addChild(Number(10))
            exponent2.addChild(Number(3))

            val thenClause = ThenClause(true)
            val subtraction = Subtraction()
            thenClause.addChild(subtraction)
            subtraction.addChild(Number(6))
            val exponent3 = Exponent()
            subtraction.addChild(exponent3)
            exponent3.addChild(DefaultVariable(DefaultVariableType.INCOMING_DELIVERY))
            val priority = Priority()
            exponent3.addChild(priority)
            val division2 = Division()
            priority.addChild(division2)
            division2.addChild(Number(1))
            division2.addChild(DefaultVariable(DefaultVariableType.STOCK_COST))

            alternativeOrder.addChild(ifClause)
            alternativeOrder.addChild(thenClause)
            return alternativeOrder
        }

        private fun alternativeOrder5(): AlternativeOrder {
            val alternativeOrder = AlternativeOrder()

            val ifClause = IfClause()
            val comparison = Comparison('=')
            ifClause.addChild(comparison)
            comparison.addChild(DefaultVariable(DefaultVariableType.STOCK))
            val addition = Addition()
            comparison.addChild(addition)
            addition.addChild(Number(7))
            val multiplication = Multiplication()
            addition.addChild(multiplication)
            val exponent = Exponent()
            multiplication.addChild(exponent)
            exponent.addChild(DefaultVariable(DefaultVariableType.STOCK))
            exponent.addChild(Number(8))
            multiplication.addChild(VariableReference("@sum"))

            val thenClause = ThenClause()
            val variableAssignment = VariableAssignment("@newOrder")
            thenClause.addChild(variableAssignment)
            val multiplication2 = Multiplication()
            variableAssignment.addChild(multiplication2)
            val priority = Priority()
            multiplication2.addChild(priority)
            val subtraction = Subtraction()
            priority.addChild(subtraction)
            subtraction.addChild(OrderVariable())
            val division3 = Division()
            subtraction.addChild(division3)
            val priority2 = Priority()
            division3.addChild(priority2)
            val subtraction2 = Subtraction()
            priority2.addChild(subtraction2)
            subtraction2.addChild(DefaultVariable(DefaultVariableType.BACKLOG))
            subtraction2.addChild(DefaultVariable(DefaultVariableType.ORDER_COST))
            division3.addChild(DefaultVariable(DefaultVariableType.ORDER_COST))
            multiplication2.addChild(VariableReference("@oldOrder"))

            alternativeOrder.addChild(ifClause)
            alternativeOrder.addChild(thenClause)
            return alternativeOrder
        }
    }
}