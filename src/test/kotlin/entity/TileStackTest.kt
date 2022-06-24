package entity

import kotlin.test.*
import java.util.*
import kotlin.test.assertEquals


/**
 * Test class for [TileStack].
 */
class TileStackTest {

    /**
     * Some tiles for tests
     */
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private val tileStackTooManyTiles = TileStack(Stack<Tile>(), Stack<Tile>())
    private val animal1 = Animal(Type.FEMALE, Species.F)
    private val animal2 = Animal(Type.MALE, Species.K)
    private val animal3 = Animal(Type.OFFSPRING, Species.P)
    private val coin1 = Coin()
    private val coin2 = Coin()



    /**
     * Function to load tiles in the drawStack and endStack
     * to test the constructor of [TileStack] with correct arguments
     */
    @Test
    fun testConstructorCorrect(){
        tileStack.drawStack.add(animal1)
        tileStack.drawStack.add(animal2)
        tileStack.drawStack.add(animal3)
        tileStack.endStack.add(coin1)
        tileStack.endStack.add(coin2)


        assertEquals(tileStack.drawStack.size, 3)
        assertEquals(tileStack.endStack.size,2)
    }

    /**
     * Function to test the constructor of [TileStack] with incorrect argument
     */
    @Test
    fun testConstructorIncorrect(){
        for(i in 0..19){
            tileStackTooManyTiles.endStack.add(coin1)
        }
        assertFalse{ tileStackTooManyTiles.endStack.size <= 15 }
    }

}
