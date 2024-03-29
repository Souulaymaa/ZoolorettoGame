package gameai.searchtree

import entity.Player
import entity.ZoolorettoGame
import entity.ZoolorettoGameState
import gameai.Move
import gameai.MoveOracle
import service.GameStateService
import service.RootService
import service.ScoreService
import kotlin.collections.ArrayList

/**
 * Class, that represents nodes in our tree of move combinations. A node always contains  a [zoolorettoGameState]
 * in order to perform moves, without modifying the actual round and an [aiPlayer] and [aiScore] to keep track of the
 * player, who triggered the tree-search and its score.
 *
 * A node may have children [Node] and keeps track of the [Move] that led to the node's [zoolorettoGameState]
 *
 * @param zoolorettoGameState State of which we want to process our search
 * @param aiPlayer Player, who triggered the search for the best move
 * @param movesTaken Movement path from a [Node] to its root node
 */
class Node(private val zoolorettoGameState : ZoolorettoGameState, private val aiPlayer : Player, val movesTaken: ArrayList<Move>) {
    val children = mutableListOf<Node>()
    val aiScore : Int = calculateScore()

    private fun calculateScore(): Int {
        val fakeRootService = RootService()
        val scoreService = ScoreService(fakeRootService)

        return scoreService.determineScore(aiPlayer)
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

                val aiPlayerInCopy = findAiPlayerInCopy(aiPlayer,copy)
                val child = Node(copy, aiPlayerInCopy, movesTakenPlusOne)
                this.children.add(child)

                //Perform recursion, consider using a try catch here
                child.createChildren(level -1)
            }
        }
    }

    /**
     * Function that should execute Moves on a ZoolorettoGameState, might be using Move's performMove method
     */
    private fun performMove(copy: ZoolorettoGameState, move: Move){
        val rootService = RootService()
        rootService.zoolorettoGame = ZoolorettoGame(1.0f, copy)

        move.performMove(rootService)
    }

    private fun findAiPlayerInCopy(toFind : Player, copy : ZoolorettoGameState) : Player{
        val playerListInCopy = copy.players

        val newAiPlayer = playerListInCopy.find { it == toFind }

        checkNotNull(newAiPlayer)

        return newAiPlayer
    }
}
