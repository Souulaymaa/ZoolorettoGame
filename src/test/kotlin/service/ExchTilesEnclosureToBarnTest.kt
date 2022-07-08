package service

import entity.*
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for the function [PlayerActionService.exchangeAllTiles], which swaps the tiles between an enclosure and a
 * barn
 */
class ExchTilesEnclosureToBarnTest {
    //rootservice and an enclosure to test with
    val rootService = RootService()
    private val deliveryTruck1 = DeliveryTruck(3)
    private val deliveryTruck2 = DeliveryTruck(3)
    private val deliveryTruck3 = DeliveryTruck(3)
    private val player1 = Player("player1", Difficulty.HUMAN)
    private val player2 = Player("player2", Difficulty.HUMAN)
    private val player3 = Player("player3", Difficulty.HUMAN)
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private val deliveryTrucks = mutableListOf<DeliveryTruck>()
    private val playerQ: Queue<Player> = LinkedList(listOf(player1, player2, player3))

    /**
     * Test case for an exchange under normal conditions where it has to work
     *
     * the player has 11 flamingos in the enclosure and a chimpanzee in the barn and the function tests if the exchange
     * works, if the right animals are in the enclosures and if the size of the enclosures is right after the exchange
     */
    @Test
    fun testExchangeEnclosureToBarn(){
        deliveryTrucks.add(0, deliveryTruck1)
        deliveryTrucks.add(1, deliveryTruck2)
        deliveryTrucks.add(2, deliveryTruck3)
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val player1 = game.currentGameState.players.peek()
        player1.playerEnclosure.add(Enclosure(11,1,1,Pair(1,1),false))
        for(animals in TileLists.flamingos()){
            player1.playerEnclosure.get(0).animalTiles.add(animals)
        }
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.S))
        val initialEnclosureSize = player1.playerEnclosure[0].animalTiles.size
        val initialBarnSize = player1.barn.animalTiles.size
            rootService.playerActionService.exchangeAllTiles(
                player1.playerEnclosure[0],
                player1,
                Animal(Type.NONE,Species.S)
            )
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
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory())
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val game = rootService.currentGame
        checkNotNull(game)
        val player1 = game.players.peek()
        player1.playerEnclosure.add(Enclosure(1,1,1, Pair(1,1),false))
        player1.playerEnclosure.get(0).animalTiles.add(Animal(Type.NONE,Species.S))
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.L))
        player1.barn.animalTiles.add(Animal(Type.NONE,Species.L))

        assertThrows<IllegalStateException> { rootService.playerActionService.exchangeAllTiles(
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
            ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory())
        rootService.currentGame = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val game = rootService.currentGame
        checkNotNull(game)
        val player1 = game.players.peek()
        player1.playerEnclosure.add(Enclosure(11,1,1,Pair(1,1),false))

        for(animals in TileLists.flamingos()){
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

    /**
     * tests if an offspring will be created when two [Animal]s are getting exchanged from the barn into the enclosure
     * which can have a child together
     */
    @Test
    fun testBaby(){
        deliveryTrucks.add(0, deliveryTruck1)
        deliveryTrucks.add(1, deliveryTruck2)
        deliveryTrucks.add(2, deliveryTruck3)
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val player1 = game.currentGameState.players.peek()
        player1.playerEnclosure.add(Enclosure(11,1,1,Pair(1,1),false))
        player1.barn.animalTiles.add(Animal(Type.FEMALE,Species.S))
        player1.barn.animalTiles.add(Animal(Type.MALE,Species.S))
        val initialBarnSize = player1.barn.animalTiles.size
        player1.playerEnclosure[0].animalTiles.add(Animal(Type.FEMALE,Species.U))
        rootService.playerActionService.exchangeAllTiles(
            player1.playerEnclosure[0],
            player1,
            player1.barn.animalTiles[0]
        )
        assertEquals(initialBarnSize + 1,player1.playerEnclosure[0].animalTiles.size)
    }
}