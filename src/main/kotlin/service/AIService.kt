package service

import entity.Difficulty
import gameai.MoveOracle
import gameai.searchtree.Tree
import kotlin.random.Random

class AIService(val rootService: RootService) {
    private val searchTreeLevel = 4
    fun performNextMove(){
        val currentPlayer = rootService.zoolorettoGame!!.currentGameState.players.peek()

        when(currentPlayer.botSkillLevel){
            Difficulty.EASY, Difficulty.MEDIUM -> performEasyMove()
            Difficulty.HARD -> performHardMove()
            else -> {}
        }
    }

    private fun performHardMove() {
        val currentGameState = rootService.zoolorettoGame!!.currentGameState
        val move = Tree(currentGameState, searchTreeLevel).getBestMove()

        move.performMove(rootService)
    }

    private fun performEasyMove() {
        val currentGameState = rootService.zoolorettoGame!!.currentGameState
        val move = MoveOracle(currentGameState).determineAllCurrentAllowedMoves().random(Random(42))

        move.performMove(rootService)
    }


    fun getHint() : String{
        val currentGameState = rootService.zoolorettoGame!!.currentGameState
        val move = Tree(currentGameState, searchTreeLevel).getBestMove()

        return move.toHintString(rootService)
    }

}