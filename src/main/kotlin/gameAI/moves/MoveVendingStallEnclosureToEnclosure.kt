package gameAI.moves

import entity.Enclosure
import entity.ZoolorettoGameState
import gameAI.Move

class MoveVendingStallEnclosureToEnclosure(val source: Enclosure, val destination: Enclosure, currentGameState: ZoolorettoGameState) : Move {

    override fun performMove() {
        super.performMove()
    }
    override fun toHintString(): String {
        return "move vending stall from Enclosure with ${source.pointValues.toString()} Points to Enclosure with " +
                "${source.pointValues.toString()} Points"
    }

    override fun calculateScore() : Int{
        throw NotImplementedError()
    }
}