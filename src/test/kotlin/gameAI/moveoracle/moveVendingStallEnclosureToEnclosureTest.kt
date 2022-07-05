package gameAI.moveoracle

import entity.ZoolorettoGame
import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class moveVendingStallEnclosureToEnclosureTest {

    private val gameInstance = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
    private val zooGame = ZoolorettoGame(1f, gameInstance)
    var moveOracle = MoveOracle(gameInstance)
    private val player1 = gameInstance.players.peek()

//    /**
//     * tests when the player has insufficient coins then no possible moves must be returned and list must be empty.
//     */
//    @Test
//    fun allMoveVendingStallEnclosureToEnclosureInsufficientCoinsTest(){
//        player1.coins = 0
//        moveOracle.rootService.zoolorettoGame = zooGame
//        moveOracle.rootService.currentGame = gameInstance
//        val moveList = moveOracle.allMoveVendingStallEnclosureToEnclosure()
//        assertEquals(0, moveList.size)
//    }
//
//    /**
//     * below function tests when the player has sufficient coins but no vendingStalls in any of his enclosures and so an
//     * empty list must be returned
//     */
//    @Test
//    fun allMoveVendingStallEnclosureToEnclosureNoVendingStallsTest(){
//        player1.coins = 3
//        moveOracle.rootService.zoolorettoGame = zooGame
//        moveOracle.rootService.currentGame = gameInstance
//        val moveList = moveOracle.allMoveVendingStallEnclosureToBarn()
//        assertEquals(0, moveList.size)
//    }
}