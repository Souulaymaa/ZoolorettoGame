package gameAI.moves

import entity.Enclosure
import gameAI.Move

class PlaceTileFromTruckToEnclosure(val destination : Enclosure) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "move tile to Enclosure with ${destination.pointValues.toString()} Points"
    }


}