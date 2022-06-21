package entity

/**
 * Enum to distinguish between 4 difficulty levels: EASY, MEDIUM, HARD, HUMAN
 */

enum class Difficulty {
    EASY,
    MEDIUM,
    HARD,
    HUMAN,
    ;

    /**
     * provide a single character to represent this value.
     * Returns one of: E/M/H/HM
     */
    override fun toString() =
        when(this) {
            EASY -> "E"
            MEDIUM -> "M"
            HARD -> "H"
            HUMAN -> "HM"
        }


}