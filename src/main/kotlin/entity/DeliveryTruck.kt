package entity

import java.io.Serializable

/**
 * Class represents the DeliveryTrack
 *
 * @param maxSize has the fixe value 3
 * @property tilesOnTruck is an Array with 0..3 Tiles
 */
data class DeliveryTruck(val maxSize: Int = 3): Serializable {

    var tilesOnTruck = arrayListOf<Tile>()

}
