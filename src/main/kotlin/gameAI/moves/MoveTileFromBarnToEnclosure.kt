package gameAI.moves

import entity.Enclosure
import entity.Player
import entity.Tile
import entity.ZoolorettoGameState
import gameAI.Move

class MoveTileFromBarnToEnclosure(val source: Player, val destination: Enclosure, val tile: Tile, currentGameState: ZoolorettoGameState) : Move {
    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "move tile ${tile.toString()} from barn to Enclosure with ${destination.pointValues.toString()} Points"

    }

    override fun calculateScore() : Int{
        throw NotImplementedError()
    }

}