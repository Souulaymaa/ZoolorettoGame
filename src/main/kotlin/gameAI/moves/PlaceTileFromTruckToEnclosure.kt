package gameAI.moves

import entity.Enclosure
import entity.ZoolorettoGameState
import gameAI.Move

class PlaceTileFromTruckToEnclosure(private val destination : Enclosure, currentGameState: ZoolorettoGameState) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "move tile to Enclosure with ${destination.pointValues.toString()} Points"
    }

    override fun calculateScore() : Int{
        throw NotImplementedError()
    }


}