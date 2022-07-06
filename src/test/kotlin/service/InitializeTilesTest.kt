package service

import entity.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

/**
 * Class to test [initialiseTiles] and [initialiseTileStack]
 */

class InitializeTilesTest {

    private val p1 = Player("p1", Difficulty.HUMAN)
    private val p2 = Player("p2", Difficulty.HUMAN)
    private val p3 = Player("p3", Difficulty.HUMAN)
    private val p4 = Player("p4", Difficulty.HUMAN)
    private val p5 = Player("p5", Difficulty.HUMAN)
    private val activePlayers: List<Player> = listOf(p1, p2)
    private val activePlayer: List<Player> = listOf(p1, p2, p3, p4, p5)

    /**
     * Test cases for [initialiseTiles] and [initialiseTileStack] with 5 Players
     */
    @Test
    fun initializeTileStack5PlayersTest(){
        val rootService = RootService()
        val zoolorettoGameService = rootService.zoolorettoGameService
        println(zoolorettoGameService.coins.size)

        zoolorettoGameService.createZoolorettoGame(activePlayer, false)
        val game = rootService.zoolorettoGame!!.currentGameState

        var counter = 0
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.E))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.F))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.K))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.L))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.P))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.S))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.U))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.Z))){
                counter++
                break
            }
        }


        assertEquals(5, game.players.size)
        assertEquals(97, game.tileStack.drawStack.size)
        assertEquals(8, counter)
        assertEquals(15, game.tileStack.endStack.size)

    }

    /**
     * Test cases for [initialiseTiles] and [initialiseTileStack] with 2 Players
     */

    @Test
    fun initializeTileStack2PlayersTest(){
        val rootService = RootService()
        val zoolorettoGameService = rootService.zoolorettoGameService
        zoolorettoGameService.createZoolorettoGame(activePlayers, false)
        val game = rootService.zoolorettoGame!!.currentGameState

        //nur Ausgabe der Tiles
        println(game.tileStack.drawStack)

        val speciesList = listOf(Species.Z, Species.S, Species.U, Species.P, Species.L, Species.K, Species.F, Species.E)
        for(t in game.tileStack.drawStack){
            for(s in speciesList){
                if(t.equals(Animal(type = Type.OFFSPRING, species = s, hasChild = false))){
                    throw java.lang.IllegalArgumentException("Type = Offspring!")
                }
            }
        }

        var counter = 0
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.E))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.F))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.K))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.L))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.P))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.S))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.U))){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Animal(Type.NONE,Species.Z))){
                counter++
                break
            }
        }
        assertEquals(2, game.players.size)
        assertEquals(64, game.tileStack.drawStack.size)
        assertEquals(5, counter)
        assertEquals(15, game.tileStack.endStack.size)

    }
}