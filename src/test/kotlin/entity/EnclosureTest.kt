package entity

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

/**
 * Test class for [Enclosure]
 */
class EnclosureTest {

    val enclosure1 = Enclosure(6, 2, 0, Pair(1,1), false)
    val animal1 = Animal(Type.MALE, Species.S)
    val animal2 = Animal(Type.MALE, Species.F)
    val animal3 = Animal(Type.FEMALE, Species.K)
    val v1 = VendingStall(StallType.VENDING1)
    val v2 = VendingStall(StallType.VENDING2)
    val v3 = VendingStall(StallType.VENDING1)

    /**
     * checking if getting and setting the animal tiles work
     */
    @Test
    fun testAnimalTiles(){
        enclosure1.animalTiles = arrayListOf(animal1, animal2, animal3)
        assertEquals(enclosure1.animalTiles[0], animal1)
        assertEquals(enclosure1.animalTiles[1], animal2)
        assertEquals(enclosure1.animalTiles[2], animal3)
    }

    /**
     * checking if getting and setting the vending stalls work
     */
    @Test
    fun testVendingStalls(){
        enclosure1.vendingStalls = arrayListOf(v1, v2, v3)
        assertEquals( enclosure1.vendingStalls[0], v1)
        assertEquals( enclosure1.vendingStalls[1], v2)
        assertEquals( enclosure1.vendingStalls[2], v3)
        assertNotSame( enclosure1.vendingStalls[2], v1)
    }

    /**
     * checking if setting and getting the value of "isBarn" work
     */
    @Test
    fun testBarn(){
        assertFalse(enclosure1.isBarn)
        enclosure1.isBarn = true
        assertTrue(enclosure1.isBarn)
    }
}