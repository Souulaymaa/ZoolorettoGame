package entity

import java.io.Serializable

/**
 * Represents an animal on a tile in the game
 * Implements an interface Tile
 * @param [type] defines the animal type: m/w/none/offspring
 * @param species represents which type of 8 species animal belongs to
 */


data class Animal (val type: Type, val species: Species, var hasChild: Boolean = false):Tile(), Serializable
{
    override fun toString(): String {
        return species.toString()
    }
}
