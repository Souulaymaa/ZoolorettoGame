package service

import entity.*
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.twoplayers.TileStackForTwoPlayers
import gamemockup.util.DeliveryTrucks
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import java.util.*

internal class ScoreServiceTest {

    /**
     * test if determineScore function calculates the score of each player correctly.
     */
    @Test
    fun determineScoreOfPlayer() {
        val gameInstance = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val zooGame = ZoolorettoGame(1f, gameInstance)
        val rootService = RootService()
        val player1 = gameInstance.players.poll()
        val player2 = gameInstance.players.poll()

        val scoreService = rootService.scoreService

        //player1 enclosures are initialized here
        player1.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5), false))
        player1.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4), false))
        player1.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6), false))
        player1.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5), false))

        //first enclosure is filled with flamingos and the one vending stall space it has --> score must be equal to 10.
        //first enclosure gets score of 8 and player has also one vending stall type + 2.

        for(i in 0..4){
            player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos()[i])
        }
        player1.playerEnclosure[0].vendingStalls.add(TileLists.vendingStalls()[0])

        assertEquals(10, scoreService.determineScore(player1))

        //cleared all tiles from player1 enclosures
        player1.playerEnclosure[0].animalTiles.clear()
        player1.playerEnclosure[0].vendingStalls.clear()

        //3 types of animals and 1 vendingstall in barn score must be = -8
        player1.barn.animalTiles.add(TileLists.zebras()[5])
        player1.barn.animalTiles.add(TileLists.camels()[0])
        player1.barn.animalTiles.add(TileLists.camels()[1])
        player1.barn.animalTiles.add(TileLists.flamingos()[1])
        player1.barn.vendingStalls.add((TileLists.vendingStalls()[0]))

        //score must be equal to -8
        assertEquals(-8, scoreService.determineScore(player1))

        /////////////////////////////////////////////////////////////////////////////////////////////////////
        //player2 enclosures are initialized here
        player2.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player2.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4),false))
        player2.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10, 6),false))
        player2.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5),false))


        //second enclosure of second player has only 2 kangaroos but full vending stall spaces therefor
        //enclosure gets score of 2(one point for each animal) however, player has two vending stall from
        //same type and therefor only 2 extra
        //total score must be equal to 4
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos()[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos()[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls()[0])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls()[0])

        assertEquals(4, scoreService.determineScore(player2))/////
        assertEquals(4, player2.score)

        //Clear the player's 2nd enclosure completely
        player2.playerEnclosure[1].animalTiles.clear()
        player2.playerEnclosure[1].vendingStalls.clear()

        player2.playerEnclosure[1].animalTiles.clear()
        player2.playerEnclosure[1].vendingStalls.clear()

        //first Enclosure
        for(i in 0..3){
            player2.playerEnclosure[0].animalTiles.add(TileLists.elephants()[i])
        }

        //second Enclosure
        player2.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees()[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees()[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls()[4])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls()[1])

        //third Enclosure
        for (i in 0..3){
            player2.playerEnclosure[2].animalTiles.add(TileLists.pandas()[i])
        }

        //expansion
        for(i in 0..4){
            player2.playerEnclosure[3].animalTiles.add(TileLists.zebras()[i])
        }
        player2.playerEnclosure[3].vendingStalls.add(TileLists.vendingStalls()[1])

        //barn
        player2.barn.animalTiles.add(TileLists.zebras()[5])
        player2.barn.animalTiles.add(TileLists.camels()[0])
        player2.barn.animalTiles.add(TileLists.camels()[1])

        //score must be equal to 16. for the details please check the example in Zooloretto PDF.
        assertEquals(16, scoreService.determineScore(player2))
    }

    /**
     * class to test if determineWinner() returns the correct winner of a game even if there is a tie
     */
    @Test
    fun determineWinner() {
        val player1 = Player("sanad", Difficulty.HUMAN)
        val player2 = Player("mathias", Difficulty.HUMAN)
        val twoTestPlayer : Queue<Player> = LinkedList(listOf(
            player1, player2
        ))
        val twoPlayerZoolorettoGameState = ZoolorettoGameState(
            false,
            false,
            twoTestPlayer,
            TileStackForTwoPlayers.getTileStack(),
            DeliveryTrucks.deliveryTrucksForTwoPlayers()
        )

        val rootService = RootService()
        val gameInstance = twoPlayerZoolorettoGameState
        val zooGame = ZoolorettoGame(1f, gameInstance)
        rootService.zoolorettoGame = zooGame

        val scoreService = rootService.scoreService



        //player1 enclosures are initialized here
        player1.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))

        //first enclosure is filled with flamingos and the one vending stall space it has --> score must be equal to 10.
        //first enclosure gets score of 8 and player has also one vending stall type + 2.
        for (i in 0..4){
            player1.playerEnclosure[0].animalTiles.add(TileLists.flamingos()[i])
        }
        player1.playerEnclosure[0].vendingStalls.add(TileLists.vendingStalls()[0])

        //player2 enclosures are initialized here
        player2.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8, 5),false))
        player2.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5, 4),false))

        //second enclosure of second player has only 2 kangaroos but full vending stall spaces therefor
        //enclosure gets score of 2(one point for each animal) however, player has two vending stall from
        //same type and therefor only 2 extra
        //total score must be equal to 4
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos()[0])
        player2.playerEnclosure[1].animalTiles.add(TileLists.kangaroos()[1])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls()[0])
        player2.playerEnclosure[1].vendingStalls.add(TileLists.vendingStalls()[0])

        checkNotNull(rootService.zoolorettoGame)

        scoreService.determineScore(player1)
        scoreService.determineScore(player2)

        assertEquals(rootService.zoolorettoGame!!.currentGameState.players.size, 2)
        assertEquals(player1.playerName, scoreService.determineWinner()!!.playerName)


        //test of draw cases begins here!
        player1.coins = 4
        player2.coins = 4

        player2.playerEnclosure[1].animalTiles.clear()
        player2.playerEnclosure[1].vendingStalls.clear()

        for (i in 0..4){
            player2.playerEnclosure[0].animalTiles.add(TileLists.flamingos()[i])
        }
        player2.playerEnclosure[0].vendingStalls.add(TileLists.vendingStalls()[0])
        scoreService.determineScore(player1)
        scoreService.determineScore(player2)
        assertEquals(null, scoreService.determineWinner())

        player1.coins ++
        assertEquals(player1, scoreService.determineWinner())



    }
}