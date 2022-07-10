package gameai.moves

import gameai.Move
import service.RootService
/**
 * class to implement functions for MoveVendingStallEnclosureToEnclosure Move
 */
class MoveVendingStallEnclosureToEnclosure(val sourceIndex: Int, val destinationIndex: Int) : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveVendingStall(sourceIndex, destinationIndex)
    }

    /**
     * toHintString simply returns a string to be used as a hint during when MoveVendingStallEnclosureToEnclosure
     * action takes place
     */
    override fun toHintString(rootService: RootService): String {
        val source = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        val destination = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[destinationIndex]

        return "move vending stall from Enclosure with ${source.pointValues.toString()} Points to Enclosure with " +
                "${destination.pointValues.toString()} Points"
    }
}