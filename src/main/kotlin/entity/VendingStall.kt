package entity

/**
 * Represents vending stall in the game
 * Implements an interface Tile
 * @param [stall] defines the stall type: v1/v2/v3/v4
 */


data class VendingStall (val stall: StallType):Tile{
    override fun toString(): String {
        return stall.toString()
    }
}
