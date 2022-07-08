package service

import entity.*
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test cases for the purchaseTile function from [PlayerActionService]
 */

class PurchaseTileTest {

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
    private val playerQ: Queue<Player> = LinkedList(listOf(player1, player2))
    private val vendingStall = VendingStall(StallType.VENDING1)
    private val enclosure = Enclosure(5, 1, 2, Pair(8, 5), false)


    /**
     * Check if animal and vending stalls can be purchased
     */
    @Test
    fun testBasicPurchase() {
        //create a game
        val rootService = RootService()
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val playerAction = rootService.playerActionService
        //give each player an enclosure
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player1Enclosure = gameState.players.peek().playerEnclosure[0]
        val player1 = gameState.players.poll()
        gameState.players.add(player1)
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player2Enclosure = gameState.players.peek().playerEnclosure[0]
        val player2 = gameState.players.poll()
        gameState.players.add(player2)
        //add tiles to barns
        player2.barn.vendingStalls.add(vendingStall)
        player1.barn.animalTiles.add(panda)
        //player 1 buys vendingStall
        playerAction.purchaseTile(player2, vendingStall, 0)
        var currentPlayer = gameState.players.peek()
        assertEquals(0, player1.coins)
        assertEquals(3, player2.coins)
        assertEquals(1, gameState.bank)
        assertEquals(vendingStall, player1Enclosure.vendingStalls[0])
        assertEquals(currentPlayer, player2)
        //player 2 buys panda
        playerAction.purchaseTile(player1, panda, 0)
        currentPlayer = gameState.players.poll()
        assertEquals(1, player2Enclosure.animalTiles.size)
        assertEquals(1, player1.coins)
        assertEquals(1, player2.coins)
        assertEquals(2, gameState.bank)
        assertEquals(panda, player2Enclosure.animalTiles[0])
        assertEquals(currentPlayer, player1)
        assertEquals(player2, gameState.players.peek()) //player 2 should be added after line 541


    }

    /**
     * Check if filling an enclosure through purchasing and producing an offspring in the enclosure
     * gives the right amount of coins
     */
    @Test
    fun testPurchaseWithOffspringInFullEnclosure() {
        //create a game
        val rootService = RootService()
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val playerAction = rootService.playerActionService
        //give each player an enclosure
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player1Enclosure = gameState.players.peek().playerEnclosure[0]
        val player1 = gameState.players.poll()
        gameState.players.add(player1)
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player2Enclosure = gameState.players.peek().playerEnclosure[0]
        val player2 = gameState.players.poll()
        gameState.players.add(player2)
        //add tile to barn
        player2.barn.animalTiles.add(pandaF)
        //fill enclosure to get bonus coins
        player1Enclosure.animalTiles.add(panda)
        player1Enclosure.animalTiles.add(panda)
        player1Enclosure.animalTiles.add(pandaM)
        playerAction.purchaseTile(player2, pandaF, 0)
        val currentPlayer = gameState.players.poll()
        assertEquals(5, player1Enclosure.animalTiles.size)
        assertEquals(1, player1.coins)
        assertEquals(3, player2.coins)
        assertEquals(0, gameState.bank)
        assertEquals(Type.OFFSPRING, player1Enclosure.animalTiles[4].type)
        assertEquals(currentPlayer, player2)
        assertEquals(gameState.players.peek(), player1)
    }

    /**
     * Check if not filling the enclosure through purchasing gives the right amount of coins
     */
    @Test
    fun testPurchaseWithOffspringInEnclosure() {
        //create a game
        val rootService = RootService()
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val playerAction = rootService.playerActionService
        //give each player an enclosure
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player1Enclosure = gameState.players.peek().playerEnclosure[0]
        val player1 = gameState.players.poll()
        gameState.players.add(player1)
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player2Enclosure = gameState.players.peek().playerEnclosure[0]
        val player2 = gameState.players.poll()
        gameState.players.add(player2)
        //add tile to barn
        player2.barn.animalTiles.add(pandaF)
        //fill enclosure to get bonus coins
        player1Enclosure.animalTiles.add(pandaM)
        playerAction.purchaseTile(player2, pandaF, 0)
        val currentPlayer = gameState.players.poll()
        assertEquals(3, player1Enclosure.animalTiles.size)
        assertEquals(0, player1.coins)
        assertEquals(3, player2.coins)
        assertEquals(1, gameState.bank)
        assertEquals(Type.OFFSPRING, player1Enclosure.animalTiles[2].type)
        assertEquals(currentPlayer, player2)
        assertEquals(gameState.players.peek(), player1)
    }

    /**
     * Check if filling an enclosure through purchasing and producing an offspring in the barn
     * gives the right amount of coins
     */
    @Test
    fun testPurchaseWithOffspringsInBarn() {
        //create a game
        val rootService = RootService()
        val gameState = ZoolorettoGameState(false, false, playerQ, tileStack, deliveryTrucks)
        val game = ZoolorettoGame(1.50f, gameState)
        rootService.zoolorettoGame = game
        val playerAction = rootService.playerActionService
        //give each player an enclosure
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player1Enclosure = gameState.players.peek().playerEnclosure[0]
        val player1 = gameState.players.poll()
        gameState.players.add(player1)
        gameState.players.peek().playerEnclosure.add(enclosure)
        val player2Enclosure = gameState.players.peek().playerEnclosure[0]
        val player2 = gameState.players.poll()
        gameState.players.add(player2)
        //add tile to barn
        player2.barn.animalTiles.add(pandaF)
        //fill enclosure to get bonus coins
        gameState.bank = 4
        player1Enclosure.animalTiles.add(panda)
        player1Enclosure.animalTiles.add(panda)
        player1Enclosure.animalTiles.add(panda)
        player1Enclosure.animalTiles.add(pandaM)
        playerAction.purchaseTile(player2, pandaF, 0)
        val currentPlayer = gameState.players.poll()
        assertEquals(5, player1Enclosure.animalTiles.size)
        assertEquals(1, player1.barn.animalTiles.size)
        assertEquals(2, player1.coins)
        assertEquals(3, player2.coins)
        assertEquals(3, gameState.bank) // bonusCoin should be changed from -+ to -=
        assertEquals(Type.OFFSPRING, player1.barn.animalTiles[0].type)
        assertEquals(currentPlayer, player2)
        assertEquals(gameState.players.peek(), player1)

    }

}