package entity

/**
 * Represents the Player of the Zooloretto game
 *
 * @param playerName: string to store the player's name
 * @param coins: amount of coins that the player has
 * @param passed: boolean to check if the current player has passed
 * @param botSkillLevel: the level of game difficulty : EASY/MEDIUM/HARD/HUMAN
 * @param playerEnclosure: the enclosures of the player
 * @param barn: barn to store all extra tiles
 * @param chosenTruck: the truck that the player chose
 * @param score: the score achieved by the player
 */
data class Player(
    val playerName: String, val botSkillLevel: Difficulty,
    val playerEnclosure: MutableList<Enclosure>, val barn: Enclosure
){
    val passed: Boolean = false
    val coins = 2
    val chosenTruck = DeliveryTruck()
    val score = 0

    init{
        require(playerEnclosure.size in 3..5){"Invalid number of Enclosures."}
    }
}