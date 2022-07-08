package gameAI.moves

import entity.*
import gameAI.Move
import service.RootService

class TakeTruckAndPlaceTiles(private val truck: DeliveryTruck) : Move {

    override fun performMove(rootService: RootService) {
        val currentGame = rootService.zoolorettoGame
        checkNotNull(currentGame)
        val currentPlayer = currentGame.currentGameState.players.peek()

        rootService.playerActionService.takeTruck(truck)

        for (tile in currentPlayer.chosenTruck!!.tilesOnTruck){
            if (tile is Coin) {
                continue
            }
            else if (tile is Animal){
                putAnimalInEnclosureOrBarn(rootService, currentPlayer, tile)
            }
            else if (tile is VendingStall){
                putVendingStallInEnclosureOrBarn(rootService, currentPlayer)
            }
        }
    }

    private fun putAnimalInEnclosureOrBarn(rootService: RootService, currentPlayer : Player, tile: Animal){
        val nonFullEnclosures = currentPlayer.playerEnclosure.filter { it.animalTiles.size < it.maxAnimalSlots }
        val emptyEnclosures = currentPlayer.playerEnclosure.filter { it.animalTiles.isEmpty() }

        //Let's see if we can find an enclosure that is not full and has the same species inside
        for (enclosure in nonFullEnclosures){
            if (enclosure.animalTiles.isNotEmpty() && enclosure.animalTiles[0].species == tile.species){
                rootService.playerActionService.placeTileFromTruck(enclosure)
                return
            }
        }

        //If there was no return it seems that either have empty enclosures
        if (emptyEnclosures.isNotEmpty()){
            rootService.playerActionService.placeTileFromTruck(emptyEnclosures[0])
            return
        }
        else{
            //All Enclosures are full, so we have to place it in the barn
            rootService.playerActionService.placeTileFromTruck(currentPlayer)
        }
    }
    private fun putVendingStallInEnclosureOrBarn(rootService: RootService, currentPlayer : Player){
        val nonFullEnclosures = currentPlayer.playerEnclosure.filter { it.vendingStalls.size < it.maxVendingStalls }

        //Place it in the first enclosure where we have some empty space
        for (enclosure in nonFullEnclosures){
            rootService.playerActionService.placeTileFromTruck(enclosure)
            return
        }

        //No enclosure found then place it in the barn
        rootService.playerActionService.placeTileFromTruck(currentPlayer)
    }

    override fun toHintString(): String {
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "
    }

//    override fun toHintString(): String {
//        return "move tile to Enclosure with ${destination.pointValues.toString()} Points"
//    }
}