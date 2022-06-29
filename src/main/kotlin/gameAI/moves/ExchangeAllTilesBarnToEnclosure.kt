package gameAI.moves

import entity.Enclosure
import entity.Player
import entity.Species
import gameAI.Move

class ExchangeAllTilesBarnToEnclosure(val source : Enclosure, val destination: Player, val species : Species) : Move {


    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return ""
    }
}