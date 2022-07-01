package gamemockup.util

import entity.DeliveryTruck

class DeliveryTrucks {
    companion object{
        val deliveryTrucksForTwoPlayers = mutableListOf<DeliveryTruck>(
            DeliveryTruck(1),
            DeliveryTruck(2),
            DeliveryTruck(3)
            )

        val deliveryTrucksForThreePlayers = mutableListOf<DeliveryTruck>(
            DeliveryTruck(3),
            DeliveryTruck(3),
            DeliveryTruck(3)
        )

        val deliveryTrucksForFourPlayers = mutableListOf<DeliveryTruck>(
            DeliveryTruck(3),
            DeliveryTruck(3),
            DeliveryTruck(3),
            DeliveryTruck(3)
        )

        val deliveryTrucksForFivePlayers = mutableListOf<DeliveryTruck>(
            DeliveryTruck(3),
            DeliveryTruck(3),
            DeliveryTruck(3),
            DeliveryTruck(3),
            DeliveryTruck(3)
            )

    }
}