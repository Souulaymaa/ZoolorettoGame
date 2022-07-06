package gameAI.moveoracle

import entity.ZoolorettoGameState
import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import kotlin.test.BeforeTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MoveVendingStallEnclosureToBarnTest {
    var zoolorettoGameState: ZoolorettoGameState? = null
    private val vendingStall = TileLists.vendingStalls().first()

    @BeforeTest
    fun setup() {
        zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    }


    /**
     * tests when the player has insufficient coins then no possible moves must be returned and list must be empty.
     */
    @Test
    fun allMoveVendingStallEnclosureToBarnInsufficientCoinsTest() {
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 0
        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val moveList = moveOracle.allMoveVendingStallEnclosureToBarn()
        assertEquals(0, moveList.size)
    }

    /**
     * below function tests when the player has no vendingStalls in any of his enclosures and so an empty list must be returned
     */
    @Test
    fun allMoveVendingStallEnclosureToBarnNoVendingStallsTest() {
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 3
        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val moveList = moveOracle.allMoveVendingStallEnclosureToBarn()
        assertEquals(0, moveList.size)
    }

    /**
     * test the move action of vending stalls from Enclosure to Barn
     */
    @Test
    fun vendingStallEnclosureToBarn(){
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 3

        //one possible move here
        player1.playerEnclosure[0].vendingStalls.add(vendingStall)


        //one possible move here
        player1.playerEnclosure[1].vendingStalls.add(vendingStall)

        //one possible move here
        player1.playerEnclosure[2].vendingStalls.add((vendingStall))

        val moveOracle = MoveOracle(zoolorettoGameState!!)

        //fourth enclosure is empty
        val moveList = moveOracle.allMoveVendingStallEnclosureToBarn()
        assertEquals(3, moveList.size)
    }

}