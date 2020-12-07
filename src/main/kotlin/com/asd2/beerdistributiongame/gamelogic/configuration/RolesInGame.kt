package com.asd2.beerdistributiongame.gamelogic.configuration

enum class Roles {
   RETAILER, WHOLESALE, WAREHOUSE, FACTORY, NONE
}

data class RoleOwner (val role: Roles, var playerId: String?)

data class RolesInGame(
        val playerRoles: HashMap<String, RoleOwner> = HashMap()
)
inline fun <reified T : Enum<T>> getRoles(): MutableList<String> {
   val stringList = mutableListOf<String>()
   enumValues<Roles>().forEach {
      if (it != Roles.NONE) stringList.add(it.name)
   }
   return (stringList)
}
