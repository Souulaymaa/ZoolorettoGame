package gameAI.moves

import entity.Player
import entity.Tile
import entity.ZoolorettoGameState
import gameAI.Move

class PurchaseTile(val player: Player, val tile: Tile, currentGameState: ZoolorettoGameState) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Buy $tile from barn of player ${player.playerName}."
    }

    override fun calculateScore() : Int{
        throw NotImplementedError()
    }

}