package gameAI.moveoracle

import entity.ZoolorettoGameState
import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import kotlin.test.BeforeTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class moveVendingStallEnclosureToBarnTest {
    var zoolorettoGameState: ZoolorettoGameState? = null

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

}