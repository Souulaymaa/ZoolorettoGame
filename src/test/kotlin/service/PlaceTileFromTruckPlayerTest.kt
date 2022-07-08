package service

import entity.*
import org.junit.jupiter.api.Test
import kotlin.test.*

/**
 * Test class for the method [placeTileFromTruck]
 */
class PlaceTileFromTruckPlayerTest {

        private val rootService = RootService()

        //initialise players
        private val playerOne = Player("Beshr", Difficulty.HUMAN)
        private val playerTwo = Player("Basheer", Difficulty.HUMAN)
        private val playerThree = Player("Bashooor", Difficulty.HUMAN)
        val players: List<Player> = listOf( playerOne, playerTwo,playerThree)


        // initialise tiles
        val tile1 = VendingStall(StallType.VENDING1)
        val tile2 = Animal(Type.FEMALE, Species.E, false)


        //initialise Delivery Trucks
        var delivery1 = DeliveryTruck(3)
        var delivery2 = DeliveryTruck(3)

        /**
         * test if the playerActionService method placeTileFromTruck works
         */
        @Test
        fun placeTileFromTruckTest(){

            rootService.zoolorettoGameService.createZoolorettoGame(players,false)
            val game = rootService.zoolorettoGame!!.currentGameState


            delivery1.tilesOnTruck = arrayListOf(tile1)
            delivery2.tilesOnTruck = arrayListOf(tile2)

            val player1 = game.players.peek()
            player1.chosenTruck = delivery1
            player1.passed = true
            rootService.playerActionService.placeTileFromTruck()
            assertEquals(tile1, player1.barn.vendingStalls.last())
            assertEquals(0, player1.chosenTruck!!.tilesOnTruck.size)
            assertEquals( playerTwo, game.players.peek())

            val player2 = game.players.peek()
            player2.chosenTruck = delivery2
            player2.passed = true
            rootService.playerActionService.placeTileFromTruck()
            assertEquals(tile2, player2.barn.animalTiles.last())
            assertEquals(0, player1.chosenTruck!!.tilesOnTruck.size)
            assertEquals(playerThree, game.players.peek())
        }

}