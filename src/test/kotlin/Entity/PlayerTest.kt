package Entity

import entity.Difficulty
import entity.Enclosure
import entity.Player
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test cases for [Player]
 */
class PlayerTest {

    //some values to test the player
    private val enclosure = Enclosure(5, 1, 2, Pair(8, 5), false)
    private val enclosures = mutableListOf<Enclosure>()
    private val barn = Enclosure(104, 12, 0, Pair(0, 0), false)


    /**
     * Check if player has his initial values
     */
    @Test
    fun testPlayer() {
        enclosures.add(enclosure)
        enclosures.add(enclosure)
        enclosures.add(enclosure)
        val player = Player("Peter", Difficulty.EASY, enclosures, barn)
        assertFalse(player.passed)
        assertEquals(2, player.coins)
        assertEquals(null, player.chosenTruck)
        assertEquals(0, player.score)
        assertTrue(player.playerName == "Peter"
                && player.botSkillLevel == Difficulty.EASY
                && player.playerEnclosure == enclosures
                && player.barn == barn)
    }
}