package gamemockup.util

import entity.Difficulty
import entity.Player
import java.util.*

class PlayerQueues {
    companion object{
        fun twoHumanPlayers() : Queue<Player> {
            return LinkedList(listOf(
                Player("player1", Difficulty.HUMAN),
                Player("player2", Difficulty.HUMAN)
            ))
        }

        fun threeHumanPlayers() : Queue<Player> {
            return LinkedList(listOf(
                Player("player1", Difficulty.HUMAN),
                Player("player2", Difficulty.HUMAN),
                Player("player3", Difficulty.HUMAN)
            ))
        }

        fun oneHumanOneBot() : Queue<Player> {
            return LinkedList(listOf(
                Player("player1", Difficulty.HUMAN),
                Player("hardbot1", Difficulty.HARD)
            ))
        }
    }
}