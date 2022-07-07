package gameAI.moves

import entity.Enclosure
import entity.Player
import entity.Species
import gameAI.Move
import service.RootService

class ExchangeAllTilesBarnToEnclosure(val source : Enclosure, val destination: Player, val species : Species) : Move {


    override fun performMove(rootService: RootService) {
    }

    override fun toHintString(): String {
        return "Exchange all ${species.toString()} from Barn with all animals in" +
                " Enclosure with ${source.pointValues.toString()} Points"
    }
}