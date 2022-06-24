package entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * test class for VendingStall
 */
internal class VendingStallTest{

    private val testCase1 = VendingStall(StallType.VENDING1)
    private val testCase2 = VendingStall(StallType.VENDING2)

    /**
     * checks functionality of constructor
     */
    @Test
    fun checkObject(){
        assertEquals(testCase1.stall, StallType.VENDING1)
        assertNotEquals(null, testCase2)
    }
}