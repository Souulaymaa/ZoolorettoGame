package gameAI.moves

import entity.Tile
import gameAI.Move
import service.RootService

class DiscardTile(private val target : Tile) : Move {

    override fun performMove(rootService: RootService) {
    }

    override fun toHintString(): String {
        return "Discard Tile $target from Barn."
    }
}