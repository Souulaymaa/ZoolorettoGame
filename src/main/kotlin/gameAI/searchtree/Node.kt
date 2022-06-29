package gameAI.searchtree

import entity.Player
import entity.ZoolorettoGameState
import gameAI.Move
import gameAI.MoveOracle
import service.GameStateService
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class, that represents nodes in our tree of move combinations. A node contains always a [zoolorettoGameState]
 * in order to perform moves, without modifying the actual round and an [aiPlayer] and [aiScore] to keep track of the
 * player, who triggered the tree-search and its score.
 *
 * A node may have children [Node] and keeps track of the [Move] that led to the node's [zoolorettoGameState]
 *
 * @param zoolorettoGameState State of which we want to process our search
 * @param aiPlayer Player, who triggered the search for the best move
 * @param movesTaken Movement path from a [Node] to its root node
 */
class Node(val zoolorettoGameState : ZoolorettoGameState, val aiPlayer : Player, val movesTaken: ArrayList<Move>) {
    val children = mutableListOf<Node>()
    val aiScore : Int = calculateScore()

    private fun calculateScore(): Int {
        TODO("Implement score function")
    }

    /**
     * Recursive function to construct the tree, bounded by the parameter [level].
     *
     * In each node we first determine all currently allowed moves with the [MoveOracle] class. For each [Move]
     * we have retrieved by the [MoveOracle] we perform the Move on a copy of the [ZoolorettoGameState] and create
     * a child node with the move and the new state.
     */
    fun createChildren(level : Int){
        if (level == 0){
            return
        }
        else{
            val moveOracle = MoveOracle(zoolorettoGameState)
            val allowedMoves = moveOracle.determineAllCurrentAllowedMoves()

            for (move in allowedMoves){
                val copy = GameStateService.deepZoolorettoCopy(zoolorettoGameState)
                performMove(copy, move)

                val movesTakenPlusOne = ArrayList<Move>(movesTaken)
                movesTakenPlusOne.add(move)

                val child = Node(copy, aiPlayer, movesTakenPlusOne)

                //Perform recursion
                child.createChildren(level -1)
            }
        }
    }

    /**
     * Function that should execute Moves on a ZoolorettoGameState, might be using Move's performMove method
     */
    private fun performMove(zoolorettoGameState: ZoolorettoGameState, move: Move){
        TODO("Lookup how PlayerActionService works as soon as services are pushed on main")
    }
}
