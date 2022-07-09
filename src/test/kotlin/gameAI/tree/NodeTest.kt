package gameAI.tree

import gameAI.searchtree.Node
import gamemockup.ZoolorettoGameStateMockups
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import kotlin.test.assertEquals

class NodeTest {

    @Test
    fun OneLevelNode(){
        val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val player1 = zoolorettoGameState.players.peek()
        val node = Node(zoolorettoGameState, player1, arrayListOf() )

        node.createChildren(1)

        //The Game is fresh and we can only pick a truck, therefore we must have three children in our tree
        assertEquals(3, node.children.size)
    }

    @Test
    fun twoLevelNode(){
        val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val player1 = zoolorettoGameState.players.peek()
        val node = Node(zoolorettoGameState, player1, arrayListOf() )

        assertDoesNotThrow {
            node.createChildren(2)

            node.children
        }
    }
}