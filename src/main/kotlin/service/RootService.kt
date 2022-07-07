package service

import entity.ZoolorettoGame
import entity.*
import view.Refreshable

/**
 * Managing the services and the current game.
 */
class RootService {
    /**
     * The services of Zooloretto
     */
    var zoolorettoGame: ZoolorettoGame? = null
    val zoolorettoGameService = ZoolorettoGameService(this)
    val playerActionService = PlayerActionService(this)
    val aiService = AIService(this)
    val gameStateService = GameStateService(this)
    val scoreService = ScoreService()

    var highscore = mutableListOf<Pair<String, Double>>()
    var currentGame: ZoolorettoGameState? = null



    /**
     * Function to add refreshables to the services
     * @param newRefreshable the Interface
     */
    fun addRefreshable(newRefreshable: Refreshable) {
        zoolorettoGameService.addRefreshable(newRefreshable)
        playerActionService.addRefreshable(newRefreshable)
        gameStateService.addRefreshable(newRefreshable)
        scoreService.addRefreshable(newRefreshable)
    }

    /**
     * Adds each of the provided [newRefreshables] to all services
     * connected to this root service
     */
    fun addRefreshables (vararg newRefreshables: Refreshable) {
        newRefreshables.forEach { addRefreshable(it) }
    }
}

