package gameAI.moves

import gameAI.Move
import service.RootService

class ExchangeAllTilesEnclosureToEnclosure(val sourceIndex : Int, val destinationIndex: Int) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.exchangeAllTiles(sourceIndex, destinationIndex)
    }

    override fun toHintString(rootService: RootService): String {
        val source = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]
        val destination = rootService.zoolorettoGame!!.currentGameState.players.peek().playerEnclosure[sourceIndex]

        return "Exchange ${source.animalTiles[0].toString()} from Enclosure  ${source.pointValues.toString()} " +
                "Points with ${destination.animalTiles[0].toString()}"
    }
}