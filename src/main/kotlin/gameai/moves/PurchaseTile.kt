package gameai.moves

import entity.Player
import entity.Tile
import gameai.Move
import service.RootService
/**
 * class to implement functions for PurchaseTile Move
 */
class PurchaseTile(val player: Player, val tile: Tile, val destination: Int) : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        //TODO Purchase Tile must be an index
        rootService.playerActionService.purchaseTile(player,tile, destination)
    }

    /**
     * toHintString simply returns a string to be used as a hint during when PurchaseTile
     * action takes place
     */
    override fun toHintString(rootService: RootService): String {
        return "Buy $tile from barn of player ${player.playerName}."
    }
}