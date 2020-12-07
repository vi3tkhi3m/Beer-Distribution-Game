package com.asd2.beerdistributiongame.frontend.inputCheckers

import com.asd2.beerdistributiongame.gameagent.storage.StorageManager
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType

class GameAgentInputChecker {

    var personalVariablesList: MutableList<String> = mutableListOf("NewOrder")
    val defaultVariablesList: Array<String> = arrayOf("Order", "ProductionCost", "IncomingDelivery", "Backlog", "Stock", "Budget", "StockCost", "BacklogCost", "IncomingOrder")
    val errorMessage = "Er zijn errors gevonden, los deze op zodat de agent correct kan worden opgeslagen."
    var errors: MutableList<String> = mutableListOf()

    fun checkCreateInput(personalVariables: String, defaultFormula: String, alternativeFormulas: String, agentNameInput: String): Boolean {
        errors.clear()
        when {
            (StorageManager.agentExists(agentNameInput)) ->
                errors.add("The used agentname already exists. Please change the name or edit the existing agent.")

            (!checkNotEmpty(agentNameInput, false)) ->
                errors.add("The name of an agent can't be empty!" + "\n")

            (!checkNotEmpty(defaultFormula, true)) ->
                errors.add("The rules of an agent can't be empty!" + "\n")

            (!checkNoIllegalChars(agentNameInput) || !checkNoIllegalChars(personalVariables) || !checkNoIllegalChars(defaultFormula) || !checkNoIllegalChars(alternativeFormulas)) ->
                errors.add("The rules contain symbols that can't be used!" + "\n")

            (!checkPersonalVariablesCorrect(personalVariables.replace("\\s".toRegex(), ""))) ->
                errors.add("Something is wrong with the personal variables!" + "\n")

            (!checkAlternativeFormulasCorrect(alternativeFormulas.replace("\\s".toRegex(), ""))) ->
                errors.add("Something is wrong with the alternative formula(s)" + "\n")
        }
        if (!errors.isEmpty())
            alertUser(errorMessage)
        else
            return true

        return false
    }

    private fun checkPersonalVariablesCorrect(personalVariables: String): Boolean {
        var nameCharString = ""
        var valueCharString = ""
        var inName = false
        var inValue = false
        for (char in personalVariables) {
            when {
                (char.toString() == "@" || char.toString() == "=") -> {
                    if (nameCharString != "") {
                        personalVariablesList.add(nameCharString)
                        nameCharString = ""
                    }
                    if (char.toString() == "@") {
                        inName = true
                        inValue = false
                    } else {
                        valueCharString = ""
                        inValue = true
                        inName = false
                    }
                }
                inName -> {
                    if (!checkAlphabet(char)) return false
                    else nameCharString += char
                }

                inValue -> {
                    if (checkAlphabet(char)) valueCharString += char
                    else
                        if (valueCharString != "" && valueCharString !in defaultVariablesList)
                            return false
                        else valueCharString = ""
                }
            }
        }
        if (nameCharString != "") {
            personalVariablesList.add(nameCharString)
        }
        return true
    }

    private fun checkAlternativeFormulasCorrect(alternativeFormulas: String): Boolean {
        var variableString = ""
        for (char in alternativeFormulas) {
            when {
                (checkAlphabet(char)) -> variableString += char
                else -> {
                    if (variableString !in personalVariablesList && variableString !in defaultVariablesList && variableString != "" && variableString != "if" && variableString != "then") return false
                    variableString = ""
                }
            }
        }
        return true
    }

    fun checkEditInput(personalVariables: String, defaultFormula: String, alternativeFormulas: String, showError : Boolean): Boolean {
        when {
            (!checkNotEmpty(defaultFormula, true)) ->
                errors.add("There are empty fields!" + "\n")

            (!checkNoIllegalChars(personalVariables) || !checkNoIllegalChars(defaultFormula) || !checkNoIllegalChars(alternativeFormulas)) ->
                errors.add("The rules contain symbols that can't be used!" + "\n")

            (!checkPersonalVariablesCorrect(personalVariables.replace("\\s".toRegex(), ""))) ->
                errors.add("Something is wrong with the personal variables!" + "\n")

            (!checkAlternativeFormulasCorrect(alternativeFormulas.replace("\\s".toRegex(), ""))) ->
                errors.add("Something is wrong with the alternative formula(s)" + "\n")
            else ->
                return true
        }
        if (!errors.isEmpty() && showError)
            alertUser(errorMessage)

        return false
    }

    fun checkAlphabet(c: Char): Boolean = c in 'a'..'z' || c in 'A'..'Z'

    fun checkNotEmpty(agentInput: String, isDefaultFormula: Boolean): Boolean {
        if (isDefaultFormula) {
            if (agentInput.trim() != "NewOrder =") return true
        } else {
            if (agentInput.trim() != "") return true
        }
        return false
    }

    private fun checkNoIllegalChars(agentInput: String): Boolean {
        if (!agentInput.matches("^[a-zA-Z0-9()<>+*/^√=; @\r\n\\-\\[\\]]+$".toRegex()) && agentInput != "")
            return false
        return true
    }

    private fun alertUser(message: String) {
        val alert = Alert(Alert.AlertType.ERROR, message)
        val okButton = alert.dialogPane.lookupButton(ButtonType.OK) as Button
        okButton.id = "alertOK"
        alert.showAndWait()
    }
}
