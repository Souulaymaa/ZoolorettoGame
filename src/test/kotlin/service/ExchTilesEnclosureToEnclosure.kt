package service

import entity.*
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [PlayerActionService.exchangeAllTiles] with source = Enclosure and destination = Enclosure
 */
class ExchTilesEnclosureToEnclosure {
    private val rootService = RootService()
    private val enclosure1 = Enclosure(11,1,4,Pair(1,1),false)
    private val enclosure2 = Enclosure(11,1,4,Pair(1,1),false)

    /**
     * tests if the function works right when the conditions are alright to exchange the tiles between two [Enclosure]s
     */
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

            player.playerEnclosure[0].animalTiles.add(animals)
        }
        for (animals in TileLists.camels){
            player.playerEnclosure[1].animalTiles.add(animals)
        }
        assertDoesNotThrow { rootService.playerActionService.exchangeAllTiles(
            player.playerEnclosure[0],
            player.playerEnclosure[1]
            )
        }
        for(animal in player.playerEnclosure[0].animalTiles){
            assertEquals(Species.K,animal.species)
        }
        for(animal in player.playerEnclosure[1].animalTiles){
            assertEquals(Species.F,animal.species)
        }
    }
    @AfterTest

    /**
     * tests if the function throws a [IllegalArgumentException] when one [Enclosure] has not enough space to exchange
     * the tiles
     */
    @Test
    fun testNoSpace(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.players.peek()
        player.passed = false
        player.coins = 2
        player.playerEnclosure.add(Enclosure(1,1,1, Pair(1,1),false))
        player.playerEnclosure.add(enclosure2)
        player.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.F))
        for (animals in TileLists.camels){
            game.players.peek().playerEnclosure[1].animalTiles.add(animals)
        }

        assertThrows<IllegalStateException> {
            rootService.playerActionService.exchangeAllTiles(
                player.playerEnclosure[0],
                player.playerEnclosure[1]
            )
        }
    }


    /**
     * tests if the function throws a [IllegalArgumentException] when the [Player] has no coins to perform the
     * money action
     */
    @Test
    fun testNoCoins(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.players.peek()
        player.playerEnclosure.add(enclosure1)
        player.playerEnclosure.add(enclosure2)
        player.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.U))
        player.playerEnclosure[1].animalTiles.add(Animal(Type.NONE,Species.P))
        player.coins = 0

        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.exchangeAllTiles(
                player.playerEnclosure[0],
                player.playerEnclosure[1]
            )
        }
    }

    /**
     * tests if the function throws a [IllegalArgumentException] when the player, who wants to perform the action has
     * already taken a truck
     */
    @Test
    fun testHasChecked(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player = game.players.peek()
        player.playerEnclosure.add(enclosure1)
        player.playerEnclosure.add(enclosure2)
        player.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.U))
        player.playerEnclosure[1].animalTiles.add(Animal(Type.NONE,Species.P))
        player.passed = true
        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.exchangeAllTiles(
                player.playerEnclosure[0],
                player.playerEnclosure[1]
            )
        }
    }

    /**
     * tests if the function throws a [IllegalArgumentException] when you try to exchange the tiles with an
     * enclosure of another player
     */
    @Test
    fun testTwoPlayer(){
        rootService.zoolorettoGame = ZoolorettoGame(1.0f,
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState)
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val game = rootService.currentGame
        checkNotNull(game)
        val player1 = game.players.poll()
        game.players.add(player1)
        val player2 = game.players.poll()
        game.players.add(player2)
        player1.playerEnclosure.add(enclosure1)
        player2.playerEnclosure.add(enclosure2)
        player1.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.U))
        player2.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.P))

        assertThrows<IllegalStateException> {
            rootService.playerActionService.exchangeAllTiles(
                player1.playerEnclosure[0],
                player2.playerEnclosure[0]
            )
        }
    }
}