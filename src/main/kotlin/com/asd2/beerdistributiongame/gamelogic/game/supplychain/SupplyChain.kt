package com.asd2.beerdistributiongame.gamelogic.game.supplychain

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.gamelogic.configuration.IllegalChainStructureException
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.gamelogic.datastructure.ChainStructure
import com.asd2.beerdistributiongame.gamelogic.datastructure.ChainStructureNode
import com.asd2.beerdistributiongame.gamelogic.game.supplychain.chainrole.*

class SupplyChain {

    val gameConfig = Context.gameConfig
    val rolesInGame = Context.rolesInGame
    val chainStructure: ChainStructure<SupplyChainRole> = ChainStructure()
    val factoryQueue: ArrayList<Delivery> = ArrayList()

    init {
        val playerRoles = rolesInGame.playerRoles
        val stockCost = gameConfig.stockCost
        val backlogCost = gameConfig.backlogCost
        val productionCost = gameConfig.productionCost

        if (playerRoles.size != 4) {
            throw IllegalChainStructureException("A linear game must have exactly four roles.")
        }

        lateinit var retailer: Retailer
        lateinit var wholesale: Wholesale
        lateinit var warehouse: Warehouse
        lateinit var factory: Factory

        for ((roleKey, roleOwner) in playerRoles) {
            when (roleOwner.role) {
                Roles.RETAILER -> {
                    retailer = Retailer(roleKey, roleOwner.playerId, stockCost, backlogCost, gameConfig.wholesaleProfit)
                }
                Roles.WHOLESALE -> {
                    wholesale = Wholesale(roleKey, roleOwner.playerId, stockCost, backlogCost, gameConfig.warehouseProfit)
                }
                Roles.WAREHOUSE -> {
                    warehouse = Warehouse(roleKey, roleOwner.playerId, stockCost, backlogCost, gameConfig.factoryProfit)
                }
                Roles.FACTORY -> {
                    factory = Factory(roleKey, roleOwner.playerId, stockCost, backlogCost, productionCost)
                }
                Roles.NONE -> {
                    throw IllegalChainStructureException("Every Player must have a Role.")
                }
            }
        }

        val rolesArray = arrayListOf(retailer, wholesale, warehouse, factory)
        rolesArray.forEach {
            it.stock = gameConfig.initialStock
            it.budget = gameConfig.initialBudget
            when (it) {
                is Retailer -> it.deliveryGain = gameConfig.retailProfit
                is Wholesale -> it.deliveryGain = gameConfig.wholesaleProfit
                is Warehouse -> it.deliveryGain = gameConfig.warehouseProfit
                is Factory -> it.deliveryGain = gameConfig.factoryProfit
            }
        }

        chainStructure.addPlayerRole(roleKey = retailer.roleKey, role = retailer, parents = arrayListOf(wholesale.roleKey))
        chainStructure.addPlayerRole(roleKey = wholesale.roleKey, role = wholesale, parents = arrayListOf(warehouse.roleKey), children = arrayListOf(retailer.roleKey))
        chainStructure.addPlayerRole(roleKey = warehouse.roleKey, role = warehouse, parents = arrayListOf(factory.roleKey), children = arrayListOf(wholesale.roleKey))
        chainStructure.addPlayerRole(roleKey = factory.roleKey, role = factory, parents = arrayListOf(factory.roleKey), children = arrayListOf(warehouse.roleKey))
    }

    fun createCustomerDemand(minCustomerDemand: Int, maxCustomerDemand: Int) {
        chainStructure.playerRoles.forEach {
            if (it.value.representative is Retailer) {
                val customerDemand = (minCustomerDemand..maxCustomerDemand).random()
                Context.beerDistributionGame!!.gameFlow.placeOrder(Order("CustomerOf${it.value.representative.roleKey}", it.value.representative.roleKey, customerDemand))
                it.value.representative.incomingOrder = hashMapOf(Pair("CustomerOf${it.value.representative.roleKey}", customerDemand))
            }
        }
    }

    fun updateChainFlow(placedOrders: HashMap<String, Order>) {
        val factories = ArrayList<ChainStructureNode<SupplyChainRole>>()
        val warehouses = ArrayList<ChainStructureNode<SupplyChainRole>>()
        val wholesales = ArrayList<ChainStructureNode<SupplyChainRole>>()
        val retailers = ArrayList<ChainStructureNode<SupplyChainRole>>()

        for (role in chainStructure.playerRoles) {
            when (role.value.representative) {
                is Factory -> {
                    factories.add(role.value)
                }
                is Warehouse -> {
                    warehouses.add(role.value)
                }
                is Wholesale -> {
                    wholesales.add(role.value)
                }
                is Retailer -> {
                    retailers.add(role.value)
                }
            }
        }

        resetIncomingDeliveries()
        updateFactoryQueue()
        factories.forEach { updateSupplyChainRole(it, placedOrders) }
        warehouses.forEach { updateSupplyChainRole(it, placedOrders) }
        wholesales.forEach { updateSupplyChainRole(it, placedOrders) }
        retailers.forEach { updateSupplyChainRole(it, placedOrders) }
    }

    private fun updateSupplyChainRole(role: ChainStructureNode<SupplyChainRole>, placedOrders: HashMap<String, Order>) {
        deliverGoods(role)
        updateStockBasedOnIncomingDelivery(role)
        resetIncomingOrders(role)
        processOrdersInBacklog(role)

        for (order in placedOrders.values) {
            updateOutgoingOrderAndBudget(role, order)
            processIncomingOrders(role, order)
        }

        updateBudgetBasedOnOutgoingDelivery(role)
        updateBudgetBasedOnStock(role)
        updateBudgetBasedOnBacklog(role)
    }

    private fun deliverGoods(role: ChainStructureNode<SupplyChainRole>) {
        for ((roleKey, amount) in role.representative.outgoingDelivery) {
            if (role.representative !is Retailer) {
                chainStructure.playerRoles[roleKey]!!.representative.incomingDelivery += amount
            }
            role.representative.outgoingDelivery.replace(roleKey, 0)
        }
    }

    private fun updateFactoryQueue() {
        factoryQueue.forEach {
            if (it.turnsInQueue-- <= 0) {
                chainStructure.playerRoles[it.destination]!!.representative.incomingDelivery += it.amount
            }
        }
        factoryQueue.removeIf { t -> t.turnsInQueue <= 0 }
    }

    private fun updateStockBasedOnIncomingDelivery(role: ChainStructureNode<SupplyChainRole>) {
        role.representative.stock += role.representative.incomingDelivery
    }

    private fun resetIncomingOrders(role: ChainStructureNode<SupplyChainRole>) {
        role.representative.incomingOrder.forEach {
            role.representative.incomingOrder[it.key] = 0
        }
    }

    private fun resetIncomingDeliveries() {
        chainStructure.playerRoles.forEach {
            it.value.representative.incomingDelivery = 0
        }
    }

    private fun updateOutgoingOrderAndBudget(role: ChainStructureNode<SupplyChainRole>, order: Order) {
        if (role.representative.roleKey == order.origin) {
            if (role.representative is Factory && order.amount > 0) {
                addOrderToFactoryQueue(order)
            }
            role.representative.outgoingOrder = order.amount
            role.representative.budget -= order.amount * role.representative.productionCost
        }
    }

    private fun addOrderToFactoryQueue(order: Order) {
        //default turnsInQueue = 2
        factoryQueue.add(Delivery(order.origin, order.destination, order.amount))
    }

    private fun processOrdersInBacklog(role: ChainStructureNode<SupplyChainRole>) {
        for (backlog in role.representative.backlogs) {
            if (role.representative.stock >= backlog.value) {
                setOutgoingDelivery(role, backlog.key, backlog.value)
                role.representative.stock -= backlog.value
                role.representative.backlogs.replace(backlog.key, 0)
            } else {
                setOutgoingDelivery(role, backlog.key, role.representative.stock)
                role.representative.backlogs.replace(backlog.key, backlog.value - role.representative.stock)
                role.representative.stock = 0
                break
            }
        }
    }

    private fun processIncomingOrders(role: ChainStructureNode<SupplyChainRole>, order: Order) {
        if (role.representative.roleKey == order.destination) {
            if (chainStructure.playerRoles.containsKey(order.origin) && chainStructure.playerRoles[order.origin]!!.representative !is Factory) {
                role.representative.incomingOrder[order.origin] = order.amount
            } else {
                role.representative.incomingOrder[order.origin] = order.amount
            }
            if (order.destination != order.origin) {
                if (role.representative.stock >= order.amount) {
                    role.representative.stock -= order.amount
                    setOutgoingDelivery(role, order.origin, order.amount)
                } else {
                    val addToBacklog = order.amount - role.representative.stock

                    setOutgoingDelivery(role, order.origin, role.representative.stock)
                    if (role.representative.backlogs.containsKey(order.origin)) {
                        role.representative.backlogs[order.origin] = (role.representative.backlogs[order.origin]!! + addToBacklog)
                    } else {
                        role.representative.backlogs[order.origin] = addToBacklog
                    }
                    role.representative.stock = 0
                }
            }
        }
    }

    private fun updateBudgetBasedOnOutgoingDelivery(role: ChainStructureNode<SupplyChainRole>) {
        role.representative.budget += (role.representative.deliveryGain * role.representative.outgoingDelivery.values.sum())
    }

    private fun updateBudgetBasedOnStock(role: ChainStructureNode<SupplyChainRole>) {
        role.representative.budget -= role.representative.stockCost * role.representative.stock
    }

    private fun updateBudgetBasedOnBacklog(role: ChainStructureNode<SupplyChainRole>) {
        role.representative.budget -= role.representative.backlogs.values.sum() * role.representative.backlogCost
    }

    private fun setOutgoingDelivery(role: ChainStructureNode<SupplyChainRole>, destination: String, amount: Int) {
        if (role.representative.outgoingDelivery.containsKey(destination)) {
            role.representative.outgoingDelivery.replace(destination, role.representative.outgoingDelivery[destination]!! + amount)
        } else {
            role.representative.outgoingDelivery[destination] = amount
        }
    }
}