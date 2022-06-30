package gameAI.moves

import entity.Player
import entity.Tile
import gameAI.Move

class PurchaseTile(val player: Player, val tile: Tile) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Buy $tile from barn of player ${player.playerName}."
    }
}