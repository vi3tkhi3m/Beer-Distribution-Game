package com.asd2.beerdistributiongame.network.util

import com.asd2.beerdistributiongame.network.NetworkResponse
import mapJSON
import org.junit.Assert.assertEquals
import org.junit.Test

internal class HelperUnitTest {

    data class HelperUnitTestDTO(
            val string: String,
            val arrayOfStrings: List<String>
    )

    @Test
    fun `hostUrl - Returns a valid host url`() {
        assertEquals(APP_PORT, NetworkContext.HOST_PORT)
        assertEquals("http://$LOCALHOST:$APP_PORT/host/test", hostUrl("test"))
    }

    @Test
    fun `clientUrl - Returns a valid host url`() {
        assertEquals("http://$LOCALHOST:$APP_PORT/client/test", clientUrl(LOCALHOST, "test"))
    }

    @Test
    fun `toJson - Returns a JSON object of an object`() {
        val listOfStrings: MutableList<String> = mutableListOf()
        val string = "text"
        for (i in 1..10) listOfStrings.add("String $i")
        val dtoInJson = toJson(HelperUnitTestDTO(string, listOfStrings))
        val dto: HelperUnitTestDTO = mapJSON(dtoInJson)
        assertEquals(string, dto.string)
        for (i in 1..dto.arrayOfStrings.size) assertEquals(listOfStrings[i - 1], dto.arrayOfStrings[i - 1])
    }

    @Test
    fun `getHostCannotBeReachedNetworkResponse - success - Creates a NetworkResponse that returns a false and host cannot be reached message`(){
        val networkResponse = NetworkResponse(false, HOST_CANNOT_BE_REACHED)
        assertEquals(networkResponse,getHostCannotBeReachedNetworkResponse())
    }

    @Test
    fun `getClientCannotBeReachedNetworkResponse - success - Creates a NetworkResponse that returns a false and client cannot be reached message`(){
        val networkResponse = NetworkResponse(false, CLIENT_CANNOT_BE_REACHED)
        assertEquals(networkResponse,getClientCannotBeReachedNetworkResponse())
    }
}