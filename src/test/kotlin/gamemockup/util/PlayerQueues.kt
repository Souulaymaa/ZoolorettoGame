package gamemockup.util

import entity.Difficulty
import entity.Player
import java.util.*

class PlayerQueues {
    companion object{
        val player1 = Player("player1", Difficulty.HUMAN)
        val player2 = Player("player2", Difficulty.HUMAN)
        val player3 = Player("player3", Difficulty.HUMAN)
        val player4 = Player("player4", Difficulty.HUMAN)
        val player5 = Player("player5", Difficulty.HUMAN)

        val hardBot1 = Player("hardbot1", Difficulty.HARD)
        val hardBot2 = Player("hardbot2", Difficulty.HARD)
        val hardBot3 = Player("hardbot3", Difficulty.HARD)
        val hardBot4 = Player("hardbot4", Difficulty.HARD)
        val hardBot5 = Player("hardbot5", Difficulty.HARD)


        val twoHumanPlayers : Queue<Player> = LinkedList(listOf(
            player1, player2
        ))

        val oneHumanOneBot : Queue<Player> = LinkedList(listOf(
            player1, hardBot1
        ))
    }
}