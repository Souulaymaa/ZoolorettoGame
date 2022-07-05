package gameAI.moveoracle

import entity.ZoolorettoGameState
import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class moveTileBarnToEnclosureTest {
    var zoolorettoGameState : ZoolorettoGameState? = null

    @BeforeTest
    fun setup(){
        zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    }

    /**
     * tests when the player has insufficient coins then no possible moves must be returned and list must be empty.
     */
    @Test
    fun allMoveTileFromBarnToEnclosureInsufficientCoinsTest(){
        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 0
        val moveList = moveOracle.allMoveTileFromBarnToEnclosure()
        assertEquals(0, moveList.size)
    }

    /**
     * tests when the player has sufficient coins but no tiles found in the barn aka barn is empty.
     */
    @Test
    fun allMoveTileFromBarnToEnclosureEmptyBarnTest(){
        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 3
        val moveList = moveOracle.allMoveTileFromBarnToEnclosure()
        assertEquals(0, moveList.size)
    }

    /**
     * test case for when moving animal tile from barn to one or more enclosures possible
     */
    @Test
    fun animalFromBarnToEnclosures(){
        val moveOracle = MoveOracle(zoolorettoGameState!!)
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 3

        val flamingo = TileLists.flamingos.first()
        val chimpanzee = TileLists.chimpanzees.first()
        val vendingStall = TileLists.vendingStalls.first()

        player1.barn.animalTiles.add(flamingo)
        player1.barn.animalTiles.add(chimpanzee)
        player1.barn.vendingStalls.add(vendingStall)



    }





}