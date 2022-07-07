package gameAI.moves

import entity.DeliveryTruck
import gameAI.Move

class AddTileToTruck(val truck: DeliveryTruck) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        if (truck.tilesOnTruck.isEmpty()){
            return "Add drawn Tile to empty Truck"
        }
        return "Take truck containing ${truck.tilesOnTruck.fold("") { acc, tile -> "$acc $tile" }} "

    }

    override fun calculateScore() : Int{
        throw NotImplementedError()
    }

}