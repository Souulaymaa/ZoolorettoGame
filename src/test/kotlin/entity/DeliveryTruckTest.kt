package entity

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import kotlin.test.assertFails

internal class DeliveryTruckTest{

    private val failedTruck = DeliveryTruck()
    private val stall = VendingStall(StallType.VENDING1)
    private val animal1 = Animal(Type.FEMALE, Species.S)
    private val animal2 = Animal(Type.FEMALE, Species.S)
    private val coin = Coin()

    /**
     currently not possible:

    fun testException(){
    assertFails {
    failedTruck.tilesOnTruck.add(stall)
    failedTruck.tilesOnTruck.add(animal1)
    failedTruck.tilesOnTruck.add(animal2)
    failedTruck.tilesOnTruck.add(coin) }
    }
     */


    @Test
    fun checkObject(){
        assertEquals(failedTruck.size, 3)
    }

}