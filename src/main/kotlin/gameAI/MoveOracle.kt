package gameAI

import entity.ZoolorettoGameState
import service.deepZoolorettoCopy

class MoveOracle(currentGameState: ZoolorettoGameState) {
    val currentGameStateCopy : ZoolorettoGameState

    init {
        this.currentGameStateCopy = deepZoolorettoCopy(currentGameState)
    }

    fun determineAllCurrentAllowedMoves() : List<Move> {
        throw NotImplementedError()
    }

    fun determineAllMoneyMoves() : List<Move> {
        throw NotImplementedError()
    }

    fun determineAllTruckRelatedMoves() : List<Move>{
        throw  NotImplementedError()
    }
}

