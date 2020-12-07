package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.frontend.inputCheckers.GameAgentInputChecker
import com.asd2.beerdistributiongame.gameagent.AgentBuilder
import com.asd2.beerdistributiongame.gameagent.GameAgentService
import com.asd2.beerdistributiongame.gameagent.ast.AST
import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import com.asd2.beerdistributiongame.state.CreateGameAgentState
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.text.Text

class CreateGameAgentHandler : Handler<CreateGameAgentState>() {

    @FXML
    lateinit var createGameAgentButton: Button
    @FXML
    lateinit var agentNameInput: TextField
    @FXML
    lateinit var defaultOrder: TextField
    @FXML
    var comboBoxInsertSymbol: ComboBox<String> = ComboBox()
    @FXML
    var comboBoxInsertVariable: ComboBox<String> = ComboBox()
    @FXML
    lateinit var errorMessageText: Text
    @FXML
    lateinit var anchorPane: AnchorPane
    @FXML
    lateinit var variableSymbol: Text
    @FXML
    lateinit var personalVariableName: TextField
    @FXML
    lateinit var personalVariableFormula: TextField
    @FXML
    lateinit var equalSymbol: Text
    @FXML
    lateinit var borderPane: Pane
    @FXML
    lateinit var addPersonalVariableButton: Button
    @FXML
    lateinit var ifText: Text
    @FXML
    lateinit var thenText: Text
    @FXML
    lateinit var alternativeThen: TextField
    @FXML
    lateinit var alternativeIf: TextField
    @FXML
    lateinit var addAlternativeFormulaButton: Button

    val gameAgentInputChecker: GameAgentInputChecker = GameAgentInputChecker()

    val spaceBetweenLines = 40.0
    var personalVariableHeight = spaceBetweenLines
    var alternativeFormulaHeight = spaceBetweenLines
    var distanceBetweenInputAndButtons = 0
    val saveButtonYCoordinate = 435.0

    var newPersonalVariableDeleteButtons: MutableList<Button> = mutableListOf()
    var newSymbolTexts: MutableList<Text> = mutableListOf()
    var newPersonalVariableNames: MutableList<TextField> = mutableListOf()
    var newPersonalVariableFormulas: MutableList<TextField> = mutableListOf()
    var newEqualTexts: MutableList<Text> = mutableListOf()
    var focusedTextField: TextField = TextField()

    var newAlternativeFormulaDeleteButtons: MutableList<Button> = mutableListOf()
    var newIfTexts: MutableList<Text> = mutableListOf()
    var newThenTexts: MutableList<Text> = mutableListOf()
    var newIfFields: MutableList<TextField> = mutableListOf()
    var newThenFields: MutableList<TextField> = mutableListOf()

    var personalVariableRows = mutableListOf(newPersonalVariableDeleteButtons, newSymbolTexts, newPersonalVariableNames, newPersonalVariableFormulas, newEqualTexts)
    var alternativeFormulaRows = mutableListOf(newAlternativeFormulaDeleteButtons, newIfTexts, newThenTexts, newIfFields, newThenFields)

    override fun getViewFile(): String = "views/CreateGameAgent.fxml"

    fun goBackToMainMenu() = state.notifyToGoBackToMainMenu()

    fun insertSymbol() {
        focusedTextField.text += comboBoxInsertSymbol.selectionModel.selectedItem
    }

    fun insertVariable() {
        focusedTextField.text += comboBoxInsertVariable.selectionModel.selectedItem
    }


    fun saveGameAgent() {
        clearErrorMessages()
        var agentBuilder = AgentBuilder(AST())
        val personalVariables = GameAgentService.extractPersonalVariablesForSave(personalVariableName, newPersonalVariableNames, personalVariableFormula, newPersonalVariableFormulas)
        val standardOrder = GameAgentService.extractDefaultFormulaForSave(defaultOrder)
        val alternatives = GameAgentService.extractIfsAndThensForSave(alternativeIf, newIfFields, alternativeThen, newThenFields)
        if (!checkIfInputIsValid(personalVariables, standardOrder, alternatives, agentBuilder)) {
            StorageManager.createAgent(agentNameInput.text, personalVariables + standardOrder + alternatives)
            showAlert("De agent is succesvol opgeslagen")
        }
    }

    private fun checkIfInputIsValid(personalVariables: String, standardOrder: String, alternatives: String, agentBuilder: AgentBuilder): Boolean {
        when {
            !gameAgentInputChecker.checkCreateInput(personalVariables, standardOrder, alternatives, agentNameInput.text) -> {
                loadErrorsIntoView(gameAgentInputChecker.errors)
                return true
            }
            else -> {
                agentBuilder.parseString(personalVariables + standardOrder + alternatives)
                when {
                    !agentBuilder.errors.isEmpty() -> {
                        loadErrorsIntoView(agentBuilder.errors)
                        return true
                    }
                    else -> {
                        return false
                    }
                }
            }
        }
    }

    private fun loadErrorsIntoView(errors: MutableList<String>) {
        var distanceBetweenInputAndButtons = 0
        var index = 0
        for (text in errors) {
            distanceBetweenInputAndButtons += 18
            index++
            errorMessageText.text += "$index. input error: $text. \n"
            moveButtons()
        }
    }

    fun moveButtons() {
        if (personalVariableHeight > 40)
            createGameAgentButton.layoutY = saveButtonYCoordinate + personalVariableHeight + distanceBetweenInputAndButtons
        else if (alternativeFormulaHeight > 40)
            createGameAgentButton.layoutY = saveButtonYCoordinate + alternativeFormulaHeight + distanceBetweenInputAndButtons
        else
            createGameAgentButton.layoutY = saveButtonYCoordinate + distanceBetweenInputAndButtons
    }

    fun clearErrorMessages() {
        errorMessageText.text = ""
        distanceBetweenInputAndButtons = 18
        gameAgentInputChecker.errors.clear()
    }

    fun resetSaveButtonYCoordinate() {
        val distance = 40
        var buttonHeight = saveButtonYCoordinate
        if (personalVariableHeight > distance)
            buttonHeight = saveButtonYCoordinate + personalVariableHeight - distance
        if (alternativeFormulaHeight > distance)
            buttonHeight = saveButtonYCoordinate + alternativeFormulaHeight - distance
        createGameAgentButton.layoutY = buttonHeight
    }

    fun addPersonalVariable() {
        val newDeleteButton = GameAgentService.createButton(219.0 + personalVariableHeight, 641.0, EventHandler { deletePersonalVariable() })
        val newSymbolText = Text(variableSymbol.layoutX, variableSymbol.layoutY + personalVariableHeight, variableSymbol.text)
        val newPersonalVariableName = GameAgentService.createTextField(personalVariableName.layoutY + personalVariableHeight, personalVariableName.layoutX, personalVariableName.prefHeight, personalVariableName.prefWidth)
        val newPersonalVariableFormule = GameAgentService.createTextField(personalVariableFormula.layoutY + personalVariableHeight, personalVariableFormula.layoutX, personalVariableFormula.prefHeight, personalVariableFormula.prefWidth)
        val newEqualText = Text(equalSymbol.layoutX, equalSymbol.layoutY + personalVariableHeight, equalSymbol.text)
        errorMessageText.layoutY += spaceBetweenLines
        addPersonalVariableButton.layoutY += spaceBetweenLines
        createGameAgentButton.layoutY += spaceBetweenLines
        borderPane.layoutY += spaceBetweenLines
        anchorPane.children.addAll(newDeleteButton, newSymbolText, newPersonalVariableFormule, newPersonalVariableName, newEqualText)
        newPersonalVariableDeleteButtons.add(newDeleteButton)
        newSymbolTexts.add(newSymbolText)
        newPersonalVariableNames.add(newPersonalVariableName)
        newPersonalVariableFormulas.add(newPersonalVariableFormule)
        newEqualTexts.add(newEqualText)
        personalVariableHeight += spaceBetweenLines
    }

    fun deletePersonalVariable() {
        errorMessageText.layoutY -= spaceBetweenLines
        createGameAgentButton.layoutY -= spaceBetweenLines
        addPersonalVariableButton.layoutY -= spaceBetweenLines
        borderPane.layoutY -= spaceBetweenLines
        personalVariableHeight -= spaceBetweenLines
        GameAgentService.loopThroughFXMLListAndDeleteSelectedRow(personalVariableRows, newPersonalVariableDeleteButtons, anchorPane, spaceBetweenLines)
    }

    fun addAlternativeFormula() {
        val newDeleteButton = GameAgentService.createButton(198.0 + alternativeFormulaHeight, 667.0, EventHandler { deleteAlternativeFormula() })
        val newIfText = Text(ifText.layoutX, ifText.layoutY + alternativeFormulaHeight, ifText.text)
        val newThenText = Text(thenText.layoutX, thenText.layoutY + alternativeFormulaHeight, thenText.text)
        val newIfField = GameAgentService.createTextField(alternativeIf.layoutY + alternativeFormulaHeight, alternativeIf.layoutX, alternativeIf.prefHeight, alternativeIf.prefWidth)
        val newThenField = GameAgentService.createTextField(alternativeThen.layoutY + alternativeFormulaHeight, alternativeThen.layoutX, alternativeThen.prefHeight, alternativeThen.prefWidth)
        errorMessageText.layoutY += spaceBetweenLines
        addAlternativeFormulaButton.layoutY += spaceBetweenLines
        borderPane.children.addAll(newDeleteButton, newIfText, newThenText, newIfField, newThenField)
        createGameAgentButton.layoutY += spaceBetweenLines
        newAlternativeFormulaDeleteButtons.add(newDeleteButton)
        newIfTexts.add(newIfText)
        newThenTexts.add(newThenText)
        newIfFields.add(newIfField)
        newThenFields.add(newThenField)
        alternativeFormulaHeight += spaceBetweenLines
    }

    fun deleteAlternativeFormula() {
        errorMessageText.layoutY -= spaceBetweenLines
        addAlternativeFormulaButton.layoutY -= spaceBetweenLines
        createGameAgentButton.layoutY -= spaceBetweenLines
        alternativeFormulaHeight -= spaceBetweenLines
        GameAgentService.loopThroughFXMLListAndDeleteSelectedRow(alternativeFormulaRows, newAlternativeFormulaDeleteButtons, borderPane, spaceBetweenLines)
    }

    fun alternativeThenSelected() {
        focusedTextField = alternativeThen
    }

    fun alternativeIfSelected() {
        focusedTextField = alternativeIf
    }

    fun variableNameSelected() {
        focusedTextField = personalVariableName
    }

    fun variableFormuleSelected() {
        focusedTextField = personalVariableFormula
    }

    fun defaultOrderSelected() {
        focusedTextField = defaultOrder
    }
}
