package gameAI.moveoracle

import entity.ZoolorettoGameState
import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import kotlin.test.BeforeTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MoveVendingStallEnclosureToEnclosureTest {

    var zoolorettoGameState : ZoolorettoGameState? = null
    private val vendingStall = TileLists.vendingStalls().first()

    @BeforeTest
    fun setup(){
        zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    }

    /**
     * tests when the player has insufficient coins then no possible moves must be returned and list must be empty.
     */
    @Test
    fun allMoveVendingStallEnclosureToEnclosureInsufficientCoinsTest(){
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 0
        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val moveList = moveOracle.allMoveVendingStallEnclosureToEnclosure()
        assertEquals(0, moveList.size)
    }

    /**
     * below function tests when the player has sufficient coins but no vendingStalls in any of his enclosures and so an
     * empty list must be returned
     */
    @Test
    fun allMoveVendingStallEnclosureToEnclosureNoVendingStallsTest(){
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
    fun vendingStallEnclosureToEnclosure(){
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 3

        //two possible moves here from 0->1 and 0-> 4
        player1.playerEnclosure[0].vendingStalls.add(vendingStall)

        //one possible move here from 1->3
        player1.playerEnclosure[1].vendingStalls.add(vendingStall)

        //two possible moves here from 2->1 and 2-> 4
        player1.playerEnclosure[2].vendingStalls.add((vendingStall))

        val moveOracle = MoveOracle(zoolorettoGameState!!)

        //fourth enclosure is empty
        val moveList = moveOracle.allMoveVendingStallEnclosureToEnclosure()
        assertEquals(5, moveList.size)
    }
}