package entity

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * test class for [Difficulty]
 */
class DifficultyTest {
    val easy = Difficulty.EASY
    val medium = Difficulty.MEDIUM
    val hard = Difficulty.HARD
    val human = Difficulty.HUMAN

    /**
     * test the [Difficulty.toString]
     */
    @Test
    fun testToString(){
        assertEquals("E", easy.toString())
        assertEquals("M", medium.toString())
        assertEquals("H", hard.toString())
        assertEquals("HM", human.toString())
    }
}