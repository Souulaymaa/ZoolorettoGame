package gameai.moves

import entity.Tile
import gameai.Move
import service.RootService
/**
 * class to implement functions for MoveTileFromBarnToEnclosure Move
 */
class MoveTileFromBarnToEnclosure(val sourceIndex: Int, val tile: Tile) : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveOneTile(sourceIndex, tile)
    }

    /**
     * toHintString simply returns a string to be used as a hint during when MoveTileFromBarnToEnclosure
     * action takes place
     */
    override fun toHintString(rootService: RootService): String {
        val source = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        return "move tile ${tile.toString()} from barn to Enclosure with ${source.pointValues.toString()} Points"
    }
}