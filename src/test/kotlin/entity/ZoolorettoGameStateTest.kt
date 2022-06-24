package entity

import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNotNull

/**
 * Test case for [ZoolorettoGameState]
 */
class ZoolorettoGameStateTest {

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
     * Checks if the [ZoolorettoGameState] object is created correctly with the provided constructor parameters.
     * Also ensures that the attributes are initialized as expected.
     */
    @Test
    fun testZoolorettoGameStateInit(){

        deliveryTrucks.add(deliveryTruck1)
        deliveryTrucks.add(deliveryTruck2)
        deliveryTrucks.add(deliveryTruck3)
        player1.playerEnclosure = playerEnclosures
        players.add(player1)
        players.add(player2)

        val zoolorettoGameState = ZoolorettoGameState(
            players = players, tileStack = tileStack,
            deliveryTrucks = deliveryTrucks
        )

        assertNotNull(zoolorettoGameState)

        assertEquals(false, zoolorettoGameState.paused)
        assertEquals(false, zoolorettoGameState.roundDisc)
        assertEquals(0, zoolorettoGameState.bank)

        // Checks the correct order of the players in the queue.
        assertEquals(zoolorettoGameState.players.peek(), player1)

        // Checks the correct initialization of the tileStack.
        assertNotNull(zoolorettoGameState.tileStack.drawStack)
        assertNotNull(zoolorettoGameState.tileStack.endStack)

        // Checks the expected number of tiles of the truck at index 0 after initialization.
        assertEquals(0, zoolorettoGameState.deliveryTrucks[0].tilesOnTruck.size)

        // checks the expected drawStack size after initialization.
        assertEquals(zoolorettoGameState.tileStack.drawStack.size, 0)
    }

    /**
     * ensures the correct behavior, when initializing [ZoolorettoGameState] with wrong number of delivery trucks.
     */
    @Test
    fun testZoolorettoGameStateWithWrongNumberOfDeliveryTrucks(){

        // number of delivery trucks must be within the range 3 to 5.
        deliveryTrucks.add(deliveryTruck1)
        deliveryTrucks.add(deliveryTruck2)

        players.add(player1)
        players.add(player2)

        // initialization should fail.
        assertFails { ZoolorettoGameState(players = players, tileStack = tileStack, deliveryTrucks = deliveryTrucks) }
    }

    /**
     * ensures the correct behavior, when initializing [ZoolorettoGameState] with wrong number of players.
     */
    @Test
    fun testZoolorettoGameStateWithWrongNumberOfPlayers(){

        deliveryTrucks.add(deliveryTruck1)
        deliveryTrucks.add(deliveryTruck2)
        deliveryTrucks.add(deliveryTruck3)

        // number of players must be within the range 2 to 5.
        players.add(player1)

        // initialization should fail.
        assertFails { ZoolorettoGameState(players = players, tileStack = tileStack, deliveryTrucks = deliveryTrucks) }

    }


}