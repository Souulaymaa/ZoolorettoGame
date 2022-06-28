package gameAI.moves

import entity.Enclosure
import entity.Player
import gameAI.Move

class MoveVendingStallEnclosureToEnclosure(val source: Enclosure, val destination: Enclosure) : Move {

    override fun performMove() {
        super.performMove()
    }
    override fun toHintString(): String {
        return "Move Vending Stall"
    }
}