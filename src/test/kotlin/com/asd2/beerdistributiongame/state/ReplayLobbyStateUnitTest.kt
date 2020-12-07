package com.asd2.beerdistributiongame.state

import com.asd2.beerdistributiongame.settings.Settings
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.unmockkAll
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class ReplayLobbyStateUnitTest {

    @Before
    fun before() {
        mockkObject(Settings)
        every { Settings.username } returns "123-123-123-123-125"
    }

    @Test
    fun getUsernameTest() {
        Assert.assertEquals("123-123-123-123-125", ReplayLobbyState().getUsername())
    }

    @After
    fun clean() {
       unmockkAll()
    }
}