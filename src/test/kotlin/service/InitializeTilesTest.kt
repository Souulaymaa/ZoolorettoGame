package service

import entity.*
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.assertEquals

class InitializeTilesTest {

    private val p1 = Player("p1", Difficulty.HUMAN)
    private val p2 = Player("p2", Difficulty.HUMAN)
    private val p3 = Player("p3", Difficulty.HUMAN)
    private val activePlayers: Queue<Player> = LinkedList(listOf(p1, p2))
    private val activePlayer: List<Player> = listOf(p1, p2)
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private val deliveryTrucks = mutableListOf<DeliveryTruck>(
        DeliveryTruck(3),
        DeliveryTruck(3),
        DeliveryTruck(3))

    /**
     * Test cases for [initialiseTiles] and [initialiseTileStack]
     */
    @Test
    fun initializeTileStackTest(){
        val rootService = RootService()
        //println(game.currentGameState.players.size)
        val zoolorettoGameService = rootService.zoolorettoGameService
        //println(zoolorettoGameService.players.size)
        println(zoolorettoGameService.coins.size)
        zoolorettoGameService.createZoolorettoGame(activePlayer, false)
        println(zoolorettoGameService.coins.size)
        val game = rootService.zoolorettoGame!!.currentGameState

        //println(game.players.size)

        var counter = 0
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.E)){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.F)){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.K)){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.L)){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.P)){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.S)){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.U)){
                counter++
                break
            }
        }
        for(t in game.tileStack.drawStack){
            if(t.equals(Species.Z)){
                counter++
                break
            }
        }
        assertEquals(2, game.players.size)
        assertEquals(88, game.tileStack.drawStack.size)
        assertEquals(6, counter)
        assertEquals(15, game.tileStack.endStack.size)

    }
}