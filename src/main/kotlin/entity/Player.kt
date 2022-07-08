package entity

import java.io.Serializable

/**
 * Represents the Player of the Zooloretto game
 *
 * @param playerName: string to store the player's name
 * @property coins: amount of coins that the player has
 * @property passed to check if the current player has passed
 * @param botSkillLevel: the level of game difficulty : EASY/MEDIUM/HARD/HUMAN
 * @property playerEnclosure: the enclosures of the player
 * @property barn: barn to store all extra tiles
 * @property chosenTruck: the truck that the player chose
 * @property score: the score achieved by the player
 */
data class Player(
    val playerName: String, val botSkillLevel: Difficulty
): Serializable {
    var playerEnclosure: MutableList<Enclosure> = mutableListOf()
    var barn = Enclosure(104, 12, 0, Pair(0,0), true)
    var passed: Boolean = false
    var coins = 2
    var chosenTruck : DeliveryTruck? = null
    var score = 0


}