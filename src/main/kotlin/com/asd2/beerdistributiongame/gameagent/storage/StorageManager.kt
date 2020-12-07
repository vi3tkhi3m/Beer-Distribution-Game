package com.asd2.beerdistributiongame.gameagent.storage

import java.io.File

object StorageManager {

    var path : String = "data/gameagents"

    fun createAgent(name : String, rules : String) {
        rules.trim()
        val fileName = "$path/$name"
        val myfile = File(fileName)

        myfile.printWriter().use { out ->
            out.print(rules)
        }
    }

    fun editAgent(name : String, rules : String) {
        rules.trim()
        var filename = "$path/$name"
        var myfile = File(filename)
        myfile.deleteRecursively()
        myfile.printWriter().use { out -> out.print(rules) }
    }

    fun agentExists(name : String) : Boolean {
        if (name.equals(""))
            return false
        if (name.equals("Default")){
            return true;
        }
        ensureFolderExists()
        val filename = "$path/$name"
        var file = File(filename)

        return file.exists()
    }

    fun getAgentNames() : List<String> {
        ensureFolderExists()
        var agentsList : MutableList<String> = mutableListOf()
        File("$path/").walkBottomUp().forEach {
            if (it.name != ".gitignore") {
                agentsList.add(it.name)
            }
        }
        agentsList.remove("gameagents")
        return agentsList
    }

    fun deleteAgent(name : String) {
        var filename = "$path/$name"
        var myfile = File(filename)
        myfile.deleteRecursively()
    }

    fun getRules(agentName : String): String = File("$path/$agentName").readText(Charsets.UTF_8)

    private fun ensureFolderExists() {
        val file = File(path)
        if (!file.exists()) {
            file.mkdirs()
        }
    }

    fun createDefault(){
        val defaultFile = File("src${File.separator}main${File.separator}resources${File.separator}agent${File.separator}Default")
        val defaultAgent = File("data${File.separator}gameagents${File.separator}Default")
        defaultFile.copyTo(defaultAgent, true)
    }
}