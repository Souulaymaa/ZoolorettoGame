package entity

import java.io.Serializable

/**
 * Enum to distinguish between game species (animals are called with letters)
 */
enum class Species: Serializable {
    F,
    P,
    K,
    S,
    L,
    Z,
    E,
    U
    ;

    /**
     *
     * Provide a single character of the animal's name and the full name will be returned
     * Returns one of: F/P/K/S/L/Z/E/U
     */
    override fun toString() =
        when(this) {
            F -> "Flamingo"
            P -> "Panda"
            K -> "Kamel"
            S -> "Schimpanse"
            L -> "Leopard"
            Z -> "Zebra"
            E -> "Elefant"
            U -> "Kaenguru"
        }
}