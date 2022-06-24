package gameAI

import entity.Player
import entity.Tile
import entity.ZoolorettoGameState
import gameAI.moves.*
import service.deepZoolorettoCopy

class MoveOracle(currentGameState: ZoolorettoGameState) {
    val currentGameStateCopy : ZoolorettoGameState

    init {
        this.currentGameStateCopy = deepZoolorettoCopy(currentGameState)
    }

    fun determineAllCurrentAllowedMoves() : List<Move> {
        throw NotImplementedError()
    }

    fun determineAllMoneyMoves() : List<Move> {
        val possibleMoneyMoves = arrayListOf<Move>()

        val rootService = RootService()
        val playerWallet = rootService.ZoolorettoGame.currentGameState.players.peek().coins //ZoolorettoGame needs name on arrow in class Diagram

        val discardTile : DiscardTile
        val exchangeAllTilesBarnToEnclosure : ExchangeAllTilesBarnToEnclosure
        val exchangeAllTilesEnclosureToEnclosure : ExchangeAllTilesEnclosureToEnclosure
        val expandZoo : ExpandZoo
        val moveTileFromBarnToEnclosure : MoveTileFromBarnToEnclosure
        val moveVendingStallEnclosureToBarn : MoveVendingStallEnclosureToBarn
        val moveVendingStallEnclosureToEnclosure : MoveVendingStallEnclosureToEnclosure
        val purchaseTile : PurchaseTile

        if (purchaseTilePossible()){
            possibleMoneyMoves.add(purchaseTile)
        }
        throw NotImplementedError()
    }

    /**
     * function to determine if the other players in the game have a tile in their Barn
     */
    private fun otherPlayerHasTileInBarn(currentPlayer: Player) : Map<Player, Set<Tile>>{
        val rootService = RootService()
        val purchasableTiles = HashMap<Player, Set<Tile>>()
        for(player in rootService.ZoolorettoGame.currentGameState.players){
            if(currentPlayer != player) {
                purchasableTiles.put(player,)
                for (enclosure in player.playerEnclosure) {
                    if (enclosure.isBarn) {
                        for (tile in enclosure.animalTiles) {

                        }
                        for (tile in enclosure.vendingStall) {
                        }
                    }

                }
            }
        }

    }


    /**
     * function to determine if the purchaseTileAction is possible
     */
    private fun purchaseTilePossible() : Boolean{
        val rootService = RootService()
        val currentPlayer = rootService.ZoolorettoGame.currentGameState.players.peek()
        return if(currentPlayer.coins >= 2){
            otherPlayerHasTileInBarn(currentPlayer)
        }else{
            false
        }
    }

    fun determineAllTruckRelatedMoves() : List<Move>{
        throw  NotImplementedError()
    }
}

