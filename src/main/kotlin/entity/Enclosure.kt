package entity

/**
 * Data Class that represents an Enclosure. it could be a zoo board, an expansion board or a barn
 */

data class Enclosure(val maxAnimalSlots: Int, val maxVendingStalls: Int, val bonusCoins: Int,
                val pointValues: Pair<Int,Int>, val isBarn: Boolean ) {

    var animalTiles = arrayListOf<Animal>()
    var vendingStalls = arrayListOf<VendingStall>()
}