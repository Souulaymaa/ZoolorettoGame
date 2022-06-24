package entity

/**
 * Class represents the DeliveryTrack
 *
 * @param size has the fixe value 3
 */
data class DeliveryTruck(val size: Int = 3) {

    val tilesOnTruck = arrayListOf<Tile>()
}