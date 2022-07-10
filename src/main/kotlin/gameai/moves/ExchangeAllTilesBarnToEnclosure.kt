package gameai.moves

import entity.*
import gameai.Move
import service.RootService

/**
 * class to implement functions for ExchangeAllTilesBarnToEnclosure Move
 */
class ExchangeAllTilesBarnToEnclosure(val sourceIndex : Int, val species : Species) : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.exchangeAllTiles(sourceIndex, Animal(Type.NONE, species))
    }

    /**
     * toHintString simply returns a string to be used as a hint during when ExchangeAllTilesBarnToEnclosure action
     * takes place
     */
    override fun toHintString(rootService: RootService): String {
        val enclosure = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        return "Exchange all ${species.toString()} from Barn with all animals in" +
                " Enclosure with ${enclosure.pointValues.toString()} Points"
    }
}