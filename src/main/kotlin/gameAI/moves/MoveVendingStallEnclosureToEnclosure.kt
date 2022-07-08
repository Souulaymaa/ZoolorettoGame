package gameAI.moves

import gameAI.Move
import service.RootService

class MoveVendingStallEnclosureToEnclosure(val sourceIndex: Int, val destinationIndex: Int) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveVendingStall(sourceIndex, destinationIndex)
    }
    override fun toHintString(rootService: RootService): String {
        val source = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        val destination = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[destinationIndex]

        return "move vending stall from Enclosure with ${source.pointValues.toString()} Points to Enclosure with " +
                "${destination.pointValues.toString()} Points"
    }
}