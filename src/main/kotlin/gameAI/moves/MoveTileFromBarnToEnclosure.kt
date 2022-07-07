package gameAI.moves

import entity.Enclosure
import entity.Player
import entity.Tile
import gameAI.Move
import service.RootService

class MoveTileFromBarnToEnclosure(val source: Player, val destination: Enclosure, val tile: Tile) : Move {
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.moveOneTile(source,destination,tile)
    }

    override fun toHintString(): String {
        return "move tile ${tile.toString()} from barn to Enclosure with ${destination.pointValues.toString()} Points"

    }
}