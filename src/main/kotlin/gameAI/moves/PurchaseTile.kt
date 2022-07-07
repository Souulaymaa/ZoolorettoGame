package gameAI.moves

import entity.Enclosure
import entity.Player
import entity.Tile
import gameAI.Move
import service.RootService

class PurchaseTile(val player: Player, val tile: Tile, val destination: Enclosure) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.purchaseTile(player,tile, destination)
    }

    override fun toHintString(): String {
        return "Buy $tile from barn of player ${player.playerName}."
    }
}