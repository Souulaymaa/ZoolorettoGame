package gameAI.moves

import entity.Player
import entity.ZoolorettoGameState
import gameAI.Move

class PlaceTileFromTruckToBarn(destination: Player, currentGameState: ZoolorettoGameState) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Place tile in Barn."
    }

    override fun calculateScore() : Int{
        throw NotImplementedError()
    }

}