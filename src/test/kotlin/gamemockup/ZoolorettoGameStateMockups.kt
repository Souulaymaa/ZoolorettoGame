package gamemockup

import entity.ZoolorettoGameState
import gamemockup.twoplayers.TileStackForTwoPlayers
import gamemockup.util.PlayerQueues
import gamemockup.util.DeliveryTrucks
import gamemockup.util.EmptyEnclosures.Companion.emptyEnclosureDefaultFactory

class ZoolorettoGameStateMockups {
    companion object{
        fun twoPlayersZoolorettoGameStateFactory(): ZoolorettoGameState {
            val playerQueues = PlayerQueues.twoHumanPlayers()

            for(p in playerQueues){
                p.playerEnclosure.addAll(emptyEnclosureDefaultFactory())
            }
            return ZoolorettoGameState(
                false,
                false,
                playerQueues,
                TileStackForTwoPlayers.getTileStack(),
                DeliveryTrucks.deliveryTrucksForTwoPlayers()
            )
        }

        //TODO: CAUTION WE USE A TileStackForTwoPlayers even though we have three players
        //Only usable for ExpandZooTest, correct later if needed anywhere else!!!!!
        fun threePlayersZoolorettoGameStateFactory(): ZoolorettoGameState {
            val playerQueues = PlayerQueues.twoHumanPlayers()

            for(p in playerQueues){
                p.playerEnclosure.addAll(emptyEnclosureDefaultFactory())
            }
            return ZoolorettoGameState(
                false,
                false,
                playerQueues,
                TileStackForTwoPlayers.getTileStack(),
                DeliveryTrucks.deliveryTrucksForTwoPlayers()
            )
        }
    }
}