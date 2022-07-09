package gameAI.moveoracle

import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import kotlin.test.Test

/**
 * Class to Test [MoveOracle.determineAllTruckRelatedMoves] function.
 */
class DetermineAllTruckRelatedMovesTest {

    private val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    private val moveOracle = MoveOracle(zoolorettoGameState)

    private val flamingo = TileLists.flamingos().first()
    private val camel = TileLists.camels().first()
    private val chimpanzee = TileLists.chimpanzees().first()
    private val kangaroo = TileLists.kangaroos().first()
    private val panda = TileLists.pandas().first()

    /**
     * Checks the functionality of determineAllTruckRelatedMoves
     */
    @Test
    fun testDetermineAllTruckRelatedMoves(){

    }
}