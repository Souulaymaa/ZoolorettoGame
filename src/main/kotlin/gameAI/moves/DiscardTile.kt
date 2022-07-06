package gameAI.moves

import entity.Tile
import gameAI.Move

class DiscardTile(private val target : Tile) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Discard Tile $target from Barn."
    }
}