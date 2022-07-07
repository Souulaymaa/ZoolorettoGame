package gameAI.moves

import entity.*
import gameAI.Move
import service.RootService

class TakeTruckAndPlaceTiles(private val truck: DeliveryTruck) : Move {

    override fun performMove(rootService: RootService) {
    }

    override fun toHintString(): String {
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "
    }

//    override fun toHintString(): String {
//        return "move tile to Enclosure with ${destination.pointValues.toString()} Points"
//    }
}