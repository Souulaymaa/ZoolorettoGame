package gameai.moveoracle

import gameai.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Class to test the [MoveOracle.determineExchangeAllTileMoves] function.
 */
class DetermineExchangeAllTileMovesTest {

    private val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
    private val moveOracle = MoveOracle(zoolorettoGameState)

    private val flamingo = TileLists.flamingos().first()
    private val camel = TileLists.camels().first()
    private val chimpanzee = TileLists.chimpanzees().first()
    private val kangaroo = TileLists.kangaroos().first()
    private val panda = TileLists.pandas().first()

    /**
     * Checks the functionality of ExchangeAllTilesMoves
     */
    @Test
    fun testExchangeAllTiles(){
        val player = moveOracle.currentGameStateCopy.players.peek()
        repeat(3){
            player.playerEnclosure[0].animalTiles.add(flamingo)
            player.playerEnclosure[1].animalTiles.add(camel)
            player.playerEnclosure[2].animalTiles.add(chimpanzee)
            player.playerEnclosure[3].animalTiles.add(panda)
            player.barn.animalTiles.add(kangaroo)
            player.barn.animalTiles.add(camel)
        }
        player.coins = 0
        var moveList = moveOracle.determineExchangeAllTileMoves()
        assertEquals(0, moveList.size)

        player.coins = 1
        moveList = moveOracle.determineExchangeAllTileMoves()
        assertEquals(13, moveList.size)

    }

    /**
     * Checks the functionality of exchangeAllTilesMoves with empty enclosures.
     */
    @Test
    fun testExchangeAllTilesWithEmptyEnclosures(){
        val player = moveOracle.currentGameStateCopy.players.peek()
        repeat(3){
            player.barn.animalTiles.add(kangaroo)
            player.barn.animalTiles.add(camel)
        }
        var moveList = moveOracle.determineExchangeAllTileMoves()
        assertEquals(0, moveList.size)

        player.playerEnclosure[0].animalTiles.add(flamingo)
        moveList = moveOracle.determineExchangeAllTileMoves()
        assertEquals(2, moveList.size)

    }

}