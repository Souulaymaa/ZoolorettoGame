package gameAI.moveoracle

import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test

class determineAllCurrentAllowedMoves {

    private val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    private val moveOracle = MoveOracle(zoolorettoGameState)

    private val flamingo = TileLists.flamingos().first()
    private val camel = TileLists.camels().first()
    private val chimpanzee = TileLists.chimpanzees().first()
    private val kangaroo = TileLists.kangaroos().first()
    private val panda = TileLists.pandas().first()


    /**
     * test for determineAllMoneyMoves()
     */
    @Test
    fun determineAllCurrentAvailableMovesTest(){

    }
}