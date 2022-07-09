package gameAI.moves

import gameAI.Move
import service.RootService

class AddTileToTruck(val truckIndex: Int) : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.addTile(truckIndex)
    }

    override fun toHintString(rootService: RootService): String {
        val trucks = rootService.zoolorettoGame!!.currentGameState.deliveryTrucks
        val truck = trucks[truckIndex]
        if (truck.tilesOnTruck.isEmpty()){
            return "Add drawn Tile to empty Truck"
        }
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "

    }
}