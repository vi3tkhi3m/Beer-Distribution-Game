package com.asd2.beerdistributiongame.gameagent.parser;// Generated from AgentGrammar.g4 by ANTLR 4.7.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link AgentGrammarParser}.
 */
public interface AgentGrammarListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#behaviourFormula}.
	 * @param ctx the parse tree
	 */
	void enterBehaviourFormula(AgentGrammarParser.BehaviourFormulaContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#behaviourFormula}.
	 * @param ctx the parse tree
	 */
	void exitBehaviourFormula(AgentGrammarParser.BehaviourFormulaContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#defaultOrder}.
	 * @param ctx the parse tree
	 */
	void enterDefaultOrder(AgentGrammarParser.DefaultOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#defaultOrder}.
	 * @param ctx the parse tree
	 */
	void exitDefaultOrder(AgentGrammarParser.DefaultOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#alternativeOrder}.
	 * @param ctx the parse tree
	 */
	void enterAlternativeOrder(AgentGrammarParser.AlternativeOrderContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#alternativeOrder}.
	 * @param ctx the parse tree
	 */
	void exitAlternativeOrder(AgentGrammarParser.AlternativeOrderContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#variableAssignment}.
	 * @param ctx the parse tree
	 */
	void enterVariableAssignment(AgentGrammarParser.VariableAssignmentContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#variableAssignment}.
	 * @param ctx the parse tree
	 */
	void exitVariableAssignment(AgentGrammarParser.VariableAssignmentContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#ifClause}.
	 * @param ctx the parse tree
	 */
	void enterIfClause(AgentGrammarParser.IfClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#ifClause}.
	 * @param ctx the parse tree
	 */
	void exitIfClause(AgentGrammarParser.IfClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#thenClause}.
	 * @param ctx the parse tree
	 */
	void enterThenClause(AgentGrammarParser.ThenClauseContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#thenClause}.
	 * @param ctx the parse tree
	 */
	void exitThenClause(AgentGrammarParser.ThenClauseContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#comparison}.
	 * @param ctx the parse tree
	 */
	void enterComparison(AgentGrammarParser.ComparisonContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#comparison}.
	 * @param ctx the parse tree
	 */
	void exitComparison(AgentGrammarParser.ComparisonContext ctx);
	/**
	 * Enter a parse tree produced by the {@code division}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterDivision(AgentGrammarParser.DivisionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code division}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitDivision(AgentGrammarParser.DivisionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subtraction}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubtraction(AgentGrammarParser.SubtractionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subtraction}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubtraction(AgentGrammarParser.SubtractionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code multiplication}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterMultiplication(AgentGrammarParser.MultiplicationContext ctx);
	/**
	 * Exit a parse tree produced by the {@code multiplication}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitMultiplication(AgentGrammarParser.MultiplicationContext ctx);
	/**
	 * Enter a parse tree produced by the {@code subExpression}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterSubExpression(AgentGrammarParser.SubExpressionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code subExpression}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitSubExpression(AgentGrammarParser.SubExpressionContext ctx);
	/**
	 * Enter a parse tree produced by the {@code addition}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void enterAddition(AgentGrammarParser.AdditionContext ctx);
	/**
	 * Exit a parse tree produced by the {@code addition}
	 * labeled alternative in {@link AgentGrammarParser#expression}.
	 * @param ctx the parse tree
	 */
	void exitAddition(AgentGrammarParser.AdditionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#expressionPart}.
	 * @param ctx the parse tree
	 */
	void enterExpressionPart(AgentGrammarParser.ExpressionPartContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#expressionPart}.
	 * @param ctx the parse tree
	 */
	void exitExpressionPart(AgentGrammarParser.ExpressionPartContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#priorityExpression}.
	 * @param ctx the parse tree
	 */
	void enterPriorityExpression(AgentGrammarParser.PriorityExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#priorityExpression}.
	 * @param ctx the parse tree
	 */
	void exitPriorityExpression(AgentGrammarParser.PriorityExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#exponentExpression}.
	 * @param ctx the parse tree
	 */
	void enterExponentExpression(AgentGrammarParser.ExponentExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#exponentExpression}.
	 * @param ctx the parse tree
	 */
	void exitExponentExpression(AgentGrammarParser.ExponentExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#rootExpression}.
	 * @param ctx the parse tree
	 */
	void enterRootExpression(AgentGrammarParser.RootExpressionContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#rootExpression}.
	 * @param ctx the parse tree
	 */
	void exitRootExpression(AgentGrammarParser.RootExpressionContext ctx);
	/**
	 * Enter a parse tree produced by {@link AgentGrammarParser#value}.
	 * @param ctx the parse tree
	 */
	void enterValue(AgentGrammarParser.ValueContext ctx);
	/**
	 * Exit a parse tree produced by {@link AgentGrammarParser#value}.
	 * @param ctx the parse tree
	 */
	void exitValue(AgentGrammarParser.ValueContext ctx);
	/**
	 * Enter a parse tree produced by the {@code number}
	 * labeled alternative in {@link AgentGrammarParser#scalar}.
	 * @param ctx the parse tree
	 */
	void enterNumber(AgentGrammarParser.NumberContext ctx);
	/**
	 * Exit a parse tree produced by the {@code number}
	 * labeled alternative in {@link AgentGrammarParser#scalar}.
	 * @param ctx the parse tree
	 */
	void exitNumber(AgentGrammarParser.NumberContext ctx);
	/**
	 * Enter a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link AgentGrammarParser#scalar}.
	 * @param ctx the parse tree
	 */
	void enterDecimal(AgentGrammarParser.DecimalContext ctx);
	/**
	 * Exit a parse tree produced by the {@code decimal}
	 * labeled alternative in {@link AgentGrammarParser#scalar}.
	 * @param ctx the parse tree
	 */
	void exitDecimal(AgentGrammarParser.DecimalContext ctx);
	/**
	 * Enter a parse tree produced by the {@code defaultVariable}
	 * labeled alternative in {@link AgentGrammarParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterDefaultVariable(AgentGrammarParser.DefaultVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code defaultVariable}
	 * labeled alternative in {@link AgentGrammarParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitDefaultVariable(AgentGrammarParser.DefaultVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code variableReference}
	 * labeled alternative in {@link AgentGrammarParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterVariableReference(AgentGrammarParser.VariableReferenceContext ctx);
	/**
	 * Exit a parse tree produced by the {@code variableReference}
	 * labeled alternative in {@link AgentGrammarParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitVariableReference(AgentGrammarParser.VariableReferenceContext ctx);
	/**
	 * Enter a parse tree produced by the {@code orderVariable}
	 * labeled alternative in {@link AgentGrammarParser#variable}.
	 * @param ctx the parse tree
	 */
	void enterOrderVariable(AgentGrammarParser.OrderVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code orderVariable}
	 * labeled alternative in {@link AgentGrammarParser#variable}.
	 * @param ctx the parse tree
	 */
	void exitOrderVariable(AgentGrammarParser.OrderVariableContext ctx);
}