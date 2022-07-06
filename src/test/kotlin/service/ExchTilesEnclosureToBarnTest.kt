package service

import entity.*
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for the function [PlayerActionService.exchangeAllTiles], which swaps the tiles between an enclosure and a
 * barn
 */
class ExchTilesEnclosureToBarnTest {
    //rootservice and an enclosure to test with
    private val rootService = RootService()
    private val enclosure1 = Enclosure(11,1,4,Pair(1,1),false)

    /**
     * Test case for an exchange under normal conditions where it has to work
     *
     * the player has 11 flamingos in the enclosure and a chimpanzee in the barn and the function tests if the exchange
     * works, if the right animals are in the enclosures and if the size of the enclosures is right after the exchange
     */
    @Test
    fun testExchangeEnclosureToBarn(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player1 = game.players.peek()
        player1.playerEnclosure.add(enclosure1)
        val initialEnclosureSize = player1.playerEnclosure[0].animalTiles.size
        val initialBarnSize = player1.barn.animalTiles.size

        assertDoesNotThrow {
            rootService.playerActionService.exchangeAllTiles(
                player1.playerEnclosure[0],
            player1,
                Animal(Type.NONE,Species.S)
            )
        }
        for(animal in player1.barn.animalTiles){
            assertEquals(Species.F,animal.species)
        }
        assertEquals(initialEnclosureSize,player1.barn.animalTiles.size)
        for(animal in player1.playerEnclosure[0].animalTiles){
            assertEquals(Species.S,animal.species)
        }
        assertEquals(initialBarnSize, player1.playerEnclosure[0].animalTiles.size)
    }

    /**
     * tests if the function throws an [IllegalArgumentException] when you try to exchange a tile from the barn
     * into an enclosure which has not enough space for the tile from the barn
     *
     * for this case we use an enclosure with maxAnimalSlots = 1 and two tiles in the barn
     */
    @Test
    fun testNoSpace(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player1 = game.players.peek()
        player1.playerEnclosure.add(Enclosure(1,1,1, Pair(1,1),false))
        //player1.playerEnclosure.get(0).animalTiles.add(Animal(Type.NONE,Species.S))
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.L))
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.L))

        assertThrows<java.lang.IllegalArgumentException> { rootService.playerActionService.exchangeAllTiles(
            player1.playerEnclosure[0],
            player1,
            Animal(Type.NONE,Species.L)
            )
        }
    }


    /**
     * tests if the function throws an [IllegalStateException] when you try to exchange the tiles of an
     * enclosure with a species which is not included in the barn
     */
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
            game.players.peek().playerEnclosure[0].animalTiles.add(animals)
        }
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.S))
        assertThrows<IllegalStateException> {
            rootService.playerActionService.exchangeAllTiles(
                player1.playerEnclosure[0],
                player1,
                Animal(Type.NONE,Species.K)
            )
        }
    }

}