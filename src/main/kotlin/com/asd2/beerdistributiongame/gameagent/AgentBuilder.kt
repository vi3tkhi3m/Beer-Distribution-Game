package com.asd2.beerdistributiongame.gameagent

import com.asd2.beerdistributiongame.gameagent.Behaviour.RuleCalculator
import com.asd2.beerdistributiongame.gameagent.ast.AST
import com.asd2.beerdistributiongame.gameagent.checker.Checker
import com.asd2.beerdistributiongame.gameagent.parser.ASTListener
import com.asd2.beerdistributiongame.gameagent.parser.AgentGrammarLexer
import com.asd2.beerdistributiongame.gameagent.parser.AgentGrammarParser
import com.asd2.beerdistributiongame.gameagent.transforms.EvaluateVariableReferences
import org.antlr.v4.runtime.*
import org.antlr.v4.runtime.atn.ATNConfigSet
import org.antlr.v4.runtime.dfa.DFA
import org.antlr.v4.runtime.misc.ParseCancellationException
import org.antlr.v4.runtime.tree.ParseTree
import org.antlr.v4.runtime.tree.ParseTreeWalker
import java.util.*
import kotlin.collections.ArrayList

class AgentBuilder(var ast: AST) : ANTLRErrorListener {
    var errors: ArrayList<String> = ArrayList()
    private var parsed = false
    private var checked = false
    private var orderCalculated = false


    fun parseString(input: String) {
        val inputStream: CharStream = CharStreams.fromString(input)!!
        val lexer = AgentGrammarLexer(inputStream)
        lexer.removeErrorListeners()
        lexer.addErrorListener(this)
        try {
            val tokens = CommonTokenStream(lexer)

            //Parse (with Antlr's generated parser)
            val parser = AgentGrammarParser(tokens)
            parser.removeErrorListeners()
            parser.addErrorListener(this)

            val parseTree = parser.behaviourFormula() as ParseTree

            //Extract AST from the Antlr parse tree
            val listener = ASTListener()
            val walker = ParseTreeWalker()
            walker.walk(listener, parseTree)

            this.ast = listener.ast

        } catch (e: RecognitionException) {
            this.ast = AST()
            errors.add(e.message!!)

        } catch (e: ParseCancellationException) {
            this.ast = AST()
            errors.add("Syntax error")
        }

        parsed = true
        orderCalculated = false
        checked = orderCalculated

        check()
    }

    private fun check(): Boolean {
        Checker().check(this.ast)

        val errors = this.ast.errors
        if (!errors.isEmpty()) {
            for (e in errors) {
                this.errors.add(e.toString())
            }
        }

        checked = true
        orderCalculated = false
        return errors.isEmpty()
    }

    fun calculateOrder(roleKey: String) : Float{

        EvaluateVariableReferences().apply(ast)
        val rc = RuleCalculator()
        rc.calculateOrder(ast, roleKey)

        orderCalculated = true

        return rc.order.value
    }

    //Catch ANTLR errors
    override fun reportAmbiguity(arg0: Parser, arg1: DFA, arg2: Int, arg3: Int,
                                 arg4: Boolean, arg5: BitSet, arg6: ATNConfigSet) {
    }

    override fun reportAttemptingFullContext(arg0: Parser, arg1: DFA, arg2: Int,
                                             arg3: Int, arg4: BitSet, arg5: ATNConfigSet) {
    }

    override fun reportContextSensitivity(arg0: Parser, arg1: DFA, arg2: Int,
                                          arg3: Int, arg4: Int, arg5: ATNConfigSet) {
    }

    override fun syntaxError(arg0: Recognizer<*, *>, arg1: Any?, arg2: Int,
                             arg3: Int, arg4: String, arg5: RecognitionException?) {
        errors.add("Syntax error: $arg4")
    }
}