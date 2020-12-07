package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.settings.Translations
import com.asd2.beerdistributiongame.state.SettingsState
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.text.Text

class SettingsHandler : Handler<SettingsState>() {

    private val languageLocaleMap = hashMapOf("English" to arrayOf("en", "US"), "Nederlands" to arrayOf("nl", "NL"), "Deutsch" to arrayOf("de", "DE"))
    private val countryMap = hashMapOf("US" to "English", "NL" to "Nederlands", "DE" to "Deutsch")

    @FXML lateinit var gameLanguage: ComboBox<String>
    @FXML lateinit var backToMenu: Button
    @FXML lateinit var playerName: TextField
    @FXML lateinit var changePlayerName: Button
    @FXML lateinit var changeGameLanguageText: Label
    @FXML lateinit var changePlayerNameLabel: Label
    @FXML lateinit var userNameError: Label
    @FXML lateinit var pageTitle: Text

    @FXML
    fun initialize() {
        applyLocalization()
        changePlayerName.isDisable = !Settings.validateUsername(playerName.text)

        if(Settings.locale.isNotEmpty()){
            gameLanguage.selectionModel.select(countryMap[Settings.locale[1]])
        }
    }

    override fun getViewFile() = "views/Settings.fxml"

    fun onBackToMainMenuPressed() {
        state.notifyToGoBackToMainMenu()
    }

    fun onChangePlayerNamePressed() {
        if (Settings.validateUsername(playerName.text)) {
            Settings.changeUsername(playerName.text)
            showAlert("Naam veranderd naar ${playerName.text}")
        } else {
            addError(playerName)
            userNameError.isVisible = true
            changePlayerName.isDisable = true
        }
    }

    fun onChangePlayerNameChanged() {
        if (Settings.validateUsername(playerName.text)) {
            removeError(playerName)
            userNameError.isVisible = false
            changePlayerName.isDisable = false
        } else {
            addError(playerName)
            userNameError.isVisible = true
            changePlayerName.isDisable = true
        }
    }

    fun onLanguageChange() {
        Translations.switchLanguage(languageLocaleMap[gameLanguage.value]!!.toList())
        applyLocalization()
    }

    private fun applyLocalization() {
        pageTitle.text = Translations.get("settingsHeader")
        backToMenu.text = Translations.get("mainMenuButton")
        changePlayerName.text = Translations.get("changePlayerNameButton")
        changePlayerNameLabel.text = Translations.get("changePlayerNameText")
        changeGameLanguageText.text = Translations.get("changeGameLanguage")
    }

    fun onEnter() = onChangePlayerNamePressed()
}