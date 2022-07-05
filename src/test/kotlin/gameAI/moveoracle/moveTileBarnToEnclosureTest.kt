package gameAI.moveoracle

import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class moveTileBarnToEnclosureTest {
    private val gameInstance = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
    var moveOracle = MoveOracle(gameInstance)
    private val player1 = gameInstance.players.peek()

    /**
     * tests when the player has insufficient coins then no possible moves must be returned and list must be empty.
     */
    @Test
    fun allMoveTileFromBarnToEnclosureInsufficientCoinsTest(){
        player1.coins = 0
        val moveList = moveOracle.allMoveTileFromBarnToEnclosure()
        assertEquals(0, moveList.size)
    }

    /**
     * tests when the player has sufficient coins but no tiles found in the barn aka barn is empty.
     */
    @Test
    fun allMoveTileFromBarnToEnclosureEmptyBarnTest(){
        player1.coins = 3
        val moveList = moveOracle.allMoveTileFromBarnToEnclosure()
        assertEquals(0, moveList.size)
    }




}