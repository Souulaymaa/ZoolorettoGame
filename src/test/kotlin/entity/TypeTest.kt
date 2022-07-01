package entity

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * testcases for [Type]
 */

class TypeTest {
    val male = Type.MALE
    val female = Type.FEMALE
    val none = Type.NONE
    val offspring = Type.OFFSPRING

    /**
     * Tests the [Type.toString] method
     */
    @Test
    fun testToString(){
        assertEquals("M", male.toString())
        assertEquals("F", female.toString())
        assertEquals("N", none.toString())
        assertEquals("O", offspring.toString())
    }
}