package entity

import java.io.Serializable

/**
 * Enum to distinguish between 4 possible stall types:
 * vending1, vending2, vending3 and vending4.
 */

enum class StallType: Serializable {
    VENDING1,
    VENDING2,
    VENDING3,
    VENDING4,
    ;

    /**
     * provide a single character to represent this value.
     * Returns one of: V1/V2/V3/V4
     */
    override fun toString() =
        when(this) {
            VENDING1 -> "V1"
            VENDING2 -> "V2"
            VENDING3 -> "V3"
            VENDING4 -> "V4"
        }
}