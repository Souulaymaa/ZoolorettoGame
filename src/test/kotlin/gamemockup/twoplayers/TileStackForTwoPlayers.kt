package gamemockup.twoplayers

import entity.Tile
import entity.TileStack
import gamemockup.util.TileLists
import java.util.*

class TileStackForTwoPlayers{
    companion object{
        // TileStack without zebras and elephants
        fun getTileStack() : TileStack  {
            //List containing all tiles for a game with two players
            val allTilesForTwoPlayers = mutableListOf<Tile>()

            allTilesForTwoPlayers.addAll(TileLists.flamingos())
            allTilesForTwoPlayers.addAll(TileLists.camels())
            allTilesForTwoPlayers.addAll(TileLists.chimpanzees())
            allTilesForTwoPlayers.addAll(TileLists.kangaroos())
            allTilesForTwoPlayers.addAll(TileLists.pandas())
            allTilesForTwoPlayers.addAll(TileLists.leopards())
            allTilesForTwoPlayers.addAll(TileLists.coins())
            allTilesForTwoPlayers.addAll(TileLists.vendingStalls())

            allTilesForTwoPlayers.shuffle(Random((42)))

            val endgameStack = Stack<Tile>()
            val gameStack = Stack<Tile>()

            //Remove 15 tiles from all tiles and add them into the endgameStack
            repeat(15){
                val removedTile = allTilesForTwoPlayers.removeLast()
                endgameStack.add(removedTile)
            }

            //Add all spare tiles to gameStack
            gameStack.addAll(allTilesForTwoPlayers)

            return TileStack(gameStack, endgameStack)
        }
    }
}