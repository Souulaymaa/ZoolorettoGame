package entity

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

/**
 * Test Class for testing all string representations of the [Species] enum
 */
class SpeciesTest {
    val flamingo = Species.F
    val panda = Species.P
    val kamel = Species.K
    val leopard = Species.L
    val zebra = Species.Z
    val elefant = Species.E
    val kaenguru = Species.U
    val schimpanse = Species.S


    /**
     * Test function for the [Species.toString] method.
     */
    @Test
    fun testToString(){
        assertEquals("Flamingo", flamingo.toString())
        assertEquals("Panda", panda.toString())
        assertEquals("Kamel", kamel.toString())
        assertEquals("Leopard", leopard.toString())
        assertEquals("Zebra", zebra.toString())
        assertEquals("Elefant", elefant.toString())
        assertEquals("Kaenguru", kaenguru.toString())
        assertEquals("Schimpanse", schimpanse.toString())
    }
}