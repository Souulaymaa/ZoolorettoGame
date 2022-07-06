package gameAI.moves

import entity.Enclosure
import gameAI.Move

class ExchangeAllTilesEnclosureToEnclosure(val source : Enclosure, val destination: Enclosure) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Exchange ${source.animalTiles[0].toString()} from Enclosure  ${source.pointValues.toString()} " +
                "Points with ${destination.animalTiles[0].toString()}"
    }
}