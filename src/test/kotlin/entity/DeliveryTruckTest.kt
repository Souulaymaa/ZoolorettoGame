package entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

/**
 * DeliveryTruck Test class
 */
internal class DeliveryTruckTest{

    private val successTruck = DeliveryTruck()
    private val stall = VendingStall(StallType.VENDING1)
    private val animal1 = Animal(Type.FEMALE, Species.S)
    private val animal2 = Animal(Type.FEMALE, Species.S)

    /**
     * function to test constructor initialization
     * and if tiles are added to tilesOnTruck arraylist properly
     */
    @Test
    fun checkObject(){
        assertEquals(successTruck.maxSize, 3)
        successTruck.tilesOnTruck.add(stall)
        successTruck.tilesOnTruck.add(animal1)
        successTruck.tilesOnTruck.add(animal2)
        assertEquals(3, successTruck.tilesOnTruck.size)
    }

}