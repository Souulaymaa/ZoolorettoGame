package gameAI.moveoracle

import entity.ZoolorettoGameState
import gameAI.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class MoveTileBarnToEnclosureTest {
    var zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    val moveOracle = MoveOracle(zoolorettoGameState)
    private val flamingo = TileLists.flamingos().first()
    private val chimpanzee = TileLists.chimpanzees().first()
    private val vendingStall = TileLists.vendingStalls().first()

    @BeforeTest
    fun setup(){
        zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    }

    /**
     * tests when the player has insufficient coins then no possible moves must be returned and list must be empty.
     */
    @Test
    fun allMoveTileFromBarnToEnclosureInsufficientCoinsTest(){
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
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 3

        player1.barn.animalTiles.add(flamingo)


        //one possible move from barn to enclosure1 available
        for (i in 0..3){
            player1.playerEnclosure[0].animalTiles.add(flamingo)
        }

        //no possible move here
        player1.playerEnclosure[1].animalTiles.add(chimpanzee)

        //third enclosure contains also flamingo but is full
        for (i in 0..5){
            player1.playerEnclosure[2].animalTiles.add((flamingo))
        }

        val moveOracle = MoveOracle(zoolorettoGameState!!)

        //fourth enclosure is empty, 1 moves available as well
        //no vending stalls in barn hence no vending stall moves
        val moveList = moveOracle.allMoveTileFromBarnToEnclosure()
        assertEquals(2, moveList.size)
    }

    /**
     * test case for when moving vendingStall tile from barn to one or more enclosures possible
     */
    @Test
    fun vendingStallFromBarnToEnclosures(){
        val player1 = zoolorettoGameState!!.players.peek()
        player1.coins = 3

        player1.barn.vendingStalls.add(vendingStall)


        //no possible move here
        player1.playerEnclosure[0].vendingStalls.add(vendingStall)


        //one possible move from barn to enclosure1 available
        player1.playerEnclosure[1].vendingStalls.add(vendingStall)

        //no possible move here
        player1.playerEnclosure[2].vendingStalls.add((vendingStall))

        val moveOracle = MoveOracle(zoolorettoGameState!!)

        //fourth enclosure is empty, 1 moves available as well
        //animal tiles not found in barn so no moves
        val moveList = moveOracle.allMoveTileFromBarnToEnclosure()
        assertEquals(2, moveList.size)
    }





}
