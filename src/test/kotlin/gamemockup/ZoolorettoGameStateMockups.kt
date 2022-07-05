package gamemockup

import entity.ZoolorettoGameState
import gamemockup.twoplayers.TileStackForTwoPlayers
import gamemockup.util.PlayerQueues
import gamemockup.util.DeliveryTrucks
import gamemockup.util.EmptyEnclosures.Companion.emptyEnclosureDefaultFactory

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


            for(p in PlayerQueues.twoHumanPlayers){
                p.playerEnclosure.addAll(emptyEnclosureDefaultFactory())
            }
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