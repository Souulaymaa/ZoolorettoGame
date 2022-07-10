package gameai.moves

import entity.Tile
import gameai.Move
import service.RootService
/**
 * class to implement functions for DiscardTile Move
 */
class DiscardTile(private val target : Tile) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.discardTile(target)
    }

    /**
     * toHintString simply returns a string to be used as a hint during when DiscardTile action takes place
     */
    override fun toHintString(rootService: RootService): String {
        return "Discard Tile $target from Barn."
    }
}