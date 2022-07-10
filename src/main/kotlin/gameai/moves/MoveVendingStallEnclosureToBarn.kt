package gameai.moves

import entity.VendingStall
import gameai.Move
import service.RootService
/**
 * class to implement functions for MoveVendingStallEnclosureToBarn Move
 */
class MoveVendingStallEnclosureToBarn(val sourceIndex: Int, val vendingStall: VendingStall) : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveTileFromEnclosureToBarn(sourceIndex, vendingStall)
    }

    /**
     * toHintString simply returns a string to be used as a hint during when MoveVendingStallEnclosureToBarn
     * action takes place
     */
    override fun toHintString(rootService: RootService): String {
        val source = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        return "move VendingStall from Enclosure with ${source.pointValues.toString()} Points to barn"
    }
}