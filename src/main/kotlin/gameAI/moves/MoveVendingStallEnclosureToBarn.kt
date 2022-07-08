package gameAI.moves

import entity.VendingStall
import gameAI.Move
import service.RootService

class MoveVendingStallEnclosureToBarn(val sourceIndex: Int, val vendingStall: VendingStall) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveTileFromEnclosureToBarn(sourceIndex, vendingStall)
    }

    override fun toHintString(rootService: RootService): String {
        val source = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        return "move VendingStall from Enclosure with ${source.pointValues.toString()} Points to barn"
    }
}