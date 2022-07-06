package service

import entity.*
import gamemockup.twoplayers.TileStackForTwoPlayers
import gamemockup.util.DeliveryTrucks
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import java.util.LinkedList
import java.util.Queue
import kotlin.test.assertEquals

/**
 * tests für endGame
 *
 */
class EndGameTest {

    @Test
    fun testEndGame() {

        val player1 = Player("sanad", Difficulty.HUMAN)
        val player2 = Player("saskia", Difficulty.HUMAN)
        val twoTestPlayer: Queue<Player> = LinkedList(
            listOf(
                player1, player2
            )
        )
        val twoPlayerZoolorettoGameState = ZoolorettoGameState(
            false,
            false,
            twoTestPlayer,
            TileStackForTwoPlayers.tileStack,
            DeliveryTrucks.deliveryTrucksForTwoPlayers
        )

        val rootService = RootService()
        val zooGame = ZoolorettoGame(1f, twoPlayerZoolorettoGameState)
        rootService.zoolorettoGame = zooGame

        val p1 = rootService.zoolorettoGame!!.currentGameState.players.poll()
        val p2 = rootService.zoolorettoGame!!.currentGameState.players.poll()
        //player1 enclosures are initialized here
        p1.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5), false))
        p1.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4), false))
        p1.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6), false))
        p1.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5), false))

        //add one tile to player 0
        p1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[0])

        //player2 enclosures are initialized here
        p2.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5), false))
        p2.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4), false))
        p2.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6), false))
        p2.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5), false))

        //barn
        p2.barn.animalTiles.add(TileLists.zebras[5])
        p2.barn.animalTiles.add(TileLists.camels[0])
        p2.barn.animalTiles.add(TileLists.camels[1])

        rootService.zoolorettoGame!!.currentGameState.players.add(p1)
        rootService.zoolorettoGame!!.currentGameState.players.add(p2)

        rootService.zoolorettoGameService.endGame()

        assertEquals(0, player1.score)
        assertEquals(-4, player2.score)

    }
}