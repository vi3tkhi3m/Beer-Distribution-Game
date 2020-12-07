package com.asd2.beerdistributiongame.settings

import org.junit.Test

class TranslationsIntegrationTest {

    @Test
    fun get_text_from_resource_bundle() {
        Translations.switchLanguage(arrayOf("en", "US").toList())
        assert(Translations.get("hostGameButton") == "Host game")
    }

    @Test
    fun switch_language() {
        Translations.switchLanguage(arrayOf("en", "US").toList())
        assert(Settings.locale[1] == "US")
        Translations.switchLanguage(arrayOf("nl", "NL").toList())
        assert(Settings.locale[1] == "NL")
    }
}