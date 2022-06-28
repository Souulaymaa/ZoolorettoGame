package entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * test class for VendingStall
 */
internal class VendingStallTest{



    /**
     * checks functionality of constructor
     */
    @Test
    fun checkObject(){
        val testCase1 = VendingStall(StallType.VENDING1)
        val testCase2 = VendingStall(StallType.VENDING2)
        assertEquals(testCase1.stall, StallType.VENDING1)
        assertNotEquals(null, testCase2)
    }
}