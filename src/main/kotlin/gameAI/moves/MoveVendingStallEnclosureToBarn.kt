package gameAI.moves

import entity.Enclosure
import entity.Player
import gameAI.Move
import service.RootService

class MoveVendingStallEnclosureToBarn(val source: Enclosure, val destination: Player) : Move {

    override fun performMove(rootService: RootService) {
    }

    override fun toHintString(): String {
        return "move VendingStall from Enclosure with ${source.pointValues.toString()} Points to barn"
    }
}