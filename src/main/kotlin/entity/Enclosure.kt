package entity

import java.io.Serializable

/**
 * Data Class that represents an Enclosure. it could be a zoo board, an expansion board or a barn
 */

data class Enclosure(val maxAnimalSlots: Int, val maxVendingStalls: Int, var bonusCoins: Int,
                val pointValues: Pair<Int,Int>, var isBarn: Boolean ): Serializable {

    var animalTiles = arrayListOf<Animal>()
    var vendingStalls = arrayListOf<VendingStall>()
}