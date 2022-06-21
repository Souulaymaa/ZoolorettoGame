package entity

/**
 * Class represents the DeliveryTrack
 *
 * @param size has the fixe value 3
 * @param tilesOnTruck is an Array with 0..3 Tiles
 */
data class DeliveryTruck(val size: Int = 3) {

    val tilesOnTruck = arrayListOf<Tile>()

    init {
        require(tilesOnTruck.size in 0..3) { "Size of tiles must be 0-3. Size was " + tilesOnTruck.size }
    }
}