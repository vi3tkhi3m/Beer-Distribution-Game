package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.context.Player
import com.asd2.beerdistributiongame.gamelogic.configuration.GameConfig
import com.asd2.beerdistributiongame.gamelogic.configuration.Roles
import com.asd2.beerdistributiongame.network.ChooseRoleEvent
import com.asd2.beerdistributiongame.settings.Settings
import com.asd2.beerdistributiongame.state.LobbyState
import javafx.fxml.FXML
import javafx.scene.control.Button
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text

data class PlayerRow(val username: String, var role: String, val uuid: String)

class LobbyHandler : Handler<LobbyState>() {

    @FXML lateinit var hostNameLobbyText: Text
    @FXML lateinit var hostIpLobbyText: Text

    @FXML lateinit var playersTable: TableView<PlayerRow>
    @FXML lateinit var usernameColumn: TableColumn<Any, Any>
    @FXML lateinit var roleColumn: TableColumn<Any, Any>

    @FXML lateinit var mainMenuButton: Button
    @FXML lateinit var startGameButton: Button
    @FXML lateinit var factoryButton: Button
    @FXML lateinit var warehouseButton: Button
    @FXML lateinit var wholesaleButton: Button
    @FXML lateinit var retailerButton: Button

    @FXML lateinit var amountOfRetailers: Text
    @FXML lateinit var amountOfWholesalers: Text
    @FXML lateinit var amountOfFactories: Text
    @FXML lateinit var amountOfWarehouses: Text

    @FXML lateinit var amountOfRetailersChosen: Text
    @FXML lateinit var amountOfWholesalersChosen: Text
    @FXML lateinit var amountOfFactoriesChosen: Text
    @FXML lateinit var amountOfWarehousesChosen: Text

    @FXML lateinit var closeLobbyAlert: AnchorPane
    @FXML lateinit var alertMainMenuButton: Button

    private var lastSelectedRoleButton: Button? = null

    override fun getViewFile() = "views/Lobby.fxml"

    @FXML
    fun initialize() {
        fillLobbyUsingGameConfig(Context.gameConfig)

        usernameColumn.cellValueFactory = PropertyValueFactory("username")
        roleColumn.cellValueFactory = PropertyValueFactory("role")

        startGameButton.isVisible = Context.isGameManager
        factoryButton.isDisable = Context.isGameManager
        warehouseButton.isDisable = Context.isGameManager
        wholesaleButton.isDisable = Context.isGameManager
        retailerButton.isDisable = Context.isGameManager

        state.handler = this

        hostNameLobbyText.text = if (Context.isHost) Settings.username else Context.gameConfig.hostName
        hostIpLobbyText.text = if (Context.isHost) state.getCorrectIpAddress() else Settings.lastSuccessfulIp
        Context.players.forEach { addPlayer(it) }
        updateButtonDisables()
    }

    fun onBackToMainMenuPressed() = state.notifyToGoBackToMainMenu()

    fun onStartGamePressed() = state.hostStartGame()

    fun onAlertMainMenuPressed() = state.notifyToGoBackToMainMenu()

    fun chooseFactoryPressed() {
        state.notifyToChooseRole(Roles.FACTORY)
        disableOneButtons(factoryButton)
    }

    fun chooseWarehousePressed() {
        state.notifyToChooseRole(Roles.WAREHOUSE)
        disableOneButtons(warehouseButton)
    }

    fun chooseWholesalePressed() {
        state.notifyToChooseRole(Roles.WHOLESALE)
        disableOneButtons(wholesaleButton)
    }

    fun chooseRetailerPressed() {
        state.notifyToChooseRole(Roles.RETAILER)
        disableOneButtons(retailerButton)
    }

    /**
     * If the player choose a role, it will update the table and the amount of roles available will be one less.
     *
     * @param newPlayer Player
     */
    @Synchronized
    fun addPlayer(newPlayer: Player) {
        playersTable.items.removeIf { it.uuid == newPlayer.uuid }
        playersTable.items.add(PlayerRow(newPlayer.username, newPlayer.role.toString(), newPlayer.uuid))
        incrementRoleQuantity(newPlayer.role)
        if (Context.isHost) updateStartGameButton()
    }

    /**
     * If the player leaves the chosen role will be set to available.
     *
     * @param disconnectPlayer Player
     */
    @Synchronized
    fun removePlayer(disconnectPlayer: Player) {
        playersTable.items.removeIf { it.uuid == disconnectPlayer.uuid }
        decreaseRoleQuantity(disconnectPlayer.role)
        if (Context.isHost) updateStartGameButton()
    }

    /**
     * It updates the player in the JavaFX UI (role, quantity of roles , amount Of available roles)
     * Disables the button of the players chosen role.
     * @param uuid The players Unique id
     * @param oldRole The players old role
     * @param newRole The players new role
     */
    @Synchronized
    fun updatePlayer(uuid: String, oldRole: Roles, newRole: Roles) {
        playersTable.items.single { it.uuid == uuid }.role = newRole.toString()
        playersTable.refresh()
        decreaseRoleQuantity(oldRole)
        incrementRoleQuantity(newRole)
        updateButtonDisables()
        if (Context.isHost) updateStartGameButton()
    }

    /**
     * Checks if the "start game button" should be enabled.
     */
    @Synchronized
    fun updateStartGameButton(){
        startGameButton.isDisable = !playersTable.items.none { it.role == "NONE" }
    }

    /**
     * Update buttons for the player only
     */
    @Synchronized
    private fun updateButtonDisables() {
        if (Context.isGameManager) return

        factoryButton.isDisable = amountOfFactoriesChosen.text.toInt() >= amountOfFactories.text.toInt()
        warehouseButton.isDisable = amountOfWarehousesChosen.text.toInt() >= amountOfWarehouses.text.toInt()
        wholesaleButton.isDisable = amountOfWholesalersChosen.text.toInt() >= amountOfWholesalers.text.toInt()
        retailerButton.isDisable = amountOfRetailersChosen.text.toInt() >= amountOfRetailers.text.toInt()

        lastSelectedRoleButton?.isDisable = true
    }

    /**
     * Called when the player himself chooses a new role
     *
     * @param roleEvent ChooseRoleEvent Networkevent
     */
    fun onChooseRole(roleEvent: ChooseRoleEvent) {
        when (roleEvent.newRole) {
            Roles.FACTORY -> lastSelectedRoleButton = factoryButton
            Roles.WAREHOUSE -> lastSelectedRoleButton = warehouseButton
            Roles.WHOLESALE -> lastSelectedRoleButton = wholesaleButton
            Roles.RETAILER -> lastSelectedRoleButton = retailerButton
            else -> return
        }
        updatePlayer(roleEvent.player.uuid, roleEvent.oldRole, roleEvent.newRole)
    }

    /**
     * When the game ends all the buttons and tables will be set to invisible or disable.
     */
    fun onEndGame() {
        Context.gameConfig = GameConfig()
        disableAllButtons()
        playersTable.isDisable = true
        closeLobbyAlert.isVisible = true
    }

    /**
     * Fills the lobby with the gameconfig(amount of roles)
     *
     * @param gameConfig Gameconfiguration from the the gameconfiguration setup
     */
    private fun fillLobbyUsingGameConfig(gameConfig: GameConfig) {
        amountOfRetailers.text = gameConfig.quantityOfRetailers.toString()
        amountOfFactories.text = gameConfig.quantityOfFactories.toString()
        amountOfWholesalers.text = gameConfig.quantityOfWholesales.toString()
        amountOfWarehouses.text = gameConfig.quantityOfWarehouses.toString()
    }

    /**
     * Increase the amount of roles
     *
     * @param amountChosen JavaFx Textobject of the amount of available roles
     */
    private fun incrementQuantityOfRoles(amountChosen: Text) {
        amountChosen.text = (amountChosen.text.toInt() + 1).toString()
    }

    /**
     * Decrease the amount of roles
     *
     * @param amountChosen JavaFx Textobject of the amount of available roles
     */
    private fun decreaseQuantityOfRoles(amountChosen: Text) {
        amountChosen.text = (amountChosen.text.toInt() - 1).toString()
    }

    /**
     * Increase the amount of quantity of available roles
     *
     * @param role Roles
     */
    private fun incrementRoleQuantity(role: Roles) {
        when (role) {
            Roles.RETAILER -> incrementQuantityOfRoles(amountOfRetailersChosen)
            Roles.WHOLESALE -> incrementQuantityOfRoles(amountOfWholesalersChosen)
            Roles.WAREHOUSE -> incrementQuantityOfRoles(amountOfWarehousesChosen)
            Roles.FACTORY -> incrementQuantityOfRoles(amountOfFactoriesChosen)
            else -> return
        }
    }

    /**
     * Decrease the amount of quantity of available roles
     *
     * @param role Roles
     */
    private fun decreaseRoleQuantity(role: Roles) {
        when (role) {
            Roles.RETAILER -> decreaseQuantityOfRoles(amountOfRetailersChosen)
            Roles.WHOLESALE -> decreaseQuantityOfRoles(amountOfWholesalersChosen)
            Roles.WAREHOUSE -> decreaseQuantityOfRoles(amountOfWarehousesChosen)
            Roles.FACTORY -> decreaseQuantityOfRoles(amountOfFactoriesChosen)
        }
    }

    /**
     * Disable a button
     *
     * @param button JavaFX Button
     */
    private fun disableOneButtons(button: Button) {
        button.isDisable = true
    }

    /**
     * Disable all button
     */
    private fun disableAllButtons() {
        mainMenuButton.isDisable = true
        factoryButton.isDisable = true
        retailerButton.isDisable = true
        startGameButton.isDisable = true
        warehouseButton.isDisable = true
        wholesaleButton.isDisable = true
    }

    /**
     * When the connection is lost it sends the user back to the main menu
     */
    fun onLostConnection() = state.notifyToGoBackToMainMenu()
}