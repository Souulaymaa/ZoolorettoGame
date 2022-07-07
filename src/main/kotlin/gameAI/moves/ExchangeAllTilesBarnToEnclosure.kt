package gameAI.moves

import entity.*
import gameAI.Move
import service.RootService

class ExchangeAllTilesBarnToEnclosure(val source : Enclosure, val destination: Player, val species : Species) : Move {


    override fun performMove(rootService: RootService) {
        rootService.playerActionService.exchangeAllTiles(source, destination, Animal(Type.NONE, species))
    }

    override fun toHintString(): String {
        return "Exchange all ${species.toString()} from Barn with all animals in" +
                " Enclosure with ${source.pointValues.toString()} Points"
    }
}