package entity

import java.util.*

/**
 * Data class that represents a game state
 *
 * @param [paused] Indicates, if the game is paused
 * @param [roundDisc] Indicates, if the drawStack is empty
 * @property [bank] Indicates number of coins in the bank
 * @param [tileStack] Stack with all the tiles that are not selected yet
 * @param [deliveryTrucks] List of the delivery Trucks
 */
data class ZoolorettoGameState(
    var paused: Boolean = false, var roundDisc: Boolean = false, val players: Queue<Player>,
    val tileStack: TileStack, val deliveryTrucks: MutableList<DeliveryTruck>
) {
    var bank = 0
    init {
        require(deliveryTrucks.size in 3..5){"The number of delivery trucks must be between 3 and 5."}
        require(players.size in 2..5){"Invalid number of players. It must be between 2 and 5."}
    }
}