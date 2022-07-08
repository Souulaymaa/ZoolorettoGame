package service

import entity.Difficulty
import entity.Player
import entity.StallType
import entity.VendingStall
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * test class to test move vending stall from enclosure to enclosure.
 */
class MoveVendingStallTest {
    private val rootService = RootService()

    private val player1 = Player("Beshr", Difficulty.HUMAN)
    private val player2 = Player("Basheer", Difficulty.HUMAN)
    private val playerList = listOf(player1, player2)

    /**
     * test method to test move vending stall from enclosure to enclosure.
     */
    @Test
    fun testMoveVendingStall() {
        rootService.zoolorettoGameService.createZoolorettoGame(playerList, true)
        val game = rootService.zoolorettoGame!!.currentGameState

        val player = game.players.peek()
        val vendingStall1 = VendingStall(StallType.VENDING1)
        val vendingStall2 = VendingStall(StallType.VENDING2)
        //the first enclosure has one vending stall
        player.playerEnclosure[0].vendingStalls.add(vendingStall1)

        //in this case the player has enough coins, but he passed.
        assertEquals(player.coins, 2)
        player.passed = true
        assertThrows<IllegalArgumentException> { rootService.playerActionService.
        moveVendingStall(player.playerEnclosure[0], player.playerEnclosure[1]) }

        //in this case the player has no coins.
        player.passed = false
        player.coins = 0
        assertThrows<IllegalArgumentException> { rootService.playerActionService.
        moveVendingStall(player.playerEnclosure[0], player.playerEnclosure[1]) }

        //in this case the player has enough coins and didn't pass, but the second enclosure is full
        player.coins = 2
        player.playerEnclosure[1].vendingStalls.add(vendingStall1)
        player.playerEnclosure[1].vendingStalls.add(vendingStall2)
        assertThrows<IllegalArgumentException> { rootService.playerActionService.
        moveVendingStall(player.playerEnclosure[0], player.playerEnclosure[1]) }

        //in this case the second enclosure is not full, but the first is empty
        player.playerEnclosure[1].vendingStalls.removeFirst()
        player.playerEnclosure[0].vendingStalls.clear()
        assertThrows<IllegalArgumentException> { rootService.playerActionService.
        moveVendingStall(player.playerEnclosure[0], player.playerEnclosure[1]) }

        //this is the case, when everything is all right
        player.playerEnclosure[0].vendingStalls.add(vendingStall1)
        rootService.playerActionService.moveVendingStall(player.playerEnclosure[0], player.playerEnclosure[1])
        assertFalse(player.playerEnclosure[0].vendingStalls.contains(vendingStall1))
        assertTrue { player.playerEnclosure[1].vendingStalls.contains(vendingStall1) }
        assertEquals(player.coins, 1)
    }
}