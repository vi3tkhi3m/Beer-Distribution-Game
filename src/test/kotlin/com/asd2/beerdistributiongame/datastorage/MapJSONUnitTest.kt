package com.asd2.beerdistributiongame.datastorage

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import mapJSON
import mapJSONFile
import mapJSONToFile
import org.junit.After
import org.junit.Assert
import org.junit.Test
import java.io.File

class MapJSONUnitTest{

    @JsonIgnoreProperties(ignoreUnknown = true)
    private data class TestClass(@get:JsonProperty("id") val name: String)

    @After
    fun clean() {
        val dir = File("src/test/resources/datastorageData/testFile.json")
        dir.delete()
    }

    @Test
    fun tesObjectFromJSONMapper() {
        val test: TestClass = mapJSON("""{"id":"123klasgian2o34"}""")
        Assert.assertEquals("123klasgian2o34", test.name)
    }

    @Test
    fun testObjectFromJSONFileMapper(){
        val test: TestClass = mapJSONFile("src/test/resources/datastorageData/factory.json")
        Assert.assertEquals("123klasgian2o34", test.name)
    }

    @Test
    fun testObjectToJSONFile(){
        val test = TestClass("Test")
        mapJSONToFile("testFile.json", "src/test/resources/datastorageData/",test)
        val test2: TestClass = mapJSONFile("src/test/resources/datastorageData/testFile.json")
        Assert.assertEquals("Test", test2.name)
    }
}