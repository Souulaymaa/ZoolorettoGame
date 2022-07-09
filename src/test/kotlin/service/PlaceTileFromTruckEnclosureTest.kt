package service

import entity.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

/**
 * Test cases for placeTileFromTruck (Enclosure) function from [PlayerActionService]
 */
class PlaceTileFromTruckEnclosureTest {
    //SOME values for the tests
    private val panda = Animal(Type.NONE, Species.P)
    private val pandaM = Animal(Type.MALE, Species.P)
    private val pandaF = Animal(Type.FEMALE, Species.P)
    private val player1 = Player("player1", Difficulty.HUMAN)
    private val player2 = Player("player2", Difficulty.HUMAN)
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private val deliveryTrucks = mutableListOf<DeliveryTruck>(
        DeliveryTruck(3),
        DeliveryTruck(3),
        DeliveryTruck(3))
    private val playerQ: LinkedList<Player> = LinkedList(listOf(player1, player2))
    private val vendingStall = VendingStall(StallType.VENDING1)
    private val enclosure2 = Enclosure(4, 2, 1, Pair(5, 4), false)


    /**
     * Check if more than one vending stall is added to the enclosure and deleted from the chosen truck
     */
    @Test
    fun testPlaceVendingStallsFromTruckToEnclosure() {
        //create a game
        val rootService = RootService()
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)

        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val playerAction = rootService.playerActionService
        //give player a truck with a vending stall and an enclosure
        val currentPlayer = game.currentGameState.players.peek()
        deliveryTrucks[0].tilesOnTruck.add(vendingStall)
        deliveryTrucks[0].tilesOnTruck.add(vendingStall)
        currentPlayer.playerEnclosure.add(enclosure2)
        //take truck
        playerAction.takeTruck(0)
        //place the vending stalls in the enclosure
        playerAction.placeTileFromTruck(0)
        assertEquals(vendingStall, enclosure2.vendingStalls[0])
        assertEquals(1, currentPlayer.chosenTruck!!.tilesOnTruck.size)
        assertEquals(currentPlayer, game.currentGameState.players.peek())
        playerAction.placeTileFromTruck(0)
        assertEquals(vendingStall, enclosure2.vendingStalls[1])
        assertEquals(0, currentPlayer.chosenTruck!!.tilesOnTruck.size)
        assertNotEquals(currentPlayer, game.currentGameState.players.peek())
    }

    /**
     * Check if animals are added to the enclosure and deleted from the chosen truck
     */
    @Test
    fun testPlaceAnimalsFromTruckToEnclosure() {
        //create a game
        val rootService = RootService()
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val playerAction = rootService.playerActionService
        //fill enclosure with pandas
        val currentPlayer = game.currentGameState.players.peek()
        gameState.players.peek().playerEnclosure.add(enclosure2)
        currentPlayer.playerEnclosure[0].animalTiles.add(pandaM)
        //fill truck with pandas
        deliveryTrucks[0].tilesOnTruck.add(pandaF)
        deliveryTrucks[0].tilesOnTruck.add(panda)
        //take truck
        playerAction.takeTruck(0)
        assertEquals(currentPlayer.chosenTruck, gameState.deliveryTrucks[0])
        //place the pandas in the enclosure
        playerAction.placeTileFromTruck(0)
        assertEquals(pandaF, enclosure2.animalTiles[1])
        assertEquals(Type.OFFSPRING, enclosure2.animalTiles[2].type)
        assertEquals(1, currentPlayer.chosenTruck!!.tilesOnTruck.size)
        assertEquals(currentPlayer, game.currentGameState.players.peek())
        playerAction.placeTileFromTruck(0)
        assertEquals(panda, enclosure2.animalTiles[3])
        assertEquals(0, currentPlayer.chosenTruck!!.tilesOnTruck.size)
        assertNotEquals(currentPlayer, game.currentGameState.players.peek())
        assertEquals(2, currentPlayer.coins)
    }
}