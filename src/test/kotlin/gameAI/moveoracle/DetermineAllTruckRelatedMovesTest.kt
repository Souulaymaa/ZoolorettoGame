package gameAI.moveoracle

import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.DeliveryTrucks
import gamemockup.util.TileLists
import kotlin.test.Test
import kotlin.test.assertEquals

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
        val trucks = moveOracle.currentGameStateCopy.deliveryTrucks
        var moveList = moveOracle.determineAllTruckRelatedMoves()
        assertEquals(3, moveList.size)

        trucks[0].tilesOnTruck.add(flamingo)
        moveList = moveOracle.determineAllTruckRelatedMoves()
        assertEquals(3, moveList.size)

        trucks[2].tilesOnTruck.add(camel)
        moveList= moveOracle.determineAllTruckRelatedMoves()
        assertEquals(4, moveList.size)

    }
}