package com.asd2.beerdistributiongame.settings

import java.util.*

object Translations {
    const val TRANSLATION_PATH = "translations/language"

    private var resourceBundle = ResourceBundle.getBundle(TRANSLATION_PATH)!!

    init {
        val localeSettings = Settings.locale
        var locale = Locale.getDefault()

        if(localeSettings.isNotEmpty()) {
            locale = Locale(localeSettings[0], localeSettings[1])
        }

        resourceBundle = ResourceBundle.getBundle(TRANSLATION_PATH, locale)
    }

    fun get(key: String): String = resourceBundle.getString(key)

    fun switchLanguage(localeInfo: List<String>) {
        Settings.locale = localeInfo
        resourceBundle = ResourceBundle.getBundle(TRANSLATION_PATH, Locale(localeInfo[0], localeInfo[1]))
    }

}