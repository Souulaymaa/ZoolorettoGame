package gameAI.moves

import entity.Player
import entity.Tile
import gameAI.Move
import service.RootService

class PurchaseTile(val player: Player, val tile: Tile, val destination: Int) : Move {

    override fun performMove(rootService: RootService) {
        //TODO Purchase Tile must be an index
        rootService.playerActionService.purchaseTile(player,tile, destination)
    }

    override fun toHintString(rootService: RootService): String {
        return "Buy $tile from barn of player ${player.playerName}."
    }
}