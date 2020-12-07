package com.asd2.beerdistributiongame.settings

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*

object Settings {

    private const val path = "data/settings.properties"

    private val properties = Properties()

    var username: String
        get() = properties.getProperty("username") ?: "USERNAME"
        private set(value) = savePropertyValueToFile("username", value)

    var uuid: String
        get() = properties.getProperty("uuid") ?: generateUUID()
        private set(value) = savePropertyValueToFile("uuid", value)

    var locale: List<String>
        get() = if (properties.getProperty("locale") != null) {
            properties.getProperty("locale").split(", ")
        } else emptyList()
        set(value) = savePropertyValueToFile("locale", value.joinToString())

    var lastSuccessfulIp: String
        get() = properties.getProperty("lastSuccessfulIp") ?: ""
        set(value) = savePropertyValueToFile("lastSuccessfulIp", value)

    init {
        createDataFolderIfNotExists()
        loadProperties()
    }

    fun fileExists(): Boolean = File(path).exists()

    fun containsUsername(): Boolean = properties.getProperty("username") != null

    private fun generateUUID(): String {
        Settings.uuid = UUID.randomUUID().toString()
        return Settings.uuid
    }

    private fun createDataFolderIfNotExists() {
        if (Files.notExists(Paths.get("data"))) {
            File("data").mkdirs()
        }
    }

    private fun loadProperties() {
        if (fileExists())
            properties.load(FileInputStream(path))
    }

    private fun savePropertyValueToFile(key: String, value: String?) {
        properties.setProperty(key, value)
        properties.store(FileOutputStream(path), null)
    }

    fun changeUsername(username: String) {
        if (!Settings.validateUsername(username)) {
            throw IllegalArgumentException("The username length should be between 1 and 24 characters")
        }
        Settings.username = username
    }

    fun validateUsername(username: String) = username.length in 1..24
}