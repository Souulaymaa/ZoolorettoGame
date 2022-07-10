package gameai.moveoracle

import entity.Enclosure
import gameai.MoveOracle
import gamemockup.ZoolorettoGameStateMockups
import gamemockup.util.TileLists
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class ExpandZooTest {
    private val flamingo = TileLists.flamingos().first()
    private val camel = TileLists.camels().first()
    private val chimpanzee = TileLists.chimpanzees().first()
    private val kangaroo = TileLists.kangaroos().first()
    private val panda = TileLists.pandas().first()

    @Test
    fun expandWithTwoPlayersWithFullEnclosures(){
        val moveOracle = MoveOracle(ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory())
        val currentGameStateCopy = moveOracle.currentGameStateCopy
        val player = currentGameStateCopy.players.peek()

        //Fill player1's enclosures completely
        repeat(5){
            player.playerEnclosure[0].animalTiles.add(flamingo)
        }
        repeat(4){
            player.playerEnclosure[1].animalTiles.add(camel)
        }
        repeat(6){
            player.playerEnclosure[2].animalTiles.add(chimpanzee)
        }
        repeat(5){
            player.playerEnclosure[3].animalTiles.add(panda)
        }

        //Give player1 some coins
        player.coins = 3


        var moves = moveOracle.expandZooMove()
        assertEquals(1, moves.size)


        //Take some coins away
        player.coins = 2
        moves = moveOracle.expandZooMove()
        assertEquals(0, moves.size)

        //Give the player another two full enclosures
        val additionalEnclosure1 = Enclosure(5,1,1,Pair(9,5), false)
        val additionalEnclosure2 = Enclosure(5,1,1,Pair(9,5), false)

        //Fill both with some animal Tiles
        repeat(5){
            additionalEnclosure1.animalTiles.add(panda)
            additionalEnclosure2.animalTiles.add(panda)
        }

        player.playerEnclosure.add(additionalEnclosure1)
        player.playerEnclosure.add(additionalEnclosure2)

        //Check if we cannot expand now
        moves = moveOracle.expandZooMove()
        assertEquals(0, moves.size)
    }

    @Test
    fun expandWithTwoPlayersWithAlmostFullEnclosures(){
        val moveOracle = MoveOracle(ZoolorettoGameStateMockups.threePlayersZoolorettoGameStateFactory())
        val currentGameStateCopy = moveOracle.currentGameStateCopy
        val player = currentGameStateCopy.players.peek()

        //Fill player1's enclosures completely
        repeat(5){
            player.playerEnclosure[0].animalTiles.add(flamingo)
        }
        repeat(4){
            player.playerEnclosure[1].animalTiles.add(camel)
        }
        repeat(6){
            player.playerEnclosure[2].animalTiles.add(chimpanzee)
        }
        repeat(5){
            player.playerEnclosure[3].animalTiles.add(panda)
        }

        //Give player1 some coins
        player.coins = 3

        val moves = moveOracle.expandZooMove()
        assertEquals(1, moves.size)
    }
}
