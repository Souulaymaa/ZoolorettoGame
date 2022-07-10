package gameai.moves

import gameai.Move
import service.RootService

/**
 * class to implement functions for AddTileToTruck Move
 */
class AddTileToTruck(val truckIndex: Int) : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.addTile(truckIndex)
    }

    /**
     * toHintString simply returns a string to be used as a hint during when AddTileToTruck action takes place
     */
    override fun toHintString(rootService: RootService): String {
        val trucks = rootService.zoolorettoGame!!.currentGameState.deliveryTrucks
        val truck = trucks[truckIndex]
        if (truck.tilesOnTruck.isEmpty()){
            return "Add drawn Tile to empty Truck"
        }
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "

    }
}