package gameAI.moves

import entity.Tile
import gameAI.Move
import service.RootService

class MoveTileFromBarnToEnclosure(val sourceIndex: Int, val tile: Tile) : Move {
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveOneTile(sourceIndex, tile)
    }

    override fun toHintString(rootService: RootService): String {
        val source = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        return "move tile ${tile.toString()} from barn to Enclosure with ${source.pointValues.toString()} Points"
    }
}