package entity

import kotlin.test.*
import java.util.*
import kotlin.test.assertEquals


/**
 * Test class for [TileStack].
 * Test with empty stack, right amount of tiles in the drawStack, wrong amount of tiles in endStack
 */
class TileStackTest {

    /**
     * Some tiles for adding to the different stacks
     */
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private val Animal1 = Animal(Type.FEMALE, Species.F)
    private val Animal2 = Animal(Type.MALE, Species.K)
    private val Animal3 = Animal(Type.OFFSPRING, Species.P)
    private val Coin1 = Coin()
    private val Coin2 = Coin()



    /**
     * Function to test the constructor of [TileStack] with correct arguments
     */
    @Test
    fun testConstructorCorrect(){
        tileStack.drawStack.add(Animal1)
        tileStack.drawStack.add(Animal2)
        tileStack.drawStack.add(Animal3)
        tileStack.endStack.add(Coin1)
        tileStack.endStack.add(Coin2)


        assertEquals(tileStack.drawStack.size, 3)
        assertEquals(tileStack.endStack.size,2)
    }

    /**
     * Function to test the constructor of [TileStack] with incorrect argument
     */
    /*@Test
    fun testConstructorIncorrect(){
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.endStack.add(Animal3)
        tileStackTooMany.drawStack.add(Coin1)
        tileStackTooMany.drawStack.add(Coin2)

        assertFailsWith<IllegalArgumentException> {tileStackTooMany}
    }*/
}