package service

import entity.Difficulty
import gameAI.MoveOracle
import gameAI.searchtree.Tree
import kotlin.random.Random

class AIService(val rootService: RootService) {
    val SEARCH_TREE_LEVEL = 4
    fun performNextMove(){
        val currentPlayer = rootService.zoolorettoGame!!.currentGameState.players.peek()

        when(currentPlayer.botSkillLevel){
            Difficulty.EASY, Difficulty.MEDIUM -> performEasyMove()
            Difficulty.HARD -> performHardMove()
        }
    }

    private fun performHardMove() {
        val currentGameState = rootService.zoolorettoGame!!.currentGameState
        val move = Tree(currentGameState, SEARCH_TREE_LEVEL).getBestMove()

        move.performMove(rootService)
    }

    private fun performEasyMove() {
        val currentGameState = rootService.zoolorettoGame!!.currentGameState
        val move = MoveOracle(currentGameState).determineAllCurrentAllowedMoves().random(Random(42))

        move.performMove(rootService)
    }


}