package gameai.moveoracle

import gameai.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class PurchaseTileTest {
    var moveOracle = MoveOracle(ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory())

//    @Test
//    fun purchaseThreeTiles(){
//        val currentGameStateCopy = moveOracle.currentGameStateCopy
//        //Get the players
//        val player1 = currentGameStateCopy.players.poll()
//        val player2 = currentGameStateCopy.players.poll()
//
//        //Add them back
//        currentGameStateCopy.players.add(player1)
//        currentGameStateCopy.players.add(player2)
//
//        //Player1 should have a zebra, chimpanzee, and kangaroo
//        player1.playerEnclosure[0].animalTiles.add(TileLists.zebras()[0])
//        player1.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees()[0])
//        player1.playerEnclosure[2].animalTiles.add(TileLists.kangaroos()[0])
//
//        //Player2 should have five zebras,  one flamingo, and one kangaroo
//        player2.barn.animalTiles.add(TileLists.zebras()[0])
//        player2.barn.animalTiles.add(TileLists.zebras()[0])
//        player2.barn.animalTiles.add(TileLists.zebras()[0])
//        player2.barn.animalTiles.add(TileLists.zebras()[0])
//        player2.barn.animalTiles.add(TileLists.zebras()[0])
//
//        player2.barn.animalTiles.add(TileLists.flamingos()[0])
//        player2.barn.animalTiles.add(TileLists.kangaroos()[0])
//
//        //Let give player1 enough money for purchasing
//        player1.coins = 4
//
//        var moves = moveOracle.purchaseTileMoves()
//
//        assertEquals(2, moves.size)
//
//        // Add a flamingo in player1's fourth enclosure and see if we can buy it from player 2 aswell
//        player1.playerEnclosure[3].animalTiles.add(TileLists.flamingos()[0])
//
//        moves = moveOracle.purchaseTileMoves()
//
//        assertEquals(3, moves.size)
//    }

    @Test
    fun noMoney(){
        val currentGameStateCopy = moveOracle.currentGameStateCopy
        //Get the players
        val player1 = currentGameStateCopy.players.poll()
        val player2 = currentGameStateCopy.players.poll()

        //Add them back
        currentGameStateCopy.players.add(player1)
        currentGameStateCopy.players.add(player2)

        //Player1 should have a zebra, chimpanzee, and kangaroo
        player1.playerEnclosure[0].animalTiles.add(TileLists.zebras()[0])
        player1.playerEnclosure[1].animalTiles.add(TileLists.chimpanzees()[0])
        player1.playerEnclosure[2].animalTiles.add(TileLists.kangaroos()[0])

        //Player2 should have five zebras,  one flamingo, and one kangaroo
        player2.barn.animalTiles.add(TileLists.zebras()[0])
        player2.barn.animalTiles.add(TileLists.zebras()[0])
        player2.barn.animalTiles.add(TileLists.zebras()[0])
        player2.barn.animalTiles.add(TileLists.zebras()[0])
        player2.barn.animalTiles.add(TileLists.zebras()[0])

        player2.barn.animalTiles.add(TileLists.flamingos()[0])
        player2.barn.animalTiles.add(TileLists.kangaroos()[0])

        //Let give player1 enough money for purchasing
        player1.coins = 0

        var moves = moveOracle.purchaseTileMoves()

        assertEquals(0, moves.size)

        // Add a flamingo in player1's fourth enclosure and see if we can buy it from player 2 aswell
        player1.playerEnclosure[3].animalTiles.add(TileLists.flamingos()[0])

        moves = moveOracle.purchaseTileMoves()

        assertEquals(0, moves.size)
    }
}