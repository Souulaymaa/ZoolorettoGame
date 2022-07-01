package gamemockup.util

import entity.DeliveryTruck

class DeliveryTrucks {
    companion object{
        val deliveryTrucksForTwoPlayers = mutableListOf<DeliveryTruck>(
            DeliveryTruck(1),
            DeliveryTruck(2),
            DeliveryTruck(3),
            )

        val deliveryTrucksMoreThanTwoPlayers = mutableListOf<DeliveryTruck>(
            DeliveryTruck(3),
            DeliveryTruck(3),
            DeliveryTruck(3),
        )
    }
}