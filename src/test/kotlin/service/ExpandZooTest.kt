package service

import entity.*
import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class ExpandZooTest {

    private val rootService = RootService()

    private val player1 = Player("Beshr", Difficulty.HUMAN)
    private val player2 = Player("Basheer", Difficulty.HUMAN)
    private val player3 = Player("Bashooor", Difficulty.HUMAN)
    private val playerList1 = listOf(player1, player2)
    private val playerList2 = listOf(player1, player2, player3)

    @Test
    fun testExpandZooWithTwoPlayers() {
        rootService.zoolorettoGameService.createZoolorettoGame(playerList1,false)
        val game = rootService.zoolorettoGame!!.currentGameState

        val player1 = game.players.peek()
        player1.coins = 9
        //now first player has 9 coins and can expand his zoo.
        assertEquals(player1.playerEnclosure.size, 3)
        rootService.playerActionService.expandZoo()
        assertEquals(player1.playerEnclosure.size, 4)
        //lost 3 coins for expanding.
        assertEquals(player1.coins, 6)

        val player2 = game.players.peek()
        player2.coins = 3
        //the second player has 3 coins and can expand his zoo.
        assertEquals(player2.playerEnclosure.size, 3)
        rootService.playerActionService.expandZoo()
        assertEquals(player2.playerEnclosure.size, 4)
        assertEquals(player2.coins, 0)

        //now first player has 6 coins and can expand his zoo.
        rootService.playerActionService.expandZoo()
        assertEquals(player1.playerEnclosure.size, 5)

        //the second player doesn't have coins for expanding his zoo.
        assertThrows<IllegalArgumentException> { rootService.playerActionService.expandZoo() }

        //so that we can use expandZoo method for the first player.
        game.players.poll()
        game.players.add(player2)

        //the first player has 3 coins but can't expand anymore, because he has 5 enclosures.
        assertEquals(player1.coins, 3)
        assertThrows<IllegalArgumentException> { rootService.playerActionService.expandZoo() }
    }

    @Test
    fun testExpandZooWithThreePlayers() {
        rootService.zoolorettoGameService.createZoolorettoGame(playerList2,true)
        val game = rootService.zoolorettoGame!!.currentGameState

        val player1 = game.players.peek()
        //the first player doesn't have 3 coins to expand.
        assertEquals(player1.coins, 2)
        assertThrows<IllegalArgumentException> { rootService.playerActionService.expandZoo() }

        game.players.poll()
        game.players.add(player1)

        val player2 = game.players.peek()
        player2.coins = 6
        //the second player has 6 coins and can expand his zoo.
        assertEquals(player2.playerEnclosure.size, 3)
        rootService.playerActionService.expandZoo()
        assertEquals(player2.playerEnclosure.size, 4)
        assertEquals(player2.coins, 3)

        val player3 = game.players.peek()
        //the third and first player don't have 3 coins to expand.
        assertEquals(player3.coins, 2)
        assertThrows<IllegalArgumentException> { rootService.playerActionService.expandZoo() }

        game.players.poll()
        game.players.add(player3)

        assertThrows<IllegalArgumentException> { rootService.playerActionService.expandZoo() }

        game.players.poll()
        game.players.add(player1)

        //the second player has 3 coins but can't expand, cuz he had 4 enclosures.
        assertThrows<IllegalArgumentException> { rootService.playerActionService.expandZoo() }
    }
}