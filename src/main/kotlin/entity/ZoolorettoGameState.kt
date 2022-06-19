package entity

import java.util.*

/**
 * Data class that represents a game state
 *
 * @param [paused] Indicates, if the game is paused
 * @param [roundDisc] Indicates, if the drawStack is empty
 * @param [bank] Indicates number of coins in the bank
 * @param [tileStack] Stack with all the tiles that are not selected yet
 * @param [deliveryTruck] List of the delivery Trucks
 */
data class ZoolorettoGameState(
    val paused: Boolean = false, val roundDisc: Boolean = false, val players : Queue<Player>,
    val bank: Int = 0, val tileStack: TileStack, val deliveryTruck: MutableList<DeliveryTruck>
) {
    init {
        require(deliveryTruck.size in 3..5){"The number of delivery trucks must be between 3 and 5."}
        require(players.size in 2..5){"Invalid number of players. It must be between 2 and 5."}
    }
}