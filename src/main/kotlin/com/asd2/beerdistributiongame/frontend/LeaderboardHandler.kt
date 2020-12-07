package com.asd2.beerdistributiongame.frontend

import com.asd2.beerdistributiongame.context.Context
import com.asd2.beerdistributiongame.replay.UserRanking
import com.asd2.beerdistributiongame.state.LeaderboardState
import javafx.fxml.FXML
import javafx.scene.control.Alert
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory

class LeaderboardHandler : Handler<LeaderboardState>() {

    override fun getViewFile(): String = "views/Leaderboard.fxml"

    @FXML lateinit var personalResultsTable : TableView<UserRanking>
    @FXML lateinit var ranking: TableColumn<UserRanking, Any>
    @FXML lateinit var username: TableColumn<UserRanking, Any>
    @FXML lateinit var points: TableColumn<UserRanking, Any>

    @FXML
    fun initialize() {
        setProperties()
        setGameRankings()
    }

    private fun setProperties(){
        ranking.cellValueFactory = PropertyValueFactory<UserRanking, Any>("ranking")
        username.cellValueFactory = PropertyValueFactory<UserRanking, Any>("username")
        points.cellValueFactory = PropertyValueFactory<UserRanking, Any>("points")
    }

    private fun setGameRankings() {
        if (Context.statesOfGame.size > 0) personalResultsTable.items.addAll(state.getGameRankings())
    }

    fun onBackToMenu() = state.notifyToGoBackToMainMenu()

    fun onGoToGraph() = state.notifyGoToGraph()

    fun onGoToGameView() = state.notifyGoToReplayLinear()

    fun onPlayerResult(){
        val userRanking = personalResultsTable.selectionModel.selectedItem
        if(userRanking != null) state.notifyToPersonalResult(userRanking.uuid!!)
        else showAlert("Please select a player game", Alert.AlertType.ERROR, "Error", "Error")

    }

}