package service

import entity.Difficulty
import entity.Enclosure
import entity.Player
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.PlayerQueues.Companion.player2
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.lang.Double.POSITIVE_INFINITY
import kotlin.test.assertFails

internal class ScoreServiceTest {

    private val gameInstance = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameState
    var rootService = RootService()
    val player1 = gameInstance.players.poll()
    val player2 = gameInstance.players.poll()
    /**
     * test if determineScore function calculates the score of each player correctly.
     */
    @Test
    fun determineScoreOfFirstPlayer() {

        val scoreService = rootService.scoreService

        if(player1 != null){
            println("kaka")
        }
        //player1 enclosures are initialized here
        player1.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5), false))
        player1.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4), false))
        player1.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6), false))
        player1.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5), false))

        //first enclosure is filled with flamingos and the one vending stall space it has --> score must be equal to 10.
        //first enclosure gets score of 8 and player has also one vending stall type + 2.

        for(i in 0..4){
            player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[i])
        }
        player1.playerEnclosure[0].vendingStalls.add(TileLists.vendingStalls[0])

        assertEquals(10, scoreService.determineScore(player1))

        //cleared all tiles from player1 enclosures
        player1.playerEnclosure[0].animalTiles.clear()
        player1.playerEnclosure[0].vendingStalls.clear()

        //3 types of animals and 1 vendingstall in barn score must be = -8
        player1.barn.animalTiles.add(TileLists.zebras[5])
        player1.barn.animalTiles.add(TileLists.camels[0])
        player1.barn.animalTiles.add(TileLists.camels[1])
        player1.barn.animalTiles.add(TileLists.flamingos[1])
        player1.barn.vendingStalls.add((TileLists.vendingStalls[0]))

        //score must be equal to -8
        assertEquals(-8, scoreService.determineScore(player1))
    }

    @Test
    fun determineScoreOfSecondPlayer(){

        val scoreService = rootService.scoreService

        //player2 enclosures are initialized here
        player2.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player2.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4),false))
        player2.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6),false))
        player2.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5),false))


        //second enclosure of second player has only 2 kangaroos but full vending stall spaces therefor
        //enclosure gets score of 2(one point for each animal) however, player has two vending stall from
        //same type and therefor only 2 extra
        //total score must be equal to 4
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[0])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[0])

        assertEquals(4, scoreService.determineScore(player2))/////

        //Clear the player's 2nd enclosure completely
        player2.playerEnclosure[1].animalTiles.clear()
        player2.playerEnclosure[1].vendingStalls.clear()

        player2.playerEnclosure[1].animalTiles.clear()
        player2.playerEnclosure[1].vendingStalls.clear()

        //first Enclosure
        for(i in 0..3){
            player2.playerEnclosure[0].animalTiles.add(TileLists.elephants[i])
        }

        //second Enclosure
        player2.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[4])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[1])

        //third Enclosure
        for (i in 0..3){
            player2.playerEnclosure[2].animalTiles.add(TileLists.pandas[i])
        }

        //expansion
        for(i in 0..4){
            player2.playerEnclosure[3].animalTiles.add(TileLists.zebras[i])
        }
        player2.playerEnclosure[3].vendingStalls.add(TileLists.vendingStalls[1])

        //barn
        player2.barn.animalTiles.add(TileLists.zebras[5])
        player2.barn.animalTiles.add(TileLists.camels[0])
        player2.barn.animalTiles.add(TileLists.camels[1])

        //score must be equal to 16. for the details please check the example in Zooloretto PDF.
        assertEquals(16, scoreService.determineScore(player2))
    }

    /**
     * class to test if determine highscore calculates all scores of each player correctly and if it returns sorted map according to map.value.
     */
    @Test
    fun determineHighscore() {
        val scoreService = rootService.scoreService

        val failedPlayerList = mutableListOf<Player>()
        val successPlayerList = mutableListOf<Player>()
        val player1 = Player("one", Difficulty.HUMAN)
        val player2 = Player("two", Difficulty.HUMAN)
        val player3 = Player("three", Difficulty.HUMAN)
        val player4 = Player("four", Difficulty.HUMAN)
        val player5 = Player("four", Difficulty.HUMAN)
        val player6 = Player("four", Difficulty.HUMAN)

        failedPlayerList.add(player1)
        failedPlayerList.add(player2)
        failedPlayerList.add(player3)
        failedPlayerList.add(player4)
        failedPlayerList.add(player5)
        failedPlayerList.add(player6)

        assertFails { scoreService.determineHighscore(failedPlayerList) }

        //player1 enclosures are initialized here
        player1.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player1.playerEnclosure.add(Enclosure(POSITIVE_INFINITY.toInt(), POSITIVE_INFINITY.toInt(), 0, Pair(0, 0),true))

        //first enclosure is filled with flamingos and the one vending stall space it has --> score must be equal to 10.
        //first enclosure gets score of 8 and player has also one vending stall type + 2.
        for (i in 0..4){
            player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos[i])
        }
        player1.playerEnclosure[0].vendingStalls.add(TileLists.vendingStalls[0])

        //player2 enclosures are initialized here
        player2.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player2.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4),false))
        player2.playerEnclosure.add(Enclosure(POSITIVE_INFINITY.toInt(), POSITIVE_INFINITY.toInt(), 0, Pair(0, 0),true))

        //second enclosure of second player has only 2 kangaroos but full vending stall spaces therefor
        //enclosure gets score of 2(one point for each animal) however, player has two vending stall from
        //same type and therefor only 2 extra
        //total score must be equal to 4
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[0])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls[0])

        successPlayerList.add(player1)
        successPlayerList.add(player2)
        val map = scoreService.determineHighscore(successPlayerList)
        val firstEntry = map.entries.first()


        assertEquals(4, firstEntry.value)


    }
}