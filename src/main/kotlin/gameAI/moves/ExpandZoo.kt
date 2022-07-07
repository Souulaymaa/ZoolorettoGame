package gameAI.moves

import entity.ZoolorettoGameState
import gameAI.Move

class ExpandZoo(currentGameState: ZoolorettoGameState) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Expand Zoo"
    }

    override fun calculateScore() : Int{
        throw NotImplementedError()
    }
}