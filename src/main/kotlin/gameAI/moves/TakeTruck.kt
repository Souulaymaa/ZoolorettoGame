package gameAI.moves

import entity.DeliveryTruck
import gameAI.Move

class TakeTruck(val truck: DeliveryTruck) : Move {

    override fun performMove() {
        super.performMove()
    }

    override fun toHintString(): String {
        return "Take truck ${truck.truckNumber}."
    }

}