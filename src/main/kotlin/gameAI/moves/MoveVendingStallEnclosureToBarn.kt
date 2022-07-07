package gameAI.moves

import entity.Enclosure
import entity.Player
import entity.VendingStall
import gameAI.Move
import service.RootService

class MoveVendingStallEnclosureToBarn(val source: Enclosure, val destination: Player, val vendingStall: VendingStall) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveTileFromEnclosureToBarn(source, destination, vendingStall)
    }

    override fun toHintString(): String {
        return "move VendingStall from Enclosure with ${source.pointValues.toString()} Points to barn"
    }
}