package gameAI.moves

import entity.Tile
import gameAI.Move
import service.RootService

class DiscardTile(private val target : Tile) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.discardTile(target)
    }

    override fun toHintString(rootService: RootService): String {
        return "Discard Tile $target from Barn."
    }
}