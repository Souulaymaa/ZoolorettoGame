package gameAI.tree

import entity.*
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

    @Test
    fun testScore(){
        val zoolorettoGameState = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val chimpanzee = Animal(Type.NONE, Species.S, hasChild = true)

        //Fill all Trucks with Chimpanzee
        for (truck in zoolorettoGameState.deliveryTrucks){
            when(truck.maxSize){
                1 -> truck.tilesOnTruck.addAll(arrayListOf(chimpanzee) )
                2 -> truck.tilesOnTruck.addAll(arrayListOf(chimpanzee,chimpanzee) )
                3 -> truck.tilesOnTruck.addAll(arrayListOf(chimpanzee,chimpanzee,chimpanzee) )
            }
        }

        //Remove all enclosures of player 1, except for one with 4 maximum animal tiles to prune the tree
        //This enclosure is really worth to fill!!
        val player1 = zoolorettoGameState.players.peek()
        player1.playerEnclosure.removeAll { true }
        player1.playerEnclosure.add(Enclosure(
            maxAnimalSlots = 4,
            maxVendingStalls = 1,
            bonusCoins = 200,
            pointValues = Pair(10,10),
            false
        ))


        val node = Node(zoolorettoGameState, player1, arrayListOf() )

        assertDoesNotThrow {
            node.createChildren(2)

            node.children
        }
    }
}