package service

import entity.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Test cases for the takeTruck function from [PlayerActionService]
 */
class TakeTruckTest {
    //SOME values for the tests
    private val vendingStall = VendingStall(StallType.VENDING1)
    private val panda = Animal(Type.NONE, Species.P)
    private val deliveryTruck1 = DeliveryTruck(3)
    private val deliveryTruck2 = DeliveryTruck(3)
    private val deliveryTruck3 = DeliveryTruck(3)
    private val player1 = Player("player1", Difficulty.HUMAN)
    private val player2 = Player("player2", Difficulty.HUMAN)
    private val player3 = Player("player3", Difficulty.HUMAN)
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private val deliveryTrucks = mutableListOf<DeliveryTruck>()
    private val playerQ: LinkedList<Player> = LinkedList(listOf(player1, player2, player3))

    /**
     * Check if the function take truck saves the chosen truck
     */
    @Test
    fun testTakeTruck() {
        //create a game with 3 trucks
        deliveryTruck1.tilesOnTruck.add(panda)
        deliveryTruck1.tilesOnTruck.add(Coin())
        deliveryTruck2.tilesOnTruck.add(vendingStall)
        deliveryTruck3.tilesOnTruck.add(Coin())
        deliveryTrucks.add(0, deliveryTruck1)
        deliveryTrucks.add(1, deliveryTruck2)
        deliveryTrucks.add(2, deliveryTruck3)
        val rootService = RootService()
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val playerAction = rootService.playerActionService
        //player1 takes the first truck
        val currentPlayer = game.currentGameState.players.peek()
        playerAction.takeTruck(0)
        assertEquals(2, game.currentGameState.deliveryTrucks.size)
        assertEquals(deliveryTruck1, currentPlayer.chosenTruck)
        assertTrue(currentPlayer.passed)
        assertEquals(1, currentPlayer.chosenTruck!!.tilesOnTruck.size)
        assertEquals(3, currentPlayer.coins)
    }
}