package gameAI.moveoracle

import entity.ZoolorettoGameState
import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

/**
 * Class to test the discardTile function in [MoveOracle]
 */
class DiscardTileTest {

    var zoolorettoGameState : ZoolorettoGameState? = null

    @BeforeTest
    fun setup(){
        zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    }

    /**
     * Function, that checks, if the three barn tiles from player1 are found as discard-able.
     */
    @Test
    fun threeTiles(){
        //Fill player1s barn with three Tiles
        val player1 = zoolorettoGameState!!.players.peek()
        val flamingo = TileLists.flamingos().first()
        val chimpanzee = TileLists.chimpanzees().first()
        val vendingStall = TileLists.vendingStalls().first()

        player1.barn.animalTiles.add(flamingo)
        player1.barn.animalTiles.add(chimpanzee)
        player1.barn.vendingStalls.add(vendingStall)
        player1.coins = 4

        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val moveList = moveOracle.discardTileMoves()

        //Bugs: no usage of the root service
        //Does not loop trough barn
        assertEquals(2, moveList.size)
    }

    @Test
    fun noMoney(){
        //Fill player1s barn with three Tiles
        val player1 = zoolorettoGameState!!.players.peek()
        val flamingo = TileLists.flamingos().first()
        val chimpanzee = TileLists.chimpanzees().first()
        val vendingStall = TileLists.vendingStalls().first()

        player1.barn.animalTiles.add(flamingo)
        player1.barn.animalTiles.add(chimpanzee)
        player1.barn.vendingStalls.add(vendingStall)
        player1.coins = 0

        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val moveList = moveOracle.discardTileMoves()

        //Bugs: no usage of the root service
        //Does not loop trough barn
        assertEquals(0, moveList.size)
    }
}