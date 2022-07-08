package gamemockup.util

import entity.DeliveryTruck

class DeliveryTrucks {
    companion object{
        fun deliveryTrucksForTwoPlayers(): MutableList<DeliveryTruck> {
            return mutableListOf<DeliveryTruck>(
                DeliveryTruck(1),
                DeliveryTruck(2),
                DeliveryTruck(3)
            )
        }

        fun deliveryTrucksForThreePlayers(): MutableList<DeliveryTruck> {
            return mutableListOf<DeliveryTruck>(
                DeliveryTruck(3),
                DeliveryTruck(3),
                DeliveryTruck(3)
            )
        }

        fun deliveryTrucksForFourPlayers(): MutableList<DeliveryTruck> {
            return mutableListOf<DeliveryTruck>(
                DeliveryTruck(3),
                DeliveryTruck(3),
                DeliveryTruck(3),
                DeliveryTruck(3)
            )
        }
        fun deliveryTrucksForFivePlayers(): MutableList<DeliveryTruck> {
            return mutableListOf<DeliveryTruck>(
                DeliveryTruck(3),
                DeliveryTruck(3),
                DeliveryTruck(3),
                DeliveryTruck(3),
                DeliveryTruck(3)
            )
        }

    }
}