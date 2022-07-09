package gameAI.tree

import gameAI.searchtree.Node
import gamemockup.ZoolorettoGameStateMockups
import org.junit.jupiter.api.Test
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

//    @Test
//    fun EightLevelNode(){
//        val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
//        val player1 = zoolorettoGameState.players.peek()
//        val node = Node(zoolorettoGameState, player1, arrayListOf() )
//
//        //Test remove one truck
//        zoolorettoGameState.deliveryTrucks.removeLast()
//
//        node.createChildren(8)
//
//        //The Game is fresh and we can only pick a truck, therefore we must have three children in our tree
//        node.children
//    }
//
    @Test
    fun twoLevelNode(){
        val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val player1 = zoolorettoGameState.players.peek()
        val node = Node(zoolorettoGameState, player1, arrayListOf() )

        node.createChildren(2)

        node.children
    }
}