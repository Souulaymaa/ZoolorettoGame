package entity

/**
 * Enum to distinguish between 4 possible stall types:
 * vending1, vending2, vending3 and vending4.
 */

enum class StallType {
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
            StallType.VENDING1 -> "V1"
            StallType.VENDING2 -> "V2"
            StallType.VENDING3 -> "V3"
            StallType.VENDING4 -> "V4"
        }
}