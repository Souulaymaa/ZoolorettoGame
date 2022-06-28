package gameAI.moves

import entity.DeliveryTruck
import gameAI.Move

class AddTileToTruck(val truck: DeliveryTruck) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Add drawn Tile to Truck ${truck.truckNumber}."
    }

}