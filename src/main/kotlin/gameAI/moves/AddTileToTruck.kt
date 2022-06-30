package gameAI.moves

import entity.DeliveryTruck
import gameAI.Move

class AddTileToTruck(private val truck: DeliveryTruck) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        //To be completed
        return "Add drawn Tile to Truck."
    }

}