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
            if (tile is Coin) {
                continue
            }
            if (tile is Animal){
                for (enclosure in currentPlayer.playerEnclosure){
                    if (enclosure.animalTiles.isNotEmpty() && enclosure.animalTiles[0].species == tile.species){
                        rootService.playerActionService.placeTileFromTruck(enclosure)
                    }
                }
            }
            else{
                rootService.playerActionService.placeTileFromTruck(currentPlayer)
            }
        }
    }

    override fun toHintString(): String {
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "
    }

//    override fun toHintString(): String {
//        return "move tile to Enclosure with ${destination.pointValues.toString()} Points"
//    }
}