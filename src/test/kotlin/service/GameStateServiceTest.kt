package service

import entity.*
import java.util.*
import kotlin.test.*

/**
 * Test Cases for [GameStateService]
 */
class GameStateServiceTest {

    private val rootService = RootService()
    private val gameStateService = GameStateService(rootService)

    // deliveryTrucks is needed for the constructor of ZoolorettoGameState
    private val deliveryTruck1 = DeliveryTruck()
    private val deliveryTruck2 = DeliveryTruck()
    private val deliveryTruck3 = DeliveryTruck()
    private val deliveryTrucks = mutableListOf<DeliveryTruck>()

    // tileStack is needed for the constructor of ZoolorettoGameState.
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())

    // playerEnclosures and a barn are needed to create the players.
    private val enclosure1 = Enclosure(6, 1, 0, Pair(4,3), false)
    private val enclosure2 = Enclosure(5, 2, 3, Pair(5,4), false)
    private val enclosure3 = Enclosure(4, 2, 2, Pair(6,3), false)
    private val playerEnclosures = mutableListOf(enclosure1, enclosure2, enclosure3)

    // Player Queue is also needed for the constructor of ZoolorettoGameState.
    private val player1 = Player("Player 1", Difficulty.HUMAN)
    private val player2 = Player("Player 2", Difficulty.HUMAN)
    private val players: Queue<Player> = LinkedList()

    /**
     * tests the [GameStateService.deepZoolorettoCopy] function.
     * Checks if the [ZoolorettoGameState] is copied correctly
     */
    @Test
    fun testDeepZoolorettoCopy(){
        deliveryTrucks.add(deliveryTruck1)
        deliveryTrucks.add(deliveryTruck2)
        deliveryTrucks.add(deliveryTruck3)
        player1.playerEnclosure = playerEnclosures
        player2.playerEnclosure = playerEnclosures
        players.add(player1)
        players.add(player2)

        val zoolorettoGameState = ZoolorettoGameState(
            players = players, tileStack = tileStack,
            deliveryTrucks = deliveryTrucks
        )

        // Creates a copy of the zoolorettoGameState
        val gameCopy = gameStateService.deepZoolorettoCopy(zoolorettoGameState)

        // Ensures that zoolorettoGameState and it's copy are equal by checking the equality of the different attributes.
        assertEquals(zoolorettoGameState.players.peek(), gameCopy.players.peek())
        assertEquals(zoolorettoGameState.deliveryTrucks, gameCopy.deliveryTrucks)
        assertEquals(zoolorettoGameState.tileStack, gameCopy.tileStack)

        // Ensures that zoolorettoGameState and it's copy have different references by checking the inequality
        //of the references of each attribute.
        assertNotSame(zoolorettoGameState.players.peek(), gameCopy.players.peek())
        assertNotSame(zoolorettoGameState.deliveryTrucks, gameCopy.deliveryTrucks)
        assertNotSame(zoolorettoGameState.tileStack, gameCopy.tileStack)



    }
}