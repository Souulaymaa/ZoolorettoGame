package gameai.searchtree

import entity.ZoolorettoGameState
import gameai.Move
import service.GameStateService
import java.util.*
import kotlin.collections.ArrayList

/**
 * class Tree implemets Tree Data Structure to find the best Move that the AI-Bot can take.
 */
class Tree(private val zoolorettoGameState: ZoolorettoGameState, level: Int) {
    private val rootNode : Node

    init {
        val zoolorettoGameStateCopy = GameStateService.deepZoolorettoCopy(zoolorettoGameState)
        val currentPlayer = zoolorettoGameStateCopy.players.peek()

        rootNode = Node(zoolorettoGameStateCopy, currentPlayer, ArrayList())
        rootNode.createChildren(level)
    }

    /**
     * Performs a Breadth-First-Search on the [rootNode].
     *
     * First it initializes our nodeWithMaxScore with the [rootNode] and add the [rootNode] in our queue. Afterwards
     * it loops while the queue is not empty and picks the first Node left in the queue and checks, if the
     * score of the node is bigger than our nodeWithMaxScore. Then all child nodes of the current node are added
     * in the queue. The function does not keep track of the already visited nodes, since our graph is always a tree
     * and therefore no cycles exist.
     */
    private fun maximumBFS() : Node{
        val queue: Queue<Node> = LinkedList()
        var nodeWithMaxScore = rootNode

        queue.add(rootNode)

        while (queue.isNotEmpty()){
            val currentNode = queue.poll()

            //Check if we have a higher score, than we know already
            if (currentNode.aiScore >= nodeWithMaxScore.aiScore){
                nodeWithMaxScore = currentNode
            }

            //Add all children to the Queue
            currentNode.children.forEach {
                queue.add(it)
            }
        }

        return nodeWithMaxScore
    }

    /**
     * function that returns Best Move
     */
    fun getBestMove() : Move {
        val node = maximumBFS()

        return node.movesTaken.first()
    }
}
