package entity

/**
 * Class represents the DeliveryTrack
 *
 * @param [maxSize] has the default constant 3
 * @property [tilesOnTruck] is an Array with 0..3 Tiles
 */
data class DeliveryTruck(val maxSize: Int = 3) {

    val tilesOnTruck = arrayListOf<Tile>()
}