package gameAI.moveoracle

import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class DetermineAllCurrentAllowedMoves {

    private val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    private val moveOracle = MoveOracle(zoolorettoGameState)

    private val chimpanzee = TileLists.chimpanzees().first()
    private val kangaroo = TileLists.kangaroos().first()
    private val panda = TileLists.pandas().first()
    private val v1 = TileLists.vendingStalls().first()

    /**
     * test for determineAllMoneyMoves() when current player has already passed
     */
    @Test
    fun determineAllCurrentAvailableMovesTestWhenPassed(){
        val player1 = moveOracle.currentGameStateCopy.players.peek()
        player1.passed = true
        assertEquals(0, moveOracle.determineAllCurrentAllowedMoves().size)
    }

    /**
     * test for determineAllMoneyMoves() when current player
     */
    @Test
    fun determineAllCurrentAvailableMovesTest(){
        val player1 = moveOracle.currentGameStateCopy.players.peek()
        player1.playerEnclosure[0].animalTiles.add(panda)
        player1.playerEnclosure[0].animalTiles.add(panda)
        player1.playerEnclosure[0].vendingStalls.add(v1)

        player1.playerEnclosure[1].animalTiles.add(kangaroo)
        player1.playerEnclosure[2].animalTiles.add(chimpanzee)
        player1.playerEnclosure[2].vendingStalls.add(v1)

        assertEquals(12, moveOracle.determineAllCurrentAllowedMoves().size)
    }
}