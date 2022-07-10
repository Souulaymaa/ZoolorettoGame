package entity

import java.io.Serializable

/**
 * Interface for a tile in the game Zooloretto
 * Classes Animal, Coin and VendingStall implement this interface
 *
 */
abstract class Tile: Serializable


    /**
     * to String method
     */
    fun toString() : String{
            throw NotImplementedError()
    }
