package service

import entity.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.*

/**
 * Test class for the method [moveOneTile]
 */

class MoveOneTileTest {

    private val rootService = RootService()

    //initialise players
    private val playerOne = Player("player1", Difficulty.HUMAN)
    private val playerTwo = Player("player2", Difficulty.HUMAN)
    private val playerThree = Player("player3", Difficulty.HUMAN)
    val players: List<Player> = listOf( playerOne, playerTwo,playerThree)

    // initialise tiles
    val tile1 = VendingStall(StallType.VENDING1)
    val tile3 = Animal(Type.MALE, Species.E, false)
    val tile4 = Animal(Type.MALE, Species.K, false)

    //initialise enclosure
    val enclosure1 = Enclosure(6, 2, 0, Pair(1,1), false)
    val enclosure2 = Enclosure(2, 1, 0, Pair(1,1), false)
    val enclosure3 = Enclosure(4, 1, 0, Pair(1,1), false)

    /**
     * test if the playerActionService method moveOneTile works
     */
    @Test
    fun moveOneTileTest(){
        rootService.zoolorettoGameService.createZoolorettoGame(players,false)
        val game = rootService.zoolorettoGame!!.currentGameState

        enclosure1.animalTiles = arrayListOf(Animal(Type.MALE, Species.E, false))
        enclosure2.animalTiles = arrayListOf(Animal(Type.MALE, Species.K, false),
            Animal(Type.FEMALE, Species.K, false))
        enclosure3.animalTiles = arrayListOf(Animal(Type.MALE, Species.K, false))

        val player1 = game.players.peek()
        player1.coins = 8
        player1.playerEnclosure = mutableListOf(enclosure1)
        player1.barn.vendingStalls = arrayListOf(tile1)

        rootService.playerActionService.moveOneTile(player1, enclosure1, tile1)
        assertEquals(enclosure1.vendingStalls.last(), tile1)
        assertEquals(game.players.peek(), playerTwo)
        assertEquals(player1.coins, 7)

        val player2 = game.players.peek()
        player2.playerEnclosure = mutableListOf(enclosure1, enclosure2)
        player2.coins = 5
        player2.barn.animalTiles = arrayListOf(tile3)
        rootService.playerActionService.moveOneTile(player2, enclosure1, tile3)
        assertEquals(enclosure1.animalTiles.last(), tile3)
        assertEquals(game.players.peek(), playerThree)
        assertEquals(player2.coins, 4)

        assertFailsWith<IllegalStateException>{
            rootService.playerActionService.moveOneTile(player2,enclosure2, tile4)}

        val player3 = game.players.peek()
        player3.playerEnclosure = mutableListOf(enclosure3)
        player3.coins = 4
        player3.barn.animalTiles = arrayListOf(tile4)
        rootService.playerActionService.moveOneTile(player3, enclosure3, tile4)
        assertEquals(enclosure3.animalTiles.last(), tile4)
        assertEquals(game.players.peek(), player1)
        assertEquals(player3.coins, 3)

    }
}
