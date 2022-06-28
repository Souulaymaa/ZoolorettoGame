package gameAI.moves

import entity.Tile
import gameAI.Move

class DiscardTile(val target : Tile) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Discard Tile ${target.toString()} from Barn."
    }
}