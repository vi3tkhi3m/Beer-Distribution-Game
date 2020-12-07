package com.asd2.beerdistributiongame.gameagent.parser

import com.asd2.beerdistributiongame.gameagent.ast.*
import com.asd2.beerdistributiongame.gameagent.ast.expression.*
import com.asd2.beerdistributiongame.gameagent.ast.order.AlternativeOrder
import com.asd2.beerdistributiongame.gameagent.ast.order.DefaultOrder
import com.asd2.beerdistributiongame.gameagent.ast.scalar.Decimal
import com.asd2.beerdistributiongame.gameagent.ast.scalar.Number
import com.asd2.beerdistributiongame.gameagent.ast.variable.*
import java.util.*

class ASTListener : AgentGrammarBaseListener() {
    val ast = AST()
    private val currentContainer: Stack<ASTNode> = Stack()

    override fun enterBehaviourFormula(ctx: AgentGrammarParser.BehaviourFormulaContext) {
        val behaviourFormula = BehaviourFormula()
        ast.root = behaviourFormula
        currentContainer.push(behaviourFormula)
    }

    override fun enterVariableAssignment(ctx: AgentGrammarParser.VariableAssignmentContext) {
        val variableAssignment = VariableAssignment(ctx.children[0].text)
        addChild(variableAssignment)
        currentContainer.push(variableAssignment)
    }

    override fun exitVariableAssignment(ctx: AgentGrammarParser.VariableAssignmentContext) {
        popContainer()
    }

    override fun enterDefaultOrder(ctx: AgentGrammarParser.DefaultOrderContext) {
        val defaultOrder = DefaultOrder()
        addChild(defaultOrder)
        currentContainer.push(defaultOrder)
    }

    override fun exitDefaultOrder(ctx: AgentGrammarParser.DefaultOrderContext) {
        popContainer()
    }

    override fun enterAlternativeOrder(ctx: AgentGrammarParser.AlternativeOrderContext) {
        val alternativeOrder = AlternativeOrder()
        addChild(alternativeOrder)
        currentContainer.push(alternativeOrder)
    }

    override fun exitAlternativeOrder(ctx: AgentGrammarParser.AlternativeOrderContext) {
        popContainer()
    }

    override fun enterNumber(ctx: AgentGrammarParser.NumberContext) {
        val number = Number(Integer.parseInt(ctx.children[0].text))
        addChild(number)
    }

    override fun enterDecimal(ctx: AgentGrammarParser.DecimalContext) {
        val decimal = Decimal(ctx.children[0].text.toFloat())
        addChild(decimal)
    }

    override fun enterExponentExpression(ctx: AgentGrammarParser.ExponentExpressionContext) {
        val exponentExpression = Exponent()
        addChild(exponentExpression)
        currentContainer.push(exponentExpression)
    }

    override fun exitExponentExpression(ctx: AgentGrammarParser.ExponentExpressionContext) {
        popContainer()
    }

    override fun enterPriorityExpression(ctx: AgentGrammarParser.PriorityExpressionContext) {
        val priority = Priority()
        addChild(priority)
        currentContainer.push(priority)
    }

    override fun exitPriorityExpression(ctx: AgentGrammarParser.PriorityExpressionContext) {
        popContainer()
    }

    override fun enterRootExpression(ctx: AgentGrammarParser.RootExpressionContext) {
        val root = Root()
        addChild(root)
        currentContainer.push(root)
    }

    override fun exitRootExpression(ctx: AgentGrammarParser.RootExpressionContext) {
        popContainer()
    }

    override fun enterMultiplication(ctx: AgentGrammarParser.MultiplicationContext) {
        val multiplication = Multiplication()
        addChild(multiplication)
        currentContainer.push(multiplication)
    }

    override fun exitMultiplication(ctx: AgentGrammarParser.MultiplicationContext) {
        popContainer()
    }

    override fun enterAddition(ctx: AgentGrammarParser.AdditionContext) {
        val addition = Addition()
        addChild(addition)
        currentContainer.push(addition)
    }

    override fun exitAddition(ctx: AgentGrammarParser.AdditionContext) {
        popContainer()
    }

    override fun enterDivision(ctx: AgentGrammarParser.DivisionContext) {
        val division = Division()
        addChild(division)
        currentContainer.push(division)
    }

    override fun exitDivision(ctx: AgentGrammarParser.DivisionContext) {
        popContainer()
    }

    override fun enterSubtraction(ctx: AgentGrammarParser.SubtractionContext) {
        val subtraction = Subtraction()
        addChild(subtraction)
        currentContainer.push(subtraction)
    }

    override fun exitSubtraction(ctx: AgentGrammarParser.SubtractionContext) {
        popContainer()
    }

    override fun enterVariableReference(ctx: AgentGrammarParser.VariableReferenceContext) {
        val variableReference = VariableReference(ctx.children[0].text)
        addChild(variableReference)
    }

    override fun enterIfClause(ctx: AgentGrammarParser.IfClauseContext) {
        val ifClause = IfClause()
        addChild(ifClause)
        currentContainer.push(ifClause)
    }

    override fun exitIfClause(ctx: AgentGrammarParser.IfClauseContext) {
        popContainer()
    }

    override fun enterThenClause(ctx: AgentGrammarParser.ThenClauseContext) {
        val thenClause = ThenClause(ctx.children[2].text == "Order")
        addChild(thenClause)
        currentContainer.push(thenClause)
    }

    override fun exitThenClause(ctx: AgentGrammarParser.ThenClauseContext) {
        popContainer()
    }

    override fun enterComparison(ctx: AgentGrammarParser.ComparisonContext) {
        val comparison = Comparison(ctx.children[1].text.toCharArray()[0])
        addChild(comparison)
        currentContainer.push(comparison)
    }

    override fun exitComparison(ctx: AgentGrammarParser.ComparisonContext) {
        popContainer()
    }

    override fun enterDefaultVariable(ctx: AgentGrammarParser.DefaultVariableContext) {
        val symbol = ctx.children[0].text
        var defaultVariableType: DefaultVariableType? = null

        DefaultVariableType.values().forEach { enum ->
            if (enum.variableName == symbol) {
                defaultVariableType = enum
            }
        }

        val defaultVariable = DefaultVariable(type = defaultVariableType!!)
        addChild(defaultVariable)
    }

    override fun enterOrderVariable(ctx: AgentGrammarParser.OrderVariableContext) {
        val orderVariable = OrderVariable()
        addChild(orderVariable)
    }

    private fun addChild(node: ASTNode) {
        if (!currentContainer.empty())
            currentContainer.peek().addChild(node)
    }

    private fun popContainer() {
        if (!currentContainer.empty())
            currentContainer.pop()
    }
}