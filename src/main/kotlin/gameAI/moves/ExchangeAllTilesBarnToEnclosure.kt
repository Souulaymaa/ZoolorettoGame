package gameAI.moves

import entity.*
import gameAI.Move
import service.RootService

class ExchangeAllTilesBarnToEnclosure(val sourceIndex : Int, val species : Species) : Move {


    override fun performMove(rootService: RootService) {
        rootService.playerActionService.exchangeAllTiles(sourceIndex, Animal(Type.NONE, species))
    }

    override fun toHintString(rootService: RootService): String {
        val enclosure = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        return "Exchange all ${species.toString()} from Barn with all animals in" +
                " Enclosure with ${enclosure.pointValues.toString()} Points"
    }
}