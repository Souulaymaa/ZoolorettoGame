package gameAI

import entity.ZoolorettoGameState

class MoveOracle(currentGameState: ZoolorettoGameState) {
    val currentGameStateCopy : ZoolorettoGameState

    init {
        this.currentGameStateCopy = deepZoolorettoCopy(currentGameState)
    }

    fun determineAllCurrentAllowedMoves() : List<Move> {
        throw NotImplementedError()
    }
}

fun deepZoolorettoCopy(toCopy : ZoolorettoGameState) : ZoolorettoGameState{
    throw NotImplementedError()
}
