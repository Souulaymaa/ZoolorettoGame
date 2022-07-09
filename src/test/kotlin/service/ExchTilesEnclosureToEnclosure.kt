package service

import entity.*
import gamemockup.util.TileLists
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import java.util.*
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [PlayerActionService.exchangeAllTiles] with source = Enclosure and destination = Enclosure
 */
class ExchTilesEnclosureToEnclosure {
    private val deliveryTruck1 = DeliveryTruck(3)
    private val deliveryTruck2 = DeliveryTruck(3)
    private val deliveryTruck3 = DeliveryTruck(3)
    private val player1 = Player("player1", Difficulty.HUMAN)
    private val player2 = Player("player2", Difficulty.HUMAN)
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private val playerQ: Queue<Player> = LinkedList(listOf(player1, player2))

    /**
     * tests if the function works right when the conditions are alright to exchange the tiles between two [Enclosure]s
     */
    @Test
    fun testExchangeTiles(){
        val rootService = RootService()
        val deliveryTrucks = mutableListOf<DeliveryTruck>()
        deliveryTrucks.add(0, deliveryTruck1)
        deliveryTrucks.add(1, deliveryTruck2)
        deliveryTrucks.add(2, deliveryTruck3)
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val player = game.currentGameState.players.peek()
        player.playerEnclosure.add(Enclosure(11,1,4,Pair(1,1),false))
        player.playerEnclosure.add(Enclosure(11,1,4,Pair(1,1),false))
        for(animals in TileLists.flamingos()){

            player.playerEnclosure[0].animalTiles.add(animals)
        }
        for (animals in TileLists.camels()){
            player.playerEnclosure[1].animalTiles.add(animals)
        }
        assertDoesNotThrow { rootService.playerActionService.exchangeAllTiles(0,1)
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
        val rootService = RootService()
        val deliveryTrucks = mutableListOf<DeliveryTruck>()
        deliveryTrucks.add(0, deliveryTruck1)
        deliveryTrucks.add(1, deliveryTruck2)
        deliveryTrucks.add(2, deliveryTruck3)
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val player = game.currentGameState.players.peek()
        player.playerEnclosure.add(Enclosure(1,1,1, Pair(1,1),false))
        player.playerEnclosure.add(Enclosure(11,1,4,Pair(1,1),false))
        player.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.F))
        for (animals in TileLists.camels()){
            player.playerEnclosure[1].animalTiles.add(animals)
        }
        player.coins = 2
        player.passed = false
        assertThrows<IllegalStateException> {
            rootService.playerActionService.exchangeAllTiles(0,1)
        }
    }
    @AfterTest

    /**
     * tests if the function throws a [IllegalArgumentException] when the [Player] has no coins to perform the
     * money action
     */
    @Test
    fun testNoCoins(){
        val rootService = RootService()
        val deliveryTrucks = mutableListOf<DeliveryTruck>()
        deliveryTrucks.add(0, deliveryTruck1)
        deliveryTrucks.add(1, deliveryTruck2)
        deliveryTrucks.add(2, deliveryTruck3)
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val player = game.currentGameState.players.peek()
        player.playerEnclosure.add(Enclosure(11,1,4,Pair(1,1),false))
        player.playerEnclosure.add(Enclosure(11,1,4,Pair(1,1),false))
        player.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.U))
        player.playerEnclosure[1].animalTiles.add(Animal(Type.NONE,Species.P))
        player.coins = 0

        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.exchangeAllTiles(0,1)
        }
    }
    @AfterTest
    /**
     * tests if the function throws a [IllegalArgumentException] when the player, who wants to perform the action has
     * already taken a truck
     */
    @Test
    fun testHasChecked(){
        val rootService = RootService()
        val deliveryTrucks = mutableListOf<DeliveryTruck>()
        deliveryTrucks.add(0, deliveryTruck1)
        deliveryTrucks.add(1, deliveryTruck2)
        deliveryTrucks.add(2, deliveryTruck3)
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val player = game.currentGameState.players.peek()
        player.playerEnclosure.add(Enclosure(11,1,4,Pair(1,1),false))
        player.playerEnclosure.add(Enclosure(11,1,4,Pair(1,1),false))
        player.playerEnclosure[0].animalTiles.add(Animal(Type.NONE,Species.U))
        player.playerEnclosure[1].animalTiles.add(Animal(Type.NONE,Species.P))
        player.passed = true
        assertThrows<IllegalArgumentException> {
            rootService.playerActionService.exchangeAllTiles(0,1)
        }
    }
}