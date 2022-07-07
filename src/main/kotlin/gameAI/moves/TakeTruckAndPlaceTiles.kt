package gameAI.moves

import entity.*
import gameAI.Move
import service.RootService

class TakeTruckAndPlaceTiles(private val truck: DeliveryTruck) : Move {

    override fun performMove(rootService: RootService) {
        val currentGame = rootService.currentGame
        checkNotNull(currentGame)
        val currentPlayer = currentGame.players.peek()

        rootService.playerActionService.takeTruck(truck)

        for (tile in currentPlayer.chosenTruck!!.tilesOnTruck){
            TODO("COUNT POSSIBLE DESTINATIONS FOR THE TILES")
        }
    }

    override fun toHintString(): String {
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "
    }

//    override fun toHintString(): String {
//        return "move tile to Enclosure with ${destination.pointValues.toString()} Points"
//    }
}