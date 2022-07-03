package service

import entity.Enclosure
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Double.POSITIVE_INFINITY

internal class ScoreServiceTest {

    private val gameInstance = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
    var rootService = RootService()
    //var scoreService : ScoreService(rootService)


    @Test
    fun determineScore() {
        val player1 = gameInstance.players.poll()
        val player2 = gameInstance.players.poll()
        gameInstance.players.add(player1)
        gameInstance.players.add(player2)
        val scoreService = rootService.scoreService

        //player1 enclosures are initialized here
        player1.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player1.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4),false))
        player1.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6),false))
        player1.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5),false))
        player1.playerEnclosure.add(Enclosure(POSITIVE_INFINITY.toInt(), POSITIVE_INFINITY.toInt(), 0, Pair(0, 0),true))

        //first enclosure is filled with flamingos and the one vending stall space it has --> score must be equal to 10.
        //first enclosure gets score of 8 and player has also one vending stall type + 2.
        player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[0])
        player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[1])
        player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[2])
        player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[3])
        player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[4])
        player1.playerEnclosure[0].vendingStalls.add(TileLists.vendingStalls[0])

        assertEquals(10, scoreService.determineScore(player1))

        //player2 enclosures are initialized here
        player2.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player2.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4),false))
        player2.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6),false))
        player2.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5),false))
        player2.playerEnclosure.add(Enclosure(POSITIVE_INFINITY.toInt(), POSITIVE_INFINITY.toInt(), 0, Pair(0, 0),true))



        //second enclosure of second player has only 2 kangaroos but full vending stall spaces therefor
        //enclosure gets score of 2(one point for each animal) however, player has two vending stall from
        //same type and therefor only 2 extra
        //total score must be equal to 4
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[0])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[0])

        assertEquals(4, scoreService.determineScore(player2))/////

        //first Enclosure
        player2.playerEnclosure[0].animalTiles.add(TileLists.elephants[0])
        player2.playerEnclosure[0].animalTiles.add(TileLists.elephants[1])
        player2.playerEnclosure[0].animalTiles.add(TileLists.elephants[2])
        player2.playerEnclosure[0].animalTiles.add(TileLists.elephants[3])

        //second Enclosure
        player2.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[0])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[1])

        //third Enclosure
        player2.playerEnclosure[2].animalTiles.add(TileLists.pandas[0])
        player2.playerEnclosure[2].animalTiles.add(TileLists.pandas[1])
        player2.playerEnclosure[2].animalTiles.add(TileLists.pandas[2])
        player2.playerEnclosure[2].animalTiles.add(TileLists.pandas[3])

        //expansion Enclosure
        player2.playerEnclosure[3].animalTiles.add(TileLists.zebras[0])
        player2.playerEnclosure[3].animalTiles.add(TileLists.zebras[1])
        player2.playerEnclosure[3].animalTiles.add(TileLists.zebras[2])
        player2.playerEnclosure[3].animalTiles.add(TileLists.zebras[3])
        player2.playerEnclosure[3].animalTiles.add(TileLists.zebras[4])
        player2.playerEnclosure[3].vendingStalls.add(TileLists.vendingStalls[1])

        //barn
        player2.playerEnclosure[4].animalTiles.add(TileLists.zebras[5])
        player2.playerEnclosure[4].animalTiles.add(TileLists.camels[0])
        player2.playerEnclosure[4].animalTiles.add(TileLists.camels[1])

        //score must be equal to 16. for the details please check the example in Zooloretto PDF.
        assertEquals(16, scoreService.determineScore(player2))


        //cleared all tiles from player1 enclosures
        player1.playerEnclosure[0].animalTiles.clear()
        player1.playerEnclosure[0].vendingStalls.clear()

        //3 types of animals and 1 vendingstall in barn score must be = -8
        player1.playerEnclosure[4].animalTiles.add(TileLists.zebras[5])
        player1.playerEnclosure[4].animalTiles.add(TileLists.camels[0])
        player1.playerEnclosure[4].animalTiles.add(TileLists.camels[1])
        player1.playerEnclosure[4].animalTiles.add(TileLists.flamingos[1])
        player1.playerEnclosure[4].vendingStalls.add(TileLists.vendingStalls[0])

        //score must be equal to -8
        assertEquals(-8, scoreService.determineScore(player1))

    }

    @Test
    fun determineHighscore() {
    }
}