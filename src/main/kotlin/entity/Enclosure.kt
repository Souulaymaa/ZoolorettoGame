package entity

/**
 * Data Class that represents an Enclosure. it could be a zoo board, an expansion board or a barn
 */

data class Enclosure(val maxAnimalSlots: Int, val maxVendingStalls: Int, val bonusCoins: Int,
                val pointValues: Pair<Int,Int>, val isBarn: Boolean ) {

    val animalTiles = arrayListOf<Animal>()
    val vendingStalls = arrayListOf<VendingStall>()

    val enclosureNumber = String //ex. one, two... important for hint.
}