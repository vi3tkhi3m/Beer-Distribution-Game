package com.asd2.beerdistributiongame.gameagent

import com.asd2.beerdistributiongame.frontend.inputCheckers.GameAgentInputChecker
import com.asd2.beerdistributiongame.gameagent.ast.AST
import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.TextField
import javafx.scene.layout.Pane
import kotlin.IndexOutOfBoundsException

class GameAgentService {

    companion object {
        val gameAgentInputChecker = GameAgentInputChecker()
        fun loopThroughFXMLListAndDeleteSelectedRow(list: MutableList<MutableList<out javafx.scene.Node>>, deleteButtonsList: MutableList<Button>, pane: Pane, spaceBetweenLines: Double) {
            var index = -1
            val iterator = deleteButtonsList.iterator()
            var i = 0
            while (iterator.hasNext()) {
                val value = iterator.next()
                if (value.isFocused) {
                    deleteFromPane(list, deleteButtonsList, pane, i)
                    iterator.remove()
                    index = i
                }
                if (index > -1 && i < deleteButtonsList.size)
                    for (items in list)
                        items.get(i).layoutY -= spaceBetweenLines
                i++
            }
        }

        private fun deleteFromPane(list: MutableList<MutableList<out javafx.scene.Node>>, deleteButtonsList: MutableList<Button>, pane: Pane, index: Int) {
            for (items in list) {
                pane.children.remove(items.get(index))
                if (items != deleteButtonsList)
                    items.removeAt(index)
            }
        }

        fun createButton(newLayoutY: Double, newLayoutX: Double, newOnAction: EventHandler<ActionEvent>): Button {
            val button = Button("Delete")
            button.layoutY = newLayoutY
            button.layoutX = newLayoutX
            button.onAction = newOnAction
            return button
        }

        fun createTextField(newLayoutY: Double, newLayoutX: Double, newPrefHeight: Double, newPrefWidth: Double): TextField {
            val textField = TextField()
            textField.layoutY = newLayoutY
            textField.layoutX = newLayoutX
            textField.prefHeight = newPrefHeight
            textField.prefWidth = newPrefWidth
            return textField
        }

        fun extractPersonalVariablesForSave(personalVariableName: TextField, newPersonalVariableNames: MutableList<TextField>, personalVariableFormula: TextField, newPersonalVariableFormulas: MutableList<TextField>): String {
            if (personalVariableName.text.equals(""))
                return ""
            var allVariableNames = mutableListOf<String>(personalVariableName.text)
            for (variableNameField in newPersonalVariableNames) {
                allVariableNames.add(variableNameField.text)
            }
            var allVariableFormulas = mutableListOf<String>(personalVariableFormula.text)
            for (variableFormulaField in newPersonalVariableFormulas) {
                allVariableFormulas.add(variableFormulaField.text)
            }
            var preparedVariables = allVariableNames.map { it }.map { addSymbols(it) }.toMutableList()
            for (i in 0 until preparedVariables.size) {
                preparedVariables[i] += allVariableFormulas[i] + "\r\n"
            }
            return preparedVariables.reduce { accumulator, new -> accumulator + new } + "\r\n"
        }

        fun addSymbols(name: String): String {
            return "@$name = "
        }

        fun extractIfsAndThensForSave(alternativeIf: TextField, newIfFields: MutableList<TextField>, alternativeThen: TextField, newThenFields: MutableList<TextField>): String {
            if (alternativeIf.text.equals(""))
                return ""
            var allIfStatements = mutableListOf<String>(alternativeIf.text)
            for (ifField in newIfFields) {
                allIfStatements.add(ifField.text)
            }
            var preparedIfStatements = allIfStatements.map { prepareIfForSaving(it) }
            var allThenStatements = mutableListOf<String>(alternativeThen.text)
            for (thenField in newThenFields) {
                allThenStatements.add(thenField.text)
            }
            var preparedThenStatements = allThenStatements.map { prepareThenForSaving(it) }
            var combined = ""
            for (i in 0 until preparedIfStatements.size) {
                combined = combined + preparedIfStatements[i] + preparedThenStatements[i]
            }
            return "\r\n$combined"
        }

        fun prepareIfForSaving(ifFormula: String): String {
            return "\r\nif\r\n[$ifFormula]"
        }

        fun prepareThenForSaving(thenFormula: String): String {
            return "\r\nthen\r\n[$thenFormula];"
        }

        fun extractDefaultFormulaForSave(defaultOrder: TextField): String {
            return "NewOrder = " + defaultOrder.text
        }

        fun checkIfSelectedAgentCorrupt(name: String): Boolean {
            gameAgentInputChecker.errors.clear()
            val allLinesInFile = StorageManager.getRules(name).split("\r\n")
            var personalVariableBlock = extractPersonalVariablesForCorruptionCheck(allLinesInFile)
            var alternativeBlock = extractAlternativesForCorruptionCheck(allLinesInFile)
            val standardOrder = getStandardOrderFromLines(allLinesInFile)
            when {
                standardOrder.equals("") -> {
                    alertUserAgentCorrupt()
                    return true
                }
                else -> {
                    when {
                        !gameAgentInputChecker.checkEditInput(personalVariableBlock, standardOrder, alternativeBlock, false) -> {
                            alertUserAgentCorrupt()
                            return true
                        }
                        checkAgentBuilderForErrors(name) -> {
                            alertUserAgentCorrupt()
                            return true
                        }
                    }
                }
            }
            return false
        }

        private fun checkAgentBuilderForErrors(name: String): Boolean {
            val agentBuilder = AgentBuilder(AST())
            agentBuilder.parseString(StorageManager.getRules(name))
            when {
                agentBuilder.errors.isNotEmpty() -> {
                    return true
                }
            }
            return false
        }

        private fun extractAlternativesForCorruptionCheck(allLinesInFile: List<String>): String {
            var alternativeBlock = ""
            val alternativesLines = allLinesInFile.filter { it: String -> filterOnCharacter(it, "[") }
            if (alternativesLines.isNotEmpty()) {
                alternativeBlock = alternativesLines.reduce { accumulator, new -> accumulator + new + "\r\n" }
            }
            return alternativeBlock
        }

        private fun extractPersonalVariablesForCorruptionCheck(allLinesInFile: List<String>): String {
            val personalVariableLines = allLinesInFile.filter { it: String -> filterOnCharacter(it, "@") }
            var personalVariableBlock = ""
            if (personalVariableLines.isNotEmpty()) {
                personalVariableBlock = personalVariableLines.reduce { accumulator, new -> accumulator + new + "\r\n" } + "\r\n"
            }
            return personalVariableBlock
        }

        fun getStandardOrderFromLines(lines: Collection<String>): String {
            try {
                return lines.filter { it -> filterOnCharacter(it, "N") }[0].substring(11)
            } catch (ignored: IndexOutOfBoundsException) {
                return ""
            }
        }

        fun filterOnCharacter(line: String, character: String): Boolean {
            if (line.isNotEmpty()) {
                if (line[0].toString().equals(character))
                    return true
            }
            return false
        }

        fun getPersonalVariablesFromLines(lines: Collection<String>): HashMap<String, MutableList<String>> {
            val personalVariableLines = lines.filter { it: String -> filterOnCharacter(it, "@") }
            val personalVariableNames = mutableListOf<String>()
            val personalVariableFormulas = mutableListOf<String>()
            for (lines in personalVariableLines) {
                var personalVariables = lines.split(" = ")
                personalVariableNames.add(personalVariables[0])
                personalVariableFormulas.add(personalVariables[1])
            }
            var endMap = hashMapOf<String, MutableList<String>>()
            endMap.put("names", personalVariableNames)
            endMap.put("formulas", personalVariableFormulas)
            return endMap
        }

        fun getIfAndThenStatementsFromLines(lines: Collection<String>): HashMap<String, MutableList<String>> {
            var endmap = hashMapOf<String, MutableList<String>>()
            var ifs = mutableListOf<String>()
            var thens = mutableListOf<String>()
            for (i in 0..lines.size - 1) {
                if (lines.elementAt(i).isNotEmpty()) {
                    if (lines.elementAt(i)[0].toString().equals("i")) {
                        ifs.add(lines.elementAt(i + 1).substring(1, lines.elementAt(i + 1).length - 1))
                    } else if (lines.elementAt(i)[0].toString().equals("t")) {
                        thens.add(lines.elementAt(i + 1).substring(1, lines.elementAt(i + 1).length - 2))
                    }
                }
            }
            endmap.put("ifs", ifs)
            endmap.put("thens", thens)
            return endmap
        }

        private fun alertUser(message: String) {
            val alert = Alert(Alert.AlertType.ERROR, message)
            val okButton = alert.dialogPane.lookupButton(ButtonType.OK) as Button
            okButton.id = "alertOK"
            alert.showAndWait()
        }

        private fun alertUserAgentCorrupt() = alertUser("De geselecteerde gameagent is corrupt")


    }
}