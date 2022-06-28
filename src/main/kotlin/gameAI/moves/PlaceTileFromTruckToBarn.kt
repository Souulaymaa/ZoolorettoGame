package gameAI.moves

import entity.Player
import gameAI.Move

class PlaceTileFromTruckToBarn(destination: Player) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Place tile in Barn."
    }

}