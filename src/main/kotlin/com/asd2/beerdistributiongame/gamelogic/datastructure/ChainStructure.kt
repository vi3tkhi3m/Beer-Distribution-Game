package com.asd2.beerdistributiongame.gamelogic.datastructure

class ChainStructure<T> {
    val playerRoles: HashMap<String, ChainStructureNode<T>> = HashMap()

    fun addPlayerRole(roleKey: String, role: T, parents: ArrayList<String> = ArrayList(), children: ArrayList<String> = ArrayList()) {
        playerRoles[roleKey] = ChainStructureNode(role, parents, children)
    }
}