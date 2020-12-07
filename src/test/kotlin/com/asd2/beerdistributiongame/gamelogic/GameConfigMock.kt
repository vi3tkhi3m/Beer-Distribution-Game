package com.asd2.beerdistributiongame.gamelogic

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.*
import com.asd2.beerdistributiongame.gamelogic.datastructure.ChainStructure
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.Order

abstract class GameConfigMock {

    val gameConfigLinear: GameConfig
    val rolesInGameLinear: RolesInGame
    val falseRolesInGameLinear: RolesInGame
    val chainStructureLinear = ChainStructure<Roles>()

    init {
        rolesInGameLinear = RolesInGame()
        val playerRoles = rolesInGameLinear.playerRoles
        playerRoles["Retailer0"] = RoleOwner(Roles.RETAILER, "PlayerA")
        playerRoles["Wholesaler0"] = RoleOwner(Roles.WHOLESALE, "PlayerB")
        playerRoles["Distributor0"] = RoleOwner(Roles.WAREHOUSE, "PlayerC")
        playerRoles["Factory0"] = RoleOwner(Roles.FACTORY, "PlayerD")

        falseRolesInGameLinear = RolesInGame()
        val falsePlayerRoles = falseRolesInGameLinear.playerRoles
        falsePlayerRoles["Retailer0"] = RoleOwner(Roles.RETAILER, "PlayerA")
        falsePlayerRoles["Wholesaler0"] = RoleOwner(Roles.WHOLESALE, "PlayerB")
        falsePlayerRoles["Distributor0"] = RoleOwner(Roles.WAREHOUSE, "PlayerC")
        falsePlayerRoles["Retailer1"] = RoleOwner(Roles.NONE, "PlayerD")

        gameConfigLinear = GameConfig(
                gameName = "TestGame",
                gameID = "asdfux32-1234-4651-b6e6-sdkalske2310",
                gameMode = GameMode.LINEAR,
                overviewType = OverviewType.LOCAL,
                initialStock = 10,
                initialBudget = 100,
                turnDuration = 60,
                backlogCost = 2,
                stockCost = 1,
                productionCost = 1,
                maxTurns = 40,
                infiniteTurns = false,
                quantityOfFactories = 1,
                quantityOfWarehouses = 1,
                quantityOfWholesales = 1,
                quantityOfRetailers = 1,
                retailProfit = 5,
                wholesaleProfit = 4,
                warehouseProfit = 3,
                factoryProfit = 3,
                minCustomerDemand = 1,
                maxCustomerDemand = 20
        )

        val childrenFactory0 = ArrayList<String>()
        childrenFactory0.add("Distributor0")
        chainStructureLinear.addPlayerRole(roleKey = "Factory0", role = Roles.FACTORY, children = childrenFactory0)

        val parentsDistributor0 = ArrayList<String>()
        parentsDistributor0.add("Factory0")
        val childrenDistributor0 = ArrayList<String>()
        childrenDistributor0.add("Wholesaler0")
        chainStructureLinear.addPlayerRole(roleKey = "Distributor0", role = Roles.WAREHOUSE, parents = parentsDistributor0, children = childrenDistributor0)

        val parentsWholesaler0 = ArrayList<String>()
        parentsWholesaler0.add("Distributor0")
        val childrenWholesaler0 = ArrayList<String>()
        childrenWholesaler0.add("Retailer0")
        chainStructureLinear.addPlayerRole(roleKey = "Wholesaler0", role = Roles.WHOLESALE, parents = parentsWholesaler0, children = childrenWholesaler0)

        val parentsRetailer0 = ArrayList<String>()
        parentsRetailer0.add("Wholesaler0")
        chainStructureLinear.addPlayerRole(roleKey = "Retailer0", role = Roles.RETAILER, parents = parentsRetailer0)

        createPlayerRolesWithUsernameAndIPLineair()
    }

    private fun createPlayerRolesWithUsernameAndIPLineair() {
        val playerA = Player("PlayerA", "testUserA", "testIpA", true)
        val playerB = Player("PlayerB", "testUserB", "testIpB", true)
        val playerC = Player("PlayerC", "testUserC", "testIpC", true)
        val playerD = Player("PlayerD", "testUserD", "testIpD", true)

        if(!Context.players.contains(playerA)) {
            Context.players.add(playerA)
        }
        if(!Context.players.contains(playerB)) {
            Context.players.add(playerB)
        }
        if(!Context.players.contains(playerC)) {
            Context.players.add(playerC)
        }
        if(!Context.players.contains(playerD)) {
            Context.players.add(playerD)
        }
    }

    fun createPlacedOrderHashMapStub(amount: Int = 3) {
        Context.beerDistributionGame!!.supplyChain.createCustomerDemand(amount, amount)
        Context.beerDistributionGame!!.supplyChain.chainStructure.playerRoles.forEach {
            Context.beerDistributionGame!!.gameFlow.placedOrders[it.key] = Order(it.key, it.value.parents[0],amount)
        }
    }
}