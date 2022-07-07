package gameAI.moves

import entity.DeliveryTruck
import gameAI.Move
import service.RootService

class AddTileToTruck(val truck: DeliveryTruck) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.addTile(truck)
    }

    override fun toHintString(): String {
        if (truck.tilesOnTruck.isEmpty()){
            return "Add drawn Tile to empty Truck"
        }
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "

    }
}