package gameai.moves

import entity.*
import gameai.Move
import service.RootService
/**
 * class to implement functions for TakeTruckAndPlaceTiles Move
 */
class TakeTruckAndPlaceTiles(private val truckIndex: Int) : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        val currentGame = rootService.zoolorettoGame
        checkNotNull(currentGame)
        val currentPlayer = currentGame.currentGameState.players.peek()

        rootService.playerActionService.takeTruck(truckIndex)

        val tilesOnTruck =currentPlayer.chosenTruck!!.tilesOnTruck

        for(i in 0 until tilesOnTruck.size){
            putInGoodDestination(rootService, tilesOnTruck[0])
        }

    }

    /**
     * helping method to split between animal destinations and vending stall destinations
     */
    private fun putInGoodDestination(rootService: RootService, tile: Tile) {
        if(tile is Animal){
            putInGoodDestinationAnimal(rootService, tile)
        }
        else if (tile is VendingStall){
            putInGoodDestinationVendingStall(rootService)
        }
    }

    /**
     * helping method to place animal tile in the best animal space destination
     */
    private fun putInGoodDestinationAnimal(rootService: RootService, tile: Animal ){
        val currentPlayer = rootService.zoolorettoGame!!.currentGameState.players.peek()
        val enclosures = currentPlayer.playerEnclosure

        val indexedEnclosures = enclosures.mapIndexed { index, enclosure ->
            Pair<Int, Enclosure>(index, enclosure)
        }

        val emptyEnclosures = indexedEnclosures.filter { it.second.animalTiles.isEmpty() }
        val notFullAndNotEmptyEnclosures = indexedEnclosures.filter {
            val notFull = it.second.animalTiles.size < it.second.maxAnimalSlots
            val notEmpty = it.second.animalTiles.isNotEmpty()
            return@filter notFull && notEmpty
        }
        val notFullAndNotEmptyEnclosuresWithSameSpecies = notFullAndNotEmptyEnclosures.filter { it.second.animalTiles[0].species == tile.species }

        if (notFullAndNotEmptyEnclosuresWithSameSpecies.isNotEmpty()){
            //We have a compatible (those with animals and same type) enclosure that's not full
            val targetIndex = notFullAndNotEmptyEnclosuresWithSameSpecies.first().first //returns pair<index,enc> and then index
            rootService.playerActionService.placeTileFromTruck(targetIndex)
        }
        else if(emptyEnclosures.isNotEmpty()){
            //We have not compatible enclosures but at least one empty
            val targetIndex = emptyEnclosures.first().first //returns pair<index,enc> and then index
            rootService.playerActionService.placeTileFromTruck(targetIndex)
        }
        else{
            //We have no place for the tile at all... Therefore, it goes in the barn
            rootService.playerActionService.placeTileFromTruck()
        }
    }

    /**
     * helping method to place vending Stall tile in the best vending Stall space destination
     */
    private fun putInGoodDestinationVendingStall(rootService: RootService){
        val currentPlayer = rootService.zoolorettoGame!!.currentGameState.players.peek()
        val enclosures = currentPlayer.playerEnclosure

        val indexedEnclosures = enclosures.mapIndexed { index, enclosure ->
            Pair<Int, Enclosure>(index, enclosure)
        }

        val notFullEnclosures = indexedEnclosures.filter { it.second.vendingStalls.size < it.second.maxVendingStalls }
        val maximumAnimalEnclosure = notFullEnclosures.maxByOrNull { it.second.animalTiles.size }

        if (maximumAnimalEnclosure != null){
            //We've got at least an enclosure to put at
            val targetIndex = maximumAnimalEnclosure.first
            rootService.playerActionService.placeTileFromTruck(targetIndex)
        }
        else{
            //All enclosures must be full here
            rootService.playerActionService.placeTileFromTruck()
        }
    }

    /**
     * toHintString simply returns a string to be used as a hint during when TakeTruckAndPlaceTiles
     * action takes place
     */
    override fun toHintString(rootService: RootService): String {
        //return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "
        return "TODO" //TODO
    }
}