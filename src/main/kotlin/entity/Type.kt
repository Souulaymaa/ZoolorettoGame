package entity

/**
 * Enum to distinguish between the 4 possible genders of the animals in Zooloretto:
 * female, male, neutral and offspring
 */

enum class Type {
    MALE,
    FEMALE,
    NONE,
    OFFSPRING,
    ;


    /**
     * provide a single character to represent this value.
     * Returns one of: M/F/N/O
     */
    override fun toString() =
        when (this) {
            MALE -> "M"
            FEMALE -> "F"
            NONE -> "N"
            OFFSPRING -> "O"
        }
}