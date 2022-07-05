package gamemockup

import entity.ZoolorettoGameState
import gamemockup.twoplayers.TileStackForTwoPlayers
import gamemockup.util.PlayerQueues
import gamemockup.util.DeliveryTrucks

class ZoolorettoGameStateMockups {
    companion object{
        val twoPlayersZoolorettoGameState = ZoolorettoGameState(
            false,
            false,
            PlayerQueues.twoHumanPlayers,
            TileStackForTwoPlayers.tileStack,
            DeliveryTrucks.deliveryTrucksForTwoPlayers
        )

        fun twoPlayersZoolorettoGameStateFactory(): ZoolorettoGameState {
            return ZoolorettoGameState(
                false,
                false,
                PlayerQueues.twoHumanPlayers,
                TileStackForTwoPlayers.tileStack,
                DeliveryTrucks.deliveryTrucksForTwoPlayers
            )
        }
    }
}