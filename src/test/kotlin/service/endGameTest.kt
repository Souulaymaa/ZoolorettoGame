package service

import entity.*
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.twoplayers.TileStackForTwoPlayers
import gamemockup.util.DeliveryTrucks
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import java.util.LinkedList
import java.util.Queue
import kotlin.test.assertEquals
import kotlin.test.assertFails

/**
 * tests für endGame
 * gleiche Tests wie in determineWinner und determineScore
 */
class EndGameTest {

    @Test
    fun testEndGame(){

        val gameInstance = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
        val rootService = RootService()
        val player1 = gameInstance.players.poll()
        val player2 = gameInstance.players.poll()

        //player1 enclosures are initialized here
        player1.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5), false))
        player1.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4), false))
        player1.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6), false))
        player1.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5), false))

        //add one tile to player 0
        player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[0])

        //player2 enclosures are initialized here
        player2.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player2.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4),false))
        player2.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6),false))
        player2.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5),false))

        //barn
        player2.barn.animalTiles.add(TileLists.zebras[5])
        player2.barn.animalTiles.add(TileLists.camels[0])
        player2.barn.animalTiles.add(TileLists.camels[1])

        gameInstance.players.add(player1)
        gameInstance.players.add(player2)
        rootService.zoolorettoGame = ZoolorettoGame(1f, gameInstance)
        val zooGameService = rootService.zoolorettoGameService
        zooGameService.endGame()

        assertEquals(0, player1.score)
        assertEquals(-4, player2.score)

        gameInstance.players.remove()
        assertFails { zooGameService.endGame() }
    }
}