package gameAI.moves

import entity.Enclosure
import gameAI.Move
import service.RootService

class MoveVendingStallEnclosureToEnclosure(val source: Enclosure, val destination: Enclosure) : Move {

    override fun performMove(rootService: RootService) {
    }
    override fun toHintString(): String {
        return "move vending stall from Enclosure with ${source.pointValues.toString()} Points to Enclosure with " +
                "${source.pointValues.toString()} Points"
    }
}