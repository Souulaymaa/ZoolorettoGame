package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class InitializeTilesTest {

    private val p1 = Player("p1", Difficulty.HUMAN)
    private val p2 = Player("p2", Difficulty.HUMAN)
    private val activePlayer: List<Player> = listOf(p1, p2)


    /**
     * Test cases for initialiseTiles and initialiseTileStack
     */
    @Test
    fun initializeTileStackTest(){
        val rootService = RootService()
        val zoolorettoGameService = rootService.zoolorettoGameService
        zoolorettoGameService.createZoolorettoGame(activePlayer, false)
        val game = rootService.zoolorettoGame!!.currentGameState

        //nur Ausgabe der Tiles
        //println(game.tileStack.drawStack)

        val speciesList = listOf(Species.Z, Species.S, Species.U, Species.P, Species.L, Species.K, Species.F, Species.E)
        for(t in game.tileStack.drawStack){
            for(s in speciesList){
                if(t.equals(Animal(type = Type.OFFSPRING, species = s, hasChild = false))){
                    throw java.lang.IllegalArgumentException("Type = Offspring!")
                }
            }
        }

        assertEquals(2, game.players.size)
        assertEquals(72, game.tileStack.drawStack.size)
        assertEquals(15, game.tileStack.endStack.size)

    }

}

