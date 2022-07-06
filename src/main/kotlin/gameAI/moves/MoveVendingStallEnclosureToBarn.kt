package gameAI.moves

import entity.Enclosure
import entity.Player
import gameAI.Move

class MoveVendingStallEnclosureToBarn(val source: Enclosure, val destination: Player) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "move VendingStall from Enclosure with ${source.pointValues.toString()} Points to barn"
    }

}