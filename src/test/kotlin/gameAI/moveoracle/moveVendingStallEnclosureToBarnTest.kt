package gameAI.moveoracle

import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class moveVendingStallEnclosureToBarnTest {
    private val gameInstance = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
    var moveOracle = MoveOracle(gameInstance)
    private val player1 = gameInstance.players.peek()

    /**
     * tests when the player has insufficient coins then no possible moves must be returned and list must be empty.
     */
    @Test
    fun allMoveVendingStallEnclosureToBarnInsufficientCoinsTest(){
        player1.coins = 0
        val moveList = moveOracle.allMoveVendingStallEnclosureToBarn()
        assertEquals(0, moveList.size)
    }

    /**
     * below function tests when the player has no vendingStalls in any of his enclosures and so an empty list must be returned
     */
    @Test
    fun allMoveVendingStallEnclosureToBarnNoVendingStallsTest(){
        player1.coins = 3
        val moveList = moveOracle.allMoveVendingStallEnclosureToBarn()
        assertEquals(0, moveList.size)
    }
}