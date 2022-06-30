package gameAI.moves

import entity.Enclosure
import gameAI.Move

class PlaceTileFromTruckToEnclosure(private val destination : Enclosure) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Place tile in enclosure ${destination.enclosureNumber}."
    }


}