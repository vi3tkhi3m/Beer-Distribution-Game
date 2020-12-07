package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.state.State
import javafx.scene.Cursor
import javafx.scene.control.Alert
import javafx.scene.control.Button
import javafx.scene.control.ButtonType
import javafx.scene.control.TextField
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

abstract class Handler<T : State> {

	protected val state: T get() = Context.activeState as T

	protected companion object ViewHelper {
		fun addError(textField: TextField) {
			textField.styleClass.add("error")
		}

		fun removeError(textField: TextField) {
			textField.styleClass.removeAll("error")
		}
	}

    var loading: Boolean = false
        set(loading) {
            Context.stage.scene.cursor = if(loading) Cursor.WAIT else Cursor.DEFAULT
            Context.stage.scene.root.childrenUnmodifiable.forEach {
                it.isDisable = loading
            }
            field = loading
        }

    fun showAlert(message: String, alertType: Alert.AlertType = Alert.AlertType.INFORMATION, title: String = "Alert", headerText: String? = null){
        val alert = Alert(alertType)
        val okButton = alert.dialogPane.lookupButton(ButtonType.OK) as Button
        okButton.id = "alertOK"
        alert.title = title
        alert.headerText = headerText
        alert.contentText = message

        alert.showAndWait()
    }

    fun process(function: suspend () -> Unit) = GlobalScope.launch(Dispatchers.Main){
        loading = true
        function()
        loading = false
    }

	abstract fun getViewFile(): String
}