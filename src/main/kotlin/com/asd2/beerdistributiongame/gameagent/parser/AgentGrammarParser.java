package com.asd2.beerdistributiongame.gameagent.parser;// Generated from AgentGrammar.g4 by ANTLR 4.7.1

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class AgentGrammarParser extends Parser {
    static {
        RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            PLUS = 1, MIN = 2, MUL = 3, DIV = 4, EXP = 5, ROOT = 6, LESS = 7, GREATER = 8, ORDER = 9,
            IF = 10, THEN = 11, EQUALS = 12, SEMICOLON = 13, STANDARD_VAR = 14, ORDER_VAR = 15,
            PERSONAL_VAR = 16, NUMBER = 17, DECIMAL = 18, OPEN_ROUND_BRACKET = 19, CLOSE_ROUND_BRACKET = 20,
            OPEN_SQUARE_BRACKET = 21, CLOSE_SQUARE_BRACKET = 22, WS = 23;
    public static final int
            RULE_behaviourFormula = 0, RULE_defaultOrder = 1, RULE_alternativeOrder = 2,
            RULE_variableAssignment = 3, RULE_ifClause = 4, RULE_thenClause = 5, RULE_comparison = 6,
            RULE_expression = 7, RULE_expressionPart = 8, RULE_priorityExpression = 9,
            RULE_exponentExpression = 10, RULE_rootExpression = 11, RULE_value = 12,
            RULE_scalar = 13, RULE_variable = 14;
    public static final String[] ruleNames = {
            "behaviourFormula", "defaultOrder", "alternativeOrder", "variableAssignment",
            "ifClause", "thenClause", "comparison", "expression", "expressionPart",
            "priorityExpression", "exponentExpression", "rootExpression", "value",
            "scalar", "variable"
    };

    private static final String[] _LITERAL_NAMES = {
            null, "'+'", "'-'", "'*'", "'/'", "'^'", "'\u221A'", "'<'", "'>'", "'NewOrder'",
            "'if'", "'then'", "'='", "';'", null, "'Order'", null, null, null, "'('",
            "')'", "'['", "']'"
    };
    private static final String[] _SYMBOLIC_NAMES = {
            null, "PLUS", "MIN", "MUL", "DIV", "EXP", "ROOT", "LESS", "GREATER", "ORDER",
            "IF", "THEN", "EQUALS", "SEMICOLON", "STANDARD_VAR", "ORDER_VAR", "PERSONAL_VAR",
            "NUMBER", "DECIMAL", "OPEN_ROUND_BRACKET", "CLOSE_ROUND_BRACKET", "OPEN_SQUARE_BRACKET",
            "CLOSE_SQUARE_BRACKET", "WS"
    };
    public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

    /**
     * @deprecated Use {@link #VOCABULARY} instead.
     */
    @Deprecated
    public static final String[] tokenNames;

    static {
        tokenNames = new String[_SYMBOLIC_NAMES.length];
        for (int i = 0; i < tokenNames.length; i++) {
            tokenNames[i] = VOCABULARY.getLiteralName(i);
            if (tokenNames[i] == null) {
                tokenNames[i] = VOCABULARY.getSymbolicName(i);
            }

            if (tokenNames[i] == null) {
                tokenNames[i] = "<INVALID>";
            }
        }
    }

    @Override
    @Deprecated
    public String[] getTokenNames() {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary() {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {
        return "AgentGrammar.g4";
    }

    @Override
    public String[] getRuleNames() {
        return ruleNames;
    }

    @Override
    public String getSerializedATN() {
        return _serializedATN;
    }

    @Override
    public ATN getATN() {
        return _ATN;
    }

    public AgentGrammarParser(TokenStream input) {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class BehaviourFormulaContext extends ParserRuleContext {
        public DefaultOrderContext defaultOrder() {
            return getRuleContext(DefaultOrderContext.class, 0);
        }

        public List<VariableAssignmentContext> variableAssignment() {
            return getRuleContexts(VariableAssignmentContext.class);
        }

        public VariableAssignmentContext variableAssignment(int i) {
            return getRuleContext(VariableAssignmentContext.class, i);
        }

        public List<AlternativeOrderContext> alternativeOrder() {
            return getRuleContexts(AlternativeOrderContext.class);
        }

        public AlternativeOrderContext alternativeOrder(int i) {
            return getRuleContext(AlternativeOrderContext.class, i);
        }

        public BehaviourFormulaContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_behaviourFormula;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterBehaviourFormula(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitBehaviourFormula(this);
        }
    }

    public final BehaviourFormulaContext behaviourFormula() throws RecognitionException {
        BehaviourFormulaContext _localctx = new BehaviourFormulaContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_behaviourFormula);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(33);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == PERSONAL_VAR) {
                    {
                        {
                            setState(30);
                            variableAssignment();
                        }
                    }
                    setState(35);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                setState(36);
                defaultOrder();
                setState(40);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == IF) {
                    {
                        {
                            setState(37);
                            alternativeOrder();
                        }
                    }
                    setState(42);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class DefaultOrderContext extends ParserRuleContext {
        public TerminalNode ORDER() {
            return getToken(AgentGrammarParser.ORDER, 0);
        }

        public TerminalNode EQUALS() {
            return getToken(AgentGrammarParser.EQUALS, 0);
        }

        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public DefaultOrderContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_defaultOrder;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterDefaultOrder(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitDefaultOrder(this);
        }
    }

    public final DefaultOrderContext defaultOrder() throws RecognitionException {
        DefaultOrderContext _localctx = new DefaultOrderContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_defaultOrder);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(43);
                match(ORDER);
                setState(44);
                match(EQUALS);
                setState(46);
                _errHandler.sync(this);
                _la = _input.LA(1);
                do {
                    {
                        {
                            setState(45);
                            expression(0);
                        }
                    }
                    setState(48);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << ROOT) | (1L << STANDARD_VAR) | (1L << ORDER_VAR) | (1L << PERSONAL_VAR) | (1L << NUMBER) | (1L << DECIMAL) | (1L << OPEN_ROUND_BRACKET))) != 0));
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class AlternativeOrderContext extends ParserRuleContext {
        public IfClauseContext ifClause() {
            return getRuleContext(IfClauseContext.class, 0);
        }

        public ThenClauseContext thenClause() {
            return getRuleContext(ThenClauseContext.class, 0);
        }

        public TerminalNode SEMICOLON() {
            return getToken(AgentGrammarParser.SEMICOLON, 0);
        }

        public AlternativeOrderContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_alternativeOrder;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterAlternativeOrder(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitAlternativeOrder(this);
        }
    }

    public final AlternativeOrderContext alternativeOrder() throws RecognitionException {
        AlternativeOrderContext _localctx = new AlternativeOrderContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_alternativeOrder);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(50);
                ifClause();
                setState(51);
                thenClause();
                setState(52);
                match(SEMICOLON);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class VariableAssignmentContext extends ParserRuleContext {
        public TerminalNode PERSONAL_VAR() {
            return getToken(AgentGrammarParser.PERSONAL_VAR, 0);
        }

        public TerminalNode EQUALS() {
            return getToken(AgentGrammarParser.EQUALS, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public VariableAssignmentContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_variableAssignment;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener)
                ((AgentGrammarListener) listener).enterVariableAssignment(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener)
                ((AgentGrammarListener) listener).exitVariableAssignment(this);
        }
    }

    public final VariableAssignmentContext variableAssignment() throws RecognitionException {
        VariableAssignmentContext _localctx = new VariableAssignmentContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_variableAssignment);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(54);
                match(PERSONAL_VAR);
                setState(55);
                match(EQUALS);
                setState(56);
                expression(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IfClauseContext extends ParserRuleContext {
        public TerminalNode IF() {
            return getToken(AgentGrammarParser.IF, 0);
        }

        public TerminalNode OPEN_SQUARE_BRACKET() {
            return getToken(AgentGrammarParser.OPEN_SQUARE_BRACKET, 0);
        }

        public ComparisonContext comparison() {
            return getRuleContext(ComparisonContext.class, 0);
        }

        public TerminalNode CLOSE_SQUARE_BRACKET() {
            return getToken(AgentGrammarParser.CLOSE_SQUARE_BRACKET, 0);
        }

        public IfClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_ifClause;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterIfClause(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitIfClause(this);
        }
    }

    public final IfClauseContext ifClause() throws RecognitionException {
        IfClauseContext _localctx = new IfClauseContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_ifClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(58);
                match(IF);
                setState(59);
                match(OPEN_SQUARE_BRACKET);
                setState(60);
                comparison();
                setState(61);
                match(CLOSE_SQUARE_BRACKET);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ThenClauseContext extends ParserRuleContext {
        public TerminalNode THEN() {
            return getToken(AgentGrammarParser.THEN, 0);
        }

        public TerminalNode OPEN_SQUARE_BRACKET() {
            return getToken(AgentGrammarParser.OPEN_SQUARE_BRACKET, 0);
        }

        public TerminalNode ORDER_VAR() {
            return getToken(AgentGrammarParser.ORDER_VAR, 0);
        }

        public TerminalNode EQUALS() {
            return getToken(AgentGrammarParser.EQUALS, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode CLOSE_SQUARE_BRACKET() {
            return getToken(AgentGrammarParser.CLOSE_SQUARE_BRACKET, 0);
        }

        public VariableAssignmentContext variableAssignment() {
            return getRuleContext(VariableAssignmentContext.class, 0);
        }

        public ThenClauseContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_thenClause;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterThenClause(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitThenClause(this);
        }
    }

    public final ThenClauseContext thenClause() throws RecognitionException {
        ThenClauseContext _localctx = new ThenClauseContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_thenClause);
        try {
            setState(75);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 3, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(63);
                    match(THEN);
                    setState(64);
                    match(OPEN_SQUARE_BRACKET);
                    setState(65);
                    match(ORDER_VAR);
                    setState(66);
                    match(EQUALS);
                    setState(67);
                    expression(0);
                    setState(68);
                    match(CLOSE_SQUARE_BRACKET);
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(70);
                    match(THEN);
                    setState(71);
                    match(OPEN_SQUARE_BRACKET);
                    setState(72);
                    variableAssignment();
                    setState(73);
                    match(CLOSE_SQUARE_BRACKET);
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ComparisonContext extends ParserRuleContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode GREATER() {
            return getToken(AgentGrammarParser.GREATER, 0);
        }

        public TerminalNode LESS() {
            return getToken(AgentGrammarParser.LESS, 0);
        }

        public TerminalNode EQUALS() {
            return getToken(AgentGrammarParser.EQUALS, 0);
        }

        public TerminalNode ORDER_VAR() {
            return getToken(AgentGrammarParser.ORDER_VAR, 0);
        }

        public ComparisonContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_comparison;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterComparison(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitComparison(this);
        }
    }

    public final ComparisonContext comparison() throws RecognitionException {
        ComparisonContext _localctx = new ComparisonContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_comparison);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(79);
                _errHandler.sync(this);
                switch (getInterpreter().adaptivePredict(_input, 4, _ctx)) {
                    case 1: {
                        setState(77);
                        expression(0);
                    }
                    break;
                    case 2: {
                        setState(78);
                        match(ORDER_VAR);
                    }
                    break;
                }
                setState(81);
                _la = _input.LA(1);
                if (!((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LESS) | (1L << GREATER) | (1L << EQUALS))) != 0))) {
                    _errHandler.recoverInline(this);
                } else {
                    if (_input.LA(1) == Token.EOF) matchedEOF = true;
                    _errHandler.reportMatch(this);
                    consume();
                }
                setState(82);
                expression(0);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExpressionContext extends ParserRuleContext {
        public ExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expression;
        }

        public ExpressionContext() {
        }

        public void copyFrom(ExpressionContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class DivisionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode DIV() {
            return getToken(AgentGrammarParser.DIV, 0);
        }

        public DivisionContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterDivision(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitDivision(this);
        }
    }

    public static class SubtractionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode MIN() {
            return getToken(AgentGrammarParser.MIN, 0);
        }

        public SubtractionContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterSubtraction(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitSubtraction(this);
        }
    }

    public static class MultiplicationContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode MUL() {
            return getToken(AgentGrammarParser.MUL, 0);
        }

        public MultiplicationContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterMultiplication(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitMultiplication(this);
        }
    }

    public static class SubExpressionContext extends ExpressionContext {
        public ExpressionPartContext expressionPart() {
            return getRuleContext(ExpressionPartContext.class, 0);
        }

        public SubExpressionContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterSubExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitSubExpression(this);
        }
    }

    public static class AdditionContext extends ExpressionContext {
        public List<ExpressionContext> expression() {
            return getRuleContexts(ExpressionContext.class);
        }

        public ExpressionContext expression(int i) {
            return getRuleContext(ExpressionContext.class, i);
        }

        public TerminalNode PLUS() {
            return getToken(AgentGrammarParser.PLUS, 0);
        }

        public AdditionContext(ExpressionContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterAddition(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitAddition(this);
        }
    }

    public final ExpressionContext expression() throws RecognitionException {
        return expression(0);
    }

    private ExpressionContext expression(int _p) throws RecognitionException {
        ParserRuleContext _parentctx = _ctx;
        int _parentState = getState();
        ExpressionContext _localctx = new ExpressionContext(_ctx, _parentState);
        ExpressionContext _prevctx = _localctx;
        int _startState = 14;
        enterRecursionRule(_localctx, 14, RULE_expression, _p);
        try {
            int _alt;
            enterOuterAlt(_localctx, 1);
            {
                {
                    _localctx = new SubExpressionContext(_localctx);
                    _ctx = _localctx;
                    _prevctx = _localctx;

                    setState(85);
                    expressionPart();
                }
                _ctx.stop = _input.LT(-1);
                setState(101);
                _errHandler.sync(this);
                _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
                while (_alt != 2 && _alt != org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER) {
                    if (_alt == 1) {
                        if (_parseListeners != null) triggerExitRuleEvent();
                        _prevctx = _localctx;
                        {
                            setState(99);
                            _errHandler.sync(this);
                            switch (getInterpreter().adaptivePredict(_input, 5, _ctx)) {
                                case 1: {
                                    _localctx = new MultiplicationContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(87);
                                    if (!(precpred(_ctx, 4)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 4)");
                                    setState(88);
                                    match(MUL);
                                    setState(89);
                                    expression(5);
                                }
                                break;
                                case 2: {
                                    _localctx = new DivisionContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(90);
                                    if (!(precpred(_ctx, 3)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 3)");
                                    setState(91);
                                    match(DIV);
                                    setState(92);
                                    expression(4);
                                }
                                break;
                                case 3: {
                                    _localctx = new SubtractionContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(93);
                                    if (!(precpred(_ctx, 2)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 2)");
                                    setState(94);
                                    match(MIN);
                                    setState(95);
                                    expression(3);
                                }
                                break;
                                case 4: {
                                    _localctx = new AdditionContext(new ExpressionContext(_parentctx, _parentState));
                                    pushNewRecursionContext(_localctx, _startState, RULE_expression);
                                    setState(96);
                                    if (!(precpred(_ctx, 1)))
                                        throw new FailedPredicateException(this, "precpred(_ctx, 1)");
                                    setState(97);
                                    match(PLUS);
                                    setState(98);
                                    expression(2);
                                }
                                break;
                            }
                        }
                    }
                    setState(103);
                    _errHandler.sync(this);
                    _alt = getInterpreter().adaptivePredict(_input, 6, _ctx);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            unrollRecursionContexts(_parentctx);
        }
        return _localctx;
    }

    public static class ExpressionPartContext extends ParserRuleContext {
        public ExponentExpressionContext exponentExpression() {
            return getRuleContext(ExponentExpressionContext.class, 0);
        }

        public RootExpressionContext rootExpression() {
            return getRuleContext(RootExpressionContext.class, 0);
        }

        public ValueContext value() {
            return getRuleContext(ValueContext.class, 0);
        }

        public PriorityExpressionContext priorityExpression() {
            return getRuleContext(PriorityExpressionContext.class, 0);
        }

        public ExpressionPartContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_expressionPart;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterExpressionPart(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitExpressionPart(this);
        }
    }

    public final ExpressionPartContext expressionPart() throws RecognitionException {
        ExpressionPartContext _localctx = new ExpressionPartContext(_ctx, getState());
        enterRule(_localctx, 16, RULE_expressionPart);
        try {
            setState(108);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 7, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(104);
                    exponentExpression();
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(105);
                    rootExpression();
                }
                break;
                case 3:
                    enterOuterAlt(_localctx, 3);
                {
                    setState(106);
                    value();
                }
                break;
                case 4:
                    enterOuterAlt(_localctx, 4);
                {
                    setState(107);
                    priorityExpression();
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class PriorityExpressionContext extends ParserRuleContext {
        public TerminalNode OPEN_ROUND_BRACKET() {
            return getToken(AgentGrammarParser.OPEN_ROUND_BRACKET, 0);
        }

        public ExpressionContext expression() {
            return getRuleContext(ExpressionContext.class, 0);
        }

        public TerminalNode CLOSE_ROUND_BRACKET() {
            return getToken(AgentGrammarParser.CLOSE_ROUND_BRACKET, 0);
        }

        public PriorityExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_priorityExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener)
                ((AgentGrammarListener) listener).enterPriorityExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener)
                ((AgentGrammarListener) listener).exitPriorityExpression(this);
        }
    }

    public final PriorityExpressionContext priorityExpression() throws RecognitionException {
        PriorityExpressionContext _localctx = new PriorityExpressionContext(_ctx, getState());
        enterRule(_localctx, 18, RULE_priorityExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(110);
                match(OPEN_ROUND_BRACKET);
                setState(111);
                expression(0);
                setState(112);
                match(CLOSE_ROUND_BRACKET);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ExponentExpressionContext extends ParserRuleContext {
        public TerminalNode EXP() {
            return getToken(AgentGrammarParser.EXP, 0);
        }

        public List<ValueContext> value() {
            return getRuleContexts(ValueContext.class);
        }

        public ValueContext value(int i) {
            return getRuleContext(ValueContext.class, i);
        }

        public List<PriorityExpressionContext> priorityExpression() {
            return getRuleContexts(PriorityExpressionContext.class);
        }

        public PriorityExpressionContext priorityExpression(int i) {
            return getRuleContext(PriorityExpressionContext.class, i);
        }

        public TerminalNode OPEN_ROUND_BRACKET() {
            return getToken(AgentGrammarParser.OPEN_ROUND_BRACKET, 0);
        }

        public TerminalNode DIV() {
            return getToken(AgentGrammarParser.DIV, 0);
        }

        public TerminalNode CLOSE_ROUND_BRACKET() {
            return getToken(AgentGrammarParser.CLOSE_ROUND_BRACKET, 0);
        }

        public VariableContext variable() {
            return getRuleContext(VariableContext.class, 0);
        }

        public ScalarContext scalar() {
            return getRuleContext(ScalarContext.class, 0);
        }

        public ExponentExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_exponentExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener)
                ((AgentGrammarListener) listener).enterExponentExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener)
                ((AgentGrammarListener) listener).exitExponentExpression(this);
        }
    }

    public final ExponentExpressionContext exponentExpression() throws RecognitionException {
        ExponentExpressionContext _localctx = new ExponentExpressionContext(_ctx, getState());
        enterRule(_localctx, 20, RULE_exponentExpression);
        try {
            setState(141);
            _errHandler.sync(this);
            switch (getInterpreter().adaptivePredict(_input, 13, _ctx)) {
                case 1:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(116);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case STANDARD_VAR:
                        case ORDER_VAR:
                        case PERSONAL_VAR:
                        case NUMBER:
                        case DECIMAL: {
                            setState(114);
                            value();
                        }
                        break;
                        case OPEN_ROUND_BRACKET: {
                            setState(115);
                            priorityExpression();
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(118);
                    match(EXP);
                    setState(121);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case STANDARD_VAR:
                        case ORDER_VAR:
                        case PERSONAL_VAR:
                        case NUMBER:
                        case DECIMAL: {
                            setState(119);
                            value();
                        }
                        break;
                        case OPEN_ROUND_BRACKET: {
                            setState(120);
                            priorityExpression();
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                }
                break;
                case 2:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(125);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case STANDARD_VAR:
                        case ORDER_VAR:
                        case PERSONAL_VAR:
                        case NUMBER:
                        case DECIMAL: {
                            setState(123);
                            value();
                        }
                        break;
                        case OPEN_ROUND_BRACKET: {
                            setState(124);
                            priorityExpression();
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(127);
                    match(EXP);
                    setState(128);
                    match(OPEN_ROUND_BRACKET);
                    setState(132);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case STANDARD_VAR:
                        case ORDER_VAR:
                        case PERSONAL_VAR: {
                            setState(129);
                            variable();
                        }
                        break;
                        case NUMBER:
                        case DECIMAL: {
                            setState(130);
                            scalar();
                        }
                        break;
                        case OPEN_ROUND_BRACKET: {
                            setState(131);
                            priorityExpression();
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(134);
                    match(DIV);
                    setState(137);
                    _errHandler.sync(this);
                    switch (_input.LA(1)) {
                        case STANDARD_VAR:
                        case ORDER_VAR:
                        case PERSONAL_VAR:
                        case NUMBER:
                        case DECIMAL: {
                            setState(135);
                            value();
                        }
                        break;
                        case OPEN_ROUND_BRACKET: {
                            setState(136);
                            priorityExpression();
                        }
                        break;
                        default:
                            throw new NoViableAltException(this);
                    }
                    setState(139);
                    match(CLOSE_ROUND_BRACKET);
                }
                break;
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class RootExpressionContext extends ParserRuleContext {
        public TerminalNode ROOT() {
            return getToken(AgentGrammarParser.ROOT, 0);
        }

        public ValueContext value() {
            return getRuleContext(ValueContext.class, 0);
        }

        public PriorityExpressionContext priorityExpression() {
            return getRuleContext(PriorityExpressionContext.class, 0);
        }

        public RootExpressionContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_rootExpression;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterRootExpression(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitRootExpression(this);
        }
    }

    public final RootExpressionContext rootExpression() throws RecognitionException {
        RootExpressionContext _localctx = new RootExpressionContext(_ctx, getState());
        enterRule(_localctx, 22, RULE_rootExpression);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(143);
                match(ROOT);
                setState(146);
                _errHandler.sync(this);
                switch (_input.LA(1)) {
                    case STANDARD_VAR:
                    case ORDER_VAR:
                    case PERSONAL_VAR:
                    case NUMBER:
                    case DECIMAL: {
                        setState(144);
                        value();
                    }
                    break;
                    case OPEN_ROUND_BRACKET: {
                        setState(145);
                        priorityExpression();
                    }
                    break;
                    default:
                        throw new NoViableAltException(this);
                }
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ValueContext extends ParserRuleContext {
        public ScalarContext scalar() {
            return getRuleContext(ScalarContext.class, 0);
        }

        public VariableContext variable() {
            return getRuleContext(VariableContext.class, 0);
        }

        public ValueContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_value;
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterValue(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitValue(this);
        }
    }

    public final ValueContext value() throws RecognitionException {
        ValueContext _localctx = new ValueContext(_ctx, getState());
        enterRule(_localctx, 24, RULE_value);
        try {
            setState(150);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case NUMBER:
                case DECIMAL:
                    enterOuterAlt(_localctx, 1);
                {
                    setState(148);
                    scalar();
                }
                break;
                case STANDARD_VAR:
                case ORDER_VAR:
                case PERSONAL_VAR:
                    enterOuterAlt(_localctx, 2);
                {
                    setState(149);
                    variable();
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class ScalarContext extends ParserRuleContext {
        public ScalarContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_scalar;
        }

        public ScalarContext() {
        }

        public void copyFrom(ScalarContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class NumberContext extends ScalarContext {
        public TerminalNode NUMBER() {
            return getToken(AgentGrammarParser.NUMBER, 0);
        }

        public NumberContext(ScalarContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterNumber(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitNumber(this);
        }
    }

    public static class DecimalContext extends ScalarContext {
        public TerminalNode DECIMAL() {
            return getToken(AgentGrammarParser.DECIMAL, 0);
        }

        public DecimalContext(ScalarContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterDecimal(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitDecimal(this);
        }
    }

    public final ScalarContext scalar() throws RecognitionException {
        ScalarContext _localctx = new ScalarContext(_ctx, getState());
        enterRule(_localctx, 26, RULE_scalar);
        try {
            setState(154);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case NUMBER:
                    _localctx = new NumberContext(_localctx);
                    enterOuterAlt(_localctx, 1);
                {
                    setState(152);
                    match(NUMBER);
                }
                break;
                case DECIMAL:
                    _localctx = new DecimalContext(_localctx);
                    enterOuterAlt(_localctx, 2);
                {
                    setState(153);
                    match(DECIMAL);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public static class VariableContext extends ParserRuleContext {
        public VariableContext(ParserRuleContext parent, int invokingState) {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {
            return RULE_variable;
        }

        public VariableContext() {
        }

        public void copyFrom(VariableContext ctx) {
            super.copyFrom(ctx);
        }
    }

    public static class DefaultVariableContext extends VariableContext {
        public TerminalNode STANDARD_VAR() {
            return getToken(AgentGrammarParser.STANDARD_VAR, 0);
        }

        public DefaultVariableContext(VariableContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterDefaultVariable(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitDefaultVariable(this);
        }
    }

    public static class VariableReferenceContext extends VariableContext {
        public TerminalNode PERSONAL_VAR() {
            return getToken(AgentGrammarParser.PERSONAL_VAR, 0);
        }

        public VariableReferenceContext(VariableContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener)
                ((AgentGrammarListener) listener).enterVariableReference(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitVariableReference(this);
        }
    }

    public static class OrderVariableContext extends VariableContext {
        public TerminalNode ORDER_VAR() {
            return getToken(AgentGrammarParser.ORDER_VAR, 0);
        }

        public OrderVariableContext(VariableContext ctx) {
            copyFrom(ctx);
        }

        @Override
        public void enterRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).enterOrderVariable(this);
        }

        @Override
        public void exitRule(ParseTreeListener listener) {
            if (listener instanceof AgentGrammarListener) ((AgentGrammarListener) listener).exitOrderVariable(this);
        }
    }

    public final VariableContext variable() throws RecognitionException {
        VariableContext _localctx = new VariableContext(_ctx, getState());
        enterRule(_localctx, 28, RULE_variable);
        try {
            setState(159);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case STANDARD_VAR:
                    _localctx = new DefaultVariableContext(_localctx);
                    enterOuterAlt(_localctx, 1);
                {
                    setState(156);
                    match(STANDARD_VAR);
                }
                break;
                case PERSONAL_VAR:
                    _localctx = new VariableReferenceContext(_localctx);
                    enterOuterAlt(_localctx, 2);
                {
                    setState(157);
                    match(PERSONAL_VAR);
                }
                break;
                case ORDER_VAR:
                    _localctx = new OrderVariableContext(_localctx);
                    enterOuterAlt(_localctx, 3);
                {
                    setState(158);
                    match(ORDER_VAR);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        } catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        } finally {
            exitRule();
        }
        return _localctx;
    }

    public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
        switch (ruleIndex) {
            case 7:
                return expression_sempred((ExpressionContext) _localctx, predIndex);
        }
        return true;
    }

    private boolean expression_sempred(ExpressionContext _localctx, int predIndex) {
        switch (predIndex) {
            case 0:
                return precpred(_ctx, 4);
            case 1:
                return precpred(_ctx, 3);
            case 2:
                return precpred(_ctx, 2);
            case 3:
                return precpred(_ctx, 1);
        }
        return true;
    }

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\31\u00a4\4\2\t\2" +
                    "\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13" +
                    "\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\3\2\7\2\"\n\2\f\2" +
                    "\16\2%\13\2\3\2\3\2\7\2)\n\2\f\2\16\2,\13\2\3\3\3\3\3\3\6\3\61\n\3\r\3" +
                    "\16\3\62\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3" +
                    "\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\3\7\5\7N\n\7\3\b\3\b\5\bR\n\b\3\b\3" +
                    "\b\3\b\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\3\t\7\t" +
                    "f\n\t\f\t\16\ti\13\t\3\n\3\n\3\n\3\n\5\no\n\n\3\13\3\13\3\13\3\13\3\f" +
                    "\3\f\5\fw\n\f\3\f\3\f\3\f\5\f|\n\f\3\f\3\f\5\f\u0080\n\f\3\f\3\f\3\f\3" +
                    "\f\3\f\5\f\u0087\n\f\3\f\3\f\3\f\5\f\u008c\n\f\3\f\3\f\5\f\u0090\n\f\3" +
                    "\r\3\r\3\r\5\r\u0095\n\r\3\16\3\16\5\16\u0099\n\16\3\17\3\17\5\17\u009d" +
                    "\n\17\3\20\3\20\3\20\5\20\u00a2\n\20\3\20\2\3\20\21\2\4\6\b\n\f\16\20" +
                    "\22\24\26\30\32\34\36\2\3\4\2\t\n\16\16\2\u00ac\2#\3\2\2\2\4-\3\2\2\2" +
                    "\6\64\3\2\2\2\b8\3\2\2\2\n<\3\2\2\2\fM\3\2\2\2\16Q\3\2\2\2\20V\3\2\2\2" +
                    "\22n\3\2\2\2\24p\3\2\2\2\26\u008f\3\2\2\2\30\u0091\3\2\2\2\32\u0098\3" +
                    "\2\2\2\34\u009c\3\2\2\2\36\u00a1\3\2\2\2 \"\5\b\5\2! \3\2\2\2\"%\3\2\2" +
                    "\2#!\3\2\2\2#$\3\2\2\2$&\3\2\2\2%#\3\2\2\2&*\5\4\3\2\')\5\6\4\2(\'\3\2" +
                    "\2\2),\3\2\2\2*(\3\2\2\2*+\3\2\2\2+\3\3\2\2\2,*\3\2\2\2-.\7\13\2\2.\60" +
                    "\7\16\2\2/\61\5\20\t\2\60/\3\2\2\2\61\62\3\2\2\2\62\60\3\2\2\2\62\63\3" +
                    "\2\2\2\63\5\3\2\2\2\64\65\5\n\6\2\65\66\5\f\7\2\66\67\7\17\2\2\67\7\3" +
                    "\2\2\289\7\22\2\29:\7\16\2\2:;\5\20\t\2;\t\3\2\2\2<=\7\f\2\2=>\7\27\2" +
                    "\2>?\5\16\b\2?@\7\30\2\2@\13\3\2\2\2AB\7\r\2\2BC\7\27\2\2CD\7\21\2\2D" +
                    "E\7\16\2\2EF\5\20\t\2FG\7\30\2\2GN\3\2\2\2HI\7\r\2\2IJ\7\27\2\2JK\5\b" +
                    "\5\2KL\7\30\2\2LN\3\2\2\2MA\3\2\2\2MH\3\2\2\2N\r\3\2\2\2OR\5\20\t\2PR" +
                    "\7\21\2\2QO\3\2\2\2QP\3\2\2\2RS\3\2\2\2ST\t\2\2\2TU\5\20\t\2U\17\3\2\2" +
                    "\2VW\b\t\1\2WX\5\22\n\2Xg\3\2\2\2YZ\f\6\2\2Z[\7\5\2\2[f\5\20\t\7\\]\f" +
                    "\5\2\2]^\7\6\2\2^f\5\20\t\6_`\f\4\2\2`a\7\4\2\2af\5\20\t\5bc\f\3\2\2c" +
                    "d\7\3\2\2df\5\20\t\4eY\3\2\2\2e\\\3\2\2\2e_\3\2\2\2eb\3\2\2\2fi\3\2\2" +
                    "\2ge\3\2\2\2gh\3\2\2\2h\21\3\2\2\2ig\3\2\2\2jo\5\26\f\2ko\5\30\r\2lo\5" +
                    "\32\16\2mo\5\24\13\2nj\3\2\2\2nk\3\2\2\2nl\3\2\2\2nm\3\2\2\2o\23\3\2\2" +
                    "\2pq\7\25\2\2qr\5\20\t\2rs\7\26\2\2s\25\3\2\2\2tw\5\32\16\2uw\5\24\13" +
                    "\2vt\3\2\2\2vu\3\2\2\2wx\3\2\2\2x{\7\7\2\2y|\5\32\16\2z|\5\24\13\2{y\3" +
                    "\2\2\2{z\3\2\2\2|\u0090\3\2\2\2}\u0080\5\32\16\2~\u0080\5\24\13\2\177" +
                    "}\3\2\2\2\177~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0082\7\7\2\2\u0082\u0086" +
                    "\7\25\2\2\u0083\u0087\5\36\20\2\u0084\u0087\5\34\17\2\u0085\u0087\5\24" +
                    "\13\2\u0086\u0083\3\2\2\2\u0086\u0084\3\2\2\2\u0086\u0085\3\2\2\2\u0087" +
                    "\u0088\3\2\2\2\u0088\u008b\7\6\2\2\u0089\u008c\5\32\16\2\u008a\u008c\5" +
                    "\24\13\2\u008b\u0089\3\2\2\2\u008b\u008a\3\2\2\2\u008c\u008d\3\2\2\2\u008d" +
                    "\u008e\7\26\2\2\u008e\u0090\3\2\2\2\u008fv\3\2\2\2\u008f\177\3\2\2\2\u0090" +
                    "\27\3\2\2\2\u0091\u0094\7\b\2\2\u0092\u0095\5\32\16\2\u0093\u0095\5\24" +
                    "\13\2\u0094\u0092\3\2\2\2\u0094\u0093\3\2\2\2\u0095\31\3\2\2\2\u0096\u0099" +
                    "\5\34\17\2\u0097\u0099\5\36\20\2\u0098\u0096\3\2\2\2\u0098\u0097\3\2\2" +
                    "\2\u0099\33\3\2\2\2\u009a\u009d\7\23\2\2\u009b\u009d\7\24\2\2\u009c\u009a" +
                    "\3\2\2\2\u009c\u009b\3\2\2\2\u009d\35\3\2\2\2\u009e\u00a2\7\20\2\2\u009f" +
                    "\u00a2\7\22\2\2\u00a0\u00a2\7\21\2\2\u00a1\u009e\3\2\2\2\u00a1\u009f\3" +
                    "\2\2\2\u00a1\u00a0\3\2\2\2\u00a2\37\3\2\2\2\24#*\62MQegnv{\177\u0086\u008b" +
                    "\u008f\u0094\u0098\u009c\u00a1";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}