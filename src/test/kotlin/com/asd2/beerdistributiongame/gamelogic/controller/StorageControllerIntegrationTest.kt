package com.asd2.beerdistributiongame.gamelogic.controller

import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import org.junit.Assert
import org.junit.Test

class StorageControllerIntegrationTest : GameConfigMock() {

    private val storageController: StorageController = StorageController

    @Test
    fun testInitialize() {
        Assert.assertNotNull(storageController)
    }

    @Test
    fun saveGameConfig() {
    }

    @Test
    fun saveTurn() {
    }
}