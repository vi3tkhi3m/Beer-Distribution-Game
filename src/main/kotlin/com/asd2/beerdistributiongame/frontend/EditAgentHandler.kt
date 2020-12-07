package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.frontend.inputCheckers.GameAgentInputChecker
import com.asd2.beerdistributiongame.gameagent.AgentBuilder
import com.asd2.beerdistributiongame.gameagent.GameAgentService
import com.asd2.beerdistributiongame.gameagent.GameAgentService.Companion.checkIfSelectedAgentCorrupt
import com.asd2.beerdistributiongame.gameagent.GameAgentService.Companion.getIfAndThenStatementsFromLines
import com.asd2.beerdistributiongame.gameagent.GameAgentService.Companion.getPersonalVariablesFromLines
import com.asd2.beerdistributiongame.gameagent.GameAgentService.Companion.getStandardOrderFromLines
import com.asd2.beerdistributiongame.gameagent.ast.AST
import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import com.asd2.beerdistributiongame.state.EditGameAgentState
import javafx.event.EventHandler
import javafx.fxml.FXML
import javafx.scene.Node
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.Pane
import javafx.scene.text.Text


class EditAgentHandler : Handler<EditGameAgentState>() {

    override fun getViewFile(): String = "views/EditGameAgent.fxml"

    @FXML
    lateinit var buttonSaveAgent: Button
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
    @FXML
    var comboBoxSelectAgent: ComboBox<String> = ComboBox()
    @FXML
    lateinit var buttonDeleteAgent: Button

    val spaceBetweenLines = 40.0
    var personalVariableHeight = spaceBetweenLines
    var alternativeFormulaHeight = spaceBetweenLines
    var addPersonalVariableButtonStandardY = 0.0
    var borderPaneStandardY = 0.0
    var standardAlternativeY = 0.0
    var addAlternativeButtonStandardY = 0.0
    var distanceBetweenInputAndButtons = 0
    val saveButtonYCoordinate = 488.0

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

    val gameAgentInputChecker: GameAgentInputChecker = GameAgentInputChecker()

    @FXML
    fun initialize() {
        StorageManager.createDefault()
        comboBoxSelectAgent.items.addAll(getAgentNamesStorage())
        addPersonalVariableButtonStandardY = addPersonalVariableButton.layoutY
        borderPaneStandardY = borderPane.layoutY
        standardAlternativeY = alternativeIf.layoutY
        addAlternativeButtonStandardY = addAlternativeFormulaButton.layoutY;
    }

    fun insertSymbol() {
        focusedTextField.text += comboBoxInsertSymbol.selectionModel.selectedItem
    }

    fun insertVariable() {
        focusedTextField.text += comboBoxInsertVariable.selectionModel.selectedItem
    }

    fun getAgentNamesStorage(): List<String> = StorageManager.getAgentNames()

    fun loadPersonalVariablesIntoFields(variables: HashMap<String, MutableList<String>>) {
        if (variables["names"]?.size != 0) {
            var variableNames = variables["names"]
            var variableFormulas = variables["formulas"]
            personalVariableName.text = variableNames!!.elementAt(0).substring(1)
            personalVariableFormula.text = variableFormulas!!.elementAt(0)
            if (variableNames.size > 1) {
                for (i in 1..variableNames.size - 1) {
                    addPersonalVariable()
                    newPersonalVariableNames.get(i - 1).text = variableNames.elementAt(i).substring(1)
                    newPersonalVariableFormulas.get(i - 1).text = variableFormulas.elementAt(i)
                }
            }
        }
    }

    fun loadStandardOrderIntoField(standardOrder: String) {
        defaultOrder.text = standardOrder
    }

    fun loadIfsAndThensIntoField(ifsAndThens: HashMap<String, MutableList<String>>) {
        if (ifsAndThens["ifs"]!!.size != 0) {
            var ifs = ifsAndThens["ifs"]
            var thens = ifsAndThens["thens"]
            alternativeIf.text = ifs!!.elementAt(0)
            alternativeThen.text = thens!!.elementAt(0)
            if (ifsAndThens["ifs"]!!.size > 1) {
                for (i in 1..ifsAndThens.size - 1) {
                    addAlternativeFormula()
                    newIfFields[i - 1].text = ifs.elementAt(i)
                    newThenFields[i - 1].text = thens.elementAt(i)
                }
            }
        }
    }

    fun resetPersonalVariablePositions() {
        personalVariableHeight = 40.0
        addPersonalVariableButton.layoutY = addPersonalVariableButtonStandardY
    }

    fun resetAlternativeVariablePositions() {
        alternativeFormulaHeight = 40.0
        addAlternativeFormulaButton.layoutY = addAlternativeButtonStandardY

    }

    fun resetVariables(collectionOfVariablesToReset: MutableList<MutableList<out Node>>, pane: Pane, positionalReset: Unit) {
        if (collectionOfVariablesToReset[4].size > 0) {
            for (list in collectionOfVariablesToReset) {
                for (node in list)
                    pane.children.remove(node)
                list.clear()
            }
        }
    }

    fun resetBorderPane() {
        borderPane.layoutY = borderPaneStandardY
    }

    fun resetView() {
        clearErrorMessages()
        resetVariables(personalVariableRows, anchorPane, resetPersonalVariablePositions())
        resetBorderPane()
        resetVariables(alternativeFormulaRows, borderPane, resetAlternativeVariablePositions())
        resetSaveButtonYCoordinate()
    }

    fun setTextFieldsState(list: MutableList<TextField>, state: Boolean) {
        list.map { it -> it.isDisable = state }
    }

    fun setButtonState(list: MutableList<Button>, state: Boolean) {
        list.map { it -> it.isDisable = state }
    }

    fun disableFieldsIfDefault(selectedAgentName: String) {
        var standaloneFields = mutableListOf(alternativeIf, alternativeThen, personalVariableName, personalVariableFormula)
        var standaloneButtons = mutableListOf(addAlternativeFormulaButton, addPersonalVariableButton, buttonDeleteAgent, buttonSaveAgent)
        if (selectedAgentName.equals("Default")) {
            defaultOrder.isDisable = true
            setTextFieldsState(standaloneFields, true)
            setButtonState(standaloneButtons, true)

            setTextFieldsState(newIfFields, true)
            setTextFieldsState(newThenFields, true)
            setTextFieldsState(newPersonalVariableFormulas, true)
            setTextFieldsState(newPersonalVariableNames, true)
            setButtonState(newAlternativeFormulaDeleteButtons, true)
            setButtonState(newPersonalVariableDeleteButtons, true)

        } else {
            defaultOrder.isDisable = false
            setTextFieldsState(standaloneFields, false)
            setButtonState(standaloneButtons, false)
            setTextFieldsState(newIfFields, false)
            setTextFieldsState(newThenFields, false)
            setTextFieldsState(newPersonalVariableFormulas, false)
            setTextFieldsState(newPersonalVariableNames, false)
            setButtonState(newAlternativeFormulaDeleteButtons, false)
            setButtonState(newPersonalVariableDeleteButtons, false)
        }
    }


    fun loadAgentDataIntoFields() {
        val currentlyPicked = comboBoxSelectAgent.selectionModel.selectedItem
        when{
            !checkIfSelectedAgentCorrupt(currentlyPicked) -> {
                resetView()
                val allLinesInFile = StorageManager.getRules(currentlyPicked).split("\r\n")
                val loadedPersonalVariables = getPersonalVariablesFromLines(allLinesInFile)
                loadPersonalVariablesIntoFields(loadedPersonalVariables)
                val loadedStandardOrder = getStandardOrderFromLines(allLinesInFile)
                loadStandardOrderIntoField(loadedStandardOrder)
                val loadedIfsAndThens = getIfAndThenStatementsFromLines(allLinesInFile)
                loadIfsAndThensIntoField(loadedIfsAndThens)
                disableFieldsIfDefault(currentlyPicked)
            }
        }
    }


    fun deleteAgent() {
        if (comboBoxSelectAgent.selectionModel.selectedItem == null) showAlert("Selecteer eerst een agent die u wilt verwijderen")
        else {
            StorageManager.deleteAgent(comboBoxSelectAgent.selectionModel.selectedItem)
            state.onEnter()
        }
    }

    fun backToMenu() {
        state.notifyToGoBackToMainMenu()
    }

    fun saveGameAgent() {
        clearErrorMessages()
        var agentBuilder = AgentBuilder(AST())
        val personalVariables = GameAgentService.extractPersonalVariablesForSave(personalVariableName, newPersonalVariableNames, personalVariableFormula, newPersonalVariableFormulas)
        val standardOrder = GameAgentService.extractDefaultFormulaForSave(defaultOrder)
        val alternatives = GameAgentService.extractIfsAndThensForSave(alternativeIf, newIfFields, alternativeThen, newThenFields)
        distanceBetweenInputAndButtons = 0
        when {
            comboBoxSelectAgent.selectionModel.selectedItem != null -> {
                if (!checkIfInputIsValid(personalVariables, standardOrder, alternatives, agentBuilder)) {
                    StorageManager.createAgent(comboBoxSelectAgent.selectionModel.selectedItem, personalVariables + standardOrder + alternatives)
                    showAlert("De agent is succesvol opgeslagen")
                }
            }
            else -> {
                showAlert("Er is geen agent geselecteerd")
            }
        }
    }


    private fun checkIfInputIsValid(personalVariables: String, standardOrder: String, alternatives: String, agentBuilder: AgentBuilder): Boolean {
        when {
            !gameAgentInputChecker.checkEditInput(personalVariables, standardOrder, alternatives,true) -> {
                loadErrorsIntoView(gameAgentInputChecker.errors)
                return true
            }
            else -> {
                agentBuilder.parseString(personalVariables + standardOrder + alternatives)
                resetSaveButtonYCoordinate()
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
        if (personalVariableHeight > 40) {
            buttonSaveAgent.layoutY = saveButtonYCoordinate + personalVariableHeight + distanceBetweenInputAndButtons
            buttonDeleteAgent.layoutY = saveButtonYCoordinate + personalVariableHeight + distanceBetweenInputAndButtons
        } else if (alternativeFormulaHeight > 40) {
            buttonSaveAgent.layoutY = saveButtonYCoordinate + alternativeFormulaHeight + distanceBetweenInputAndButtons
            buttonDeleteAgent.layoutY = saveButtonYCoordinate + alternativeFormulaHeight + distanceBetweenInputAndButtons
        } else {
            buttonSaveAgent.layoutY = saveButtonYCoordinate + distanceBetweenInputAndButtons
            buttonDeleteAgent.layoutY = saveButtonYCoordinate + distanceBetweenInputAndButtons
        }
    }

    fun clearErrorMessages() {
        gameAgentInputChecker.errors.clear()
        errorMessageText.text = ""
    }

    fun resetSaveButtonYCoordinate() {
        val distance = 40
        var buttonHeight = saveButtonYCoordinate
        if (personalVariableHeight > distance)
            buttonHeight = saveButtonYCoordinate + personalVariableHeight - distance
        if (alternativeFormulaHeight > distance)
            buttonHeight = saveButtonYCoordinate + alternativeFormulaHeight - distance
        buttonSaveAgent.layoutY = buttonHeight
        buttonDeleteAgent.layoutY = buttonHeight
    }

    fun addPersonalVariable() {
        val deleteButtonX = 641.0
        val deleteButtonY = 228.0 + personalVariableHeight
        val newDeleteButton = GameAgentService.createButton(deleteButtonY, deleteButtonX, EventHandler { deletePersonalVariable() })
        val newSymbolText = Text(variableSymbol.layoutX, variableSymbol.layoutY + personalVariableHeight, variableSymbol.text)
        val newPersonalVariableName = GameAgentService.createTextField(personalVariableName.layoutY + personalVariableHeight, personalVariableName.layoutX, personalVariableName.prefHeight, personalVariableName.prefWidth)
        val newPersonalVariableFormula = GameAgentService.createTextField(personalVariableFormula.layoutY + personalVariableHeight, personalVariableFormula.layoutX, personalVariableFormula.prefHeight, personalVariableFormula.prefWidth)
        val newEqualText = Text(equalSymbol.layoutX, equalSymbol.layoutY + personalVariableHeight, equalSymbol.text)
        anchorPane.children.addAll(newDeleteButton, newSymbolText, newPersonalVariableFormula, newPersonalVariableName, newEqualText)
        errorMessageText.layoutY += spaceBetweenLines
        buttonDeleteAgent.layoutY += spaceBetweenLines
        buttonSaveAgent.layoutY += spaceBetweenLines
        addPersonalVariableButton.layoutY += spaceBetweenLines
        borderPane.layoutY += spaceBetweenLines
        personalVariableHeight += spaceBetweenLines
        newPersonalVariableDeleteButtons.add(newDeleteButton)
        newSymbolTexts.add(newSymbolText)
        newPersonalVariableNames.add(newPersonalVariableName)
        newPersonalVariableFormulas.add(newPersonalVariableFormula)
        newEqualTexts.add(newEqualText)
    }

    fun deletePersonalVariable() {
        buttonDeleteAgent.layoutY -= spaceBetweenLines
        buttonSaveAgent.layoutY -= spaceBetweenLines
        errorMessageText.layoutY -= spaceBetweenLines
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
        addAlternativeFormulaButton.layoutY += spaceBetweenLines
        errorMessageText.layoutY += spaceBetweenLines
        borderPane.children.addAll(newDeleteButton, newIfText, newThenText, newIfField, newThenField)
        buttonDeleteAgent.layoutY += spaceBetweenLines
        buttonSaveAgent.layoutY += spaceBetweenLines
        alternativeFormulaHeight += spaceBetweenLines
        newAlternativeFormulaDeleteButtons.add(newDeleteButton)
        newIfTexts.add(newIfText)
        newThenTexts.add(newThenText)
        newIfFields.add(newIfField)
        newThenFields.add(newThenField)
    }

    fun deleteAlternativeFormula() {
        errorMessageText.layoutY -= spaceBetweenLines
        addAlternativeFormulaButton.layoutY -= spaceBetweenLines
        alternativeFormulaHeight -= spaceBetweenLines
        buttonSaveAgent.layoutY -= spaceBetweenLines
        buttonDeleteAgent.layoutY -= spaceBetweenLines
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

    fun variableFormulaSelected() {
        focusedTextField = personalVariableFormula
    }

    fun defaultOrderSelected() {
        focusedTextField = defaultOrder
    }

}
