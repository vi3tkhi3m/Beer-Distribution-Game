package com.asd2.beerdistributiongame.gamelogic.datastructure

import com.asd2.beerdistributiongame.gamelogic.GameConfigMock
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import org.junit.Assert
import org.junit.Test

class ChainStructureUnitTest : GameConfigMock() {

    @Test
    fun testInitialize() {
        Assert.assertNotNull(chainStructureLinear)
    }

    @Test
    fun testAddPlayerRole() {
        val roleKey = "Wholesaler1"
        val role = Roles.WHOLESALE
        val parentsPlayerP = ArrayList<String>()
        parentsPlayerP.add("Distributor0")
        val childrenPlayerP = ArrayList<String>()
        childrenPlayerP.add("Retailer0")

        chainStructureLinear.addPlayerRole(roleKey, role, parentsPlayerP, childrenPlayerP)
        val node = chainStructureLinear.playerRoles[roleKey]

        Assert.assertNotNull(node)
        if (node != null) {
            Assert.assertEquals(role, node.representative)
            Assert.assertEquals(parentsPlayerP, node.parents)
            Assert.assertEquals(childrenPlayerP, node.children)
        }
    }

    @Test
    fun testGetPlayerRoles() {
        val roleKey = "Factory0"
        val role = chainStructureLinear.playerRoles[roleKey]!!.representative
        val parentsPlayerD = chainStructureLinear.playerRoles[roleKey]!!.parents
        val childrenPlayerD = chainStructureLinear.playerRoles[roleKey]!!.children

        val node = chainStructureLinear.playerRoles[roleKey]

        Assert.assertNotNull(node)
        if (node != null) {
            Assert.assertEquals(role, node.representative)
            Assert.assertEquals(parentsPlayerD, node.parents)
            Assert.assertEquals(childrenPlayerD, node.children)
        }
    }

    @Test
    fun testModifyPlayerRole() {
        val roleKey = "Wholesaler0"
        val role = Roles.WHOLESALE
        val parentsPlayerO = ArrayList<String>()
        parentsPlayerO.add("Distributor1")
        val childrenPlayerO = ArrayList<String>()
        childrenPlayerO.add("Retailer1")

        val node = chainStructureLinear.playerRoles[roleKey]
        node!!.representative = role
        node.parents = parentsPlayerO
        node.children = childrenPlayerO
        chainStructureLinear.playerRoles[roleKey] = node

        Assert.assertNotNull(node)
        if (node != null) {
            Assert.assertEquals(role, node.representative)
            Assert.assertEquals(parentsPlayerO, node.parents)
            Assert.assertEquals(childrenPlayerO, node.children)
        }
    }
}
