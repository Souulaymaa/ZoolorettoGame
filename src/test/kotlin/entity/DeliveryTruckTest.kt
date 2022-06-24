package entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

/**
 * DeliveryTruck Test class
 */
internal class DeliveryTruckTest{

    private val failedTruck = DeliveryTruck()
    private val stall = VendingStall(StallType.VENDING1)
    private val animal1 = Animal(Type.FEMALE, Species.S)
    private val animal2 = Animal(Type.FEMALE, Species.S)

    /**
     * function to test constructor functionality
     * and if tiles are added to tilesOnTruck arraylist properly
     */
    @Test
    fun checkObject(){
        assertEquals(failedTruck.size, 3)
        failedTruck.tilesOnTruck.add(stall)
        failedTruck.tilesOnTruck.add(animal1)
        failedTruck.tilesOnTruck.add(animal2)
        assertEquals(3, failedTruck.tilesOnTruck.size)
    }

}