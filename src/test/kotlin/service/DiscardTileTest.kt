package service

import entity.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

/**
 * test class to test discard tile from barn
 */
class DiscardTileTest {
    private val rootService = RootService()

    private val player1 = Player("Beshr", Difficulty.HUMAN)
    private val player2 = Player("Basheer", Difficulty.HUMAN)
    private val playerList = listOf(player1, player2)

    /**
     * a test method for testing discard tile move. When a player discard one tile from
     * his barn out of the game. It is a money action and required 2 coins.
     */
    @Test
    fun testDiscardTile() {
        rootService.zoolorettoGameService.createZoolorettoGame(playerList, true)
        val game = rootService.zoolorettoGame!!.currentGameState

        //the first player has an animal tile in his barn
        val player = game.players.peek()
        val tile = Animal(Type.NONE, Species.S, false)
        player.barn.animalTiles.add(tile)

        //assert if the player has 2 coins and after discarding 0 coins
        assertEquals(player.coins, 2)
        rootService.playerActionService.discardTile(tile)
        assertEquals(player.coins, 0)
        assertFalse(player.barn.animalTiles.contains(tile))

        //the second player has a vending stall in his barn
        val player2 = game.players.peek()
        val vendingStall = VendingStall(StallType.VENDING1)
        player2.barn.vendingStalls.add(vendingStall)

        assertEquals(player2.coins, 2)
        rootService.playerActionService.discardTile(vendingStall)
        assertEquals(player2.coins, 0)
        assertFalse(player2.barn.vendingStalls.contains(vendingStall))

        //first player has now 0 coins and can't discard a tile
        player.barn.animalTiles.add(tile)
        assertThrows<IllegalArgumentException> { rootService.playerActionService.discardTile(tile) }

        //we give the first player 2 coins, but we put him passed
        player.coins = 2
        player.passed = true
        assertThrows<IllegalArgumentException> { rootService.playerActionService.discardTile(tile) }
    }
}