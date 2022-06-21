package entity

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test class for [StallType]
 */
class StallTypeTest {

    //Some Tiles for the VendingStalls to test with
    val tile1 = VendingStall(StallType.VENDING1)
    val tile2 = VendingStall(StallType.VENDING2)
    val tile3 = VendingStall(StallType.VENDING3)
    val tile4 = VendingStall(StallType.VENDING4)

    /**
     * Tests, if the [StallType.toString] gives out the correct names of the [StallType]s
     */
    @Test
    fun testToString(){
        assertEquals("V1", tile1.toString())
        assertEquals("V2", tile2.toString())
        assertEquals("V3", tile3.toString())
        assertEquals("V4", tile4.toString())
    }
}