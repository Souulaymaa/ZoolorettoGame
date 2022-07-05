package entity

import java.io.Serializable

/**
 * Class represents the DeliveryTrack
 *
 * @param maxSize has the fixe value 3
 * @property tilesOnTruck is an Array with 0..3 Tiles
 */
data class DeliveryTruck(val maxSize: Int = 3): Serializable {

    val tilesOnTruck = arrayListOf<Tile>()

    init {
        require(tilesOnTruck.size in 0..3) { "Size of tiles must be 0-3. Size was " + tilesOnTruck.size }
    }
}
