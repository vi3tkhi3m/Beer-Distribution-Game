package com.asd2.beerdistributiongame.gameagent.checker

import com.asd2.beerdistributiongame.gameagent.ast.*
import com.asd2.beerdistributiongame.gameagent.ast.expression.Root
import com.asd2.beerdistributiongame.gameagent.ast.variable.VariableAssignment
import com.asd2.beerdistributiongame.gameagent.ast.variable.VariableReference
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import org.junit.Before
import org.junit.Test

class checkerTest {

    @MockK()
    var variableTypes: HashMap<String, VariableAssignment> = HashMap()

    lateinit var ast: AST

    lateinit var node: ASTNode

    var variableAssignment: VariableAssignment = VariableAssignment("Backlog")
    var variableReference: VariableReference = VariableReference("1 + 2")
    var root: Root = Root()
    var ifClause: IfClause = IfClause()
    var expression: Expression = Expression()
    var comparison: Comparison = Comparison('a')

    @InjectMockKs(overrideValues = true)
    var checker: Checker = Checker()

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        ast = AST()
        node = ASTNode()
        variableAssignment.expression = expression
        comparison.left = expression
        comparison.right = expression
        root.right = expression
        ifClause.comparison = comparison
        ast.root.addChild(node)
    }

    @Test
    fun testCheckNodeWithVariableAssignment(){
        every { checker.checkNode(variableAssignment) } just Runs
        checker.checkNode(variableAssignment)
        verify { checker.checkVariables(variableAssignment) }
    }

    @Test
    fun testCheckNodeWithReference() {
        every { checker.variableTypes.containsKey("1 + 2") } returns true
        checker.checkNode(variableReference)
        verify { checker.checkReference(variableReference) }
    }
}