package gameAI.moves

import entity.*
import gameAI.Move
import service.GameStateService

class TakeTruck(private val truck: DeliveryTruck, private val currentGameState: ZoolorettoGameState) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "
    }

    override fun calculateScore() : Int{
        val currentPlayer = currentGameState.players.peek()
        var chosenTruck: DeliveryTruck = DeliveryTruck()
        var maxScore = 0
        var maxTruckScore = 0
        for(tile in truck.tilesOnTruck){
            for(enclosure in currentPlayer.playerEnclosure){
                if (tile is Animal && enclosure.animalTiles.isNotEmpty()){
                    if (tile.species == enclosure.animalTiles[0].species) {
                        maxTruckScore++
                    }
                }else if(tile is VendingStall && (enclosure.vendingStalls.size < enclosure.maxVendingStalls)){
                    maxTruckScore++
                }else if (tile is Coin){
                    maxTruckScore++
                }
            }
        }
        if(maxScore <= maxTruckScore){
            maxScore = maxTruckScore
            chosenTruck = truck
        }
        return 0
    }

}