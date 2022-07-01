package entity

import kotlin.test.Test
import kotlin.test.assertEquals
import java.util.*
import kotlin.test.assertNotNull

/**
 * Test class of the zooloretto game class
 */
class ZoolorettoGameTest {

    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())

    private val deliveryTrucks = mutableListOf<DeliveryTruck>()
    private val deliveryTruck1 = DeliveryTruck()
    private val deliveryTruck2 = DeliveryTruck()
    private val deliveryTruck3 = DeliveryTruck()

    private val barn = Enclosure(88, 12, 0, Pair(0,0), true)
    private val enclosure1 = Enclosure(6, 1, 0, Pair(10,6), false)
    private val enclosure2 = Enclosure(5, 1, 0, Pair(8,5), false)
    private val enclosure3 = Enclosure(4, 2, 0, Pair(5,4), false)

    /**
     * test if the zoolorettoGame and currentGameState aren't null
     * and if the number of players and trucks is right.
     */
    @Test
    fun testZoolorettoGame() {
        deliveryTrucks.add(deliveryTruck1)
        deliveryTrucks.add(deliveryTruck2)
        deliveryTrucks.add(deliveryTruck3)

        val playerEnclosures = mutableListOf<Enclosure>()
        playerEnclosures.add(barn)
        playerEnclosures.add(enclosure1)
        playerEnclosures.add(enclosure2)
        playerEnclosures.add(enclosure3)

        val players : Queue<Player> = LinkedList()
        players.add(Player("Beshr", Difficulty.HUMAN))
        players.add(Player("Basheer", Difficulty.HUMAN))

        val currentGameState = ZoolorettoGameState(
            players = players, tileStack = tileStack,
            deliveryTrucks = deliveryTrucks
        )
        val zoolorettoGame = ZoolorettoGame(1.0f, currentGameState)

        assertNotNull(zoolorettoGame)
        assertNotNull(currentGameState)
        assertEquals(zoolorettoGame.currentGameState.players.size, 2)
        assertEquals(zoolorettoGame.currentGameState.deliveryTrucks.size, 3)
    }

}
