package service

import entity.Enclosure
import entity.ZoolorettoGame
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class exchTilesEnclosureToEnclosure {
    val rootService = RootService()
    val enclosure1 = Enclosure(11,1,4,Pair(1,1),false)
    val enclosure2 = Enclosure(11,1,4,Pair(1,1),false)

    @Test
    fun testExchangeTiles(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.players.peek()
        player.playerEnclosure.add(enclosure1)
        player.playerEnclosure.add(enclosure2)
        for(animals in TileLists.flamingos){
            game.players.peek().playerEnclosure.get(0).animalTiles.add(animals)
        }
        for (animals in TileLists.camels){
            game.players.peek().playerEnclosure.get(1).animalTiles.add(animals)
        }
        assertDoesNotThrow { rootService.playerActionService.exchangeAllTiles(
            player.playerEnclosure.get(0),
            player.playerEnclosure.get(1)
            )
        }
    }

    @Test
    fun testNoSpace(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.players.peek()
        player.playerEnclosure.add(Enclosure(1,1,1, Pair(1,1),false))
        player.playerEnclosure.add(enclosure2)
        for (animals in TileLists.camels){
            game.players.peek().playerEnclosure.get(1).animalTiles.add(animals)
        }

        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.exchangeAllTiles(
                player.playerEnclosure.get(0),
                player.playerEnclosure.get(1)
            )
        }
    }
}