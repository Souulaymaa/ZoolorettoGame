package service

import entity.ZoolorettoGame

class RootService {
    val zoolorettoGame: ZoolorettoGame? = null
    val zoolorettoGameService = ZoolorettoGameService(this)
    val playerActionService = PlayerActionService(this)
    val scoreService = ScoreService(this)
    val aIService = AIService(this)
    val gameStateService = GameStateService(this)
}