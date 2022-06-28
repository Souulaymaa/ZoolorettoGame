package entity

/**
 * Interface for a tile in the game Zooloretto
 * Classes Animal, Coin and VendingStall implement this interface
 *
 */
interface Tile

    fun toString() : String{
        throw NotImplementedError()
    }
