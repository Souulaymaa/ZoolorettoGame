package service

import entity.*
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test

class exchTilesEnclosureToBarnTest {
    val rootService = RootService()
    val enclosure1 = Enclosure(11,1,4,Pair(1,1),false)

    @Test
    fun testExchangeEnclosureToBarn(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player1 = game.players.peek()
        player1.playerEnclosure.add(enclosure1)
        for(animals in TileLists.flamingos){
            game.players.peek().playerEnclosure.get(0).animalTiles.add(animals)
        }
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.S))
        assertDoesNotThrow {
            rootService.playerActionService.exchangeAllTiles(
            player1.playerEnclosure.get(0),
            player1,
                Animal(Type.NONE,Species.S)
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
        val player1 = game.players.peek()
        player1.playerEnclosure.add(Enclosure(1,1,1, Pair(1,1),false))
        player1.playerEnclosure.get(0).animalTiles.add(Animal(Type.NONE,Species.S))
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.L))
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.L))

        assertThrows<java.lang.IllegalStateException> { rootService.playerActionService.exchangeAllTiles(
            player1.playerEnclosure.get(0),
            player1,
            Animal(Type.NONE,Species.L)
            )
        }
    }


    @Test
    fun testWrongAnimal(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player1 = game.players.peek()
        player1.playerEnclosure.add(enclosure1)
        for(animals in TileLists.flamingos){
            game.players.peek().playerEnclosure.get(0).animalTiles.add(animals)
        }
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.S))
        assertThrows<IllegalStateException> {
            rootService.playerActionService.exchangeAllTiles(
                player1.playerEnclosure.get(0),
                player1,
                Animal(Type.NONE,Species.K)
            )
        }
    }

}