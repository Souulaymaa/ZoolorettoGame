package gameAI

import entity.*
import gameAI.moves.*
import service.RootService
import service.deepZoolorettoCopy

class MoveOracle(currentGameState: ZoolorettoGameState) {
    private val currentGameStateCopy : ZoolorettoGameState
    private val rootService = RootService()

    init {
        this.currentGameStateCopy = deepZoolorettoCopy(currentGameState)
    }

    fun determineAllCurrentAllowedMoves() : List<Move> {
        throw NotImplementedError()
    }

    fun determineAllMoneyMoves() : List<Move> {
        val possibleMoneyMoves = arrayListOf<Move>()

        val rootService = RootService()
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val playerWallet = zoolorettoGame.currentGameState.players.peek().coins //ZoolorettoGame needs name on arrow in class Diagram

        val discardTile : DiscardTile // sanad
        val exchangeAllTilesBarnToEnclosure : ExchangeAllTilesBarnToEnclosure // micha
        val exchangeAllTilesEnclosureToEnclosure : ExchangeAllTilesEnclosureToEnclosure //micha
        val expandZoo : ExpandZoo// sanad
        val moveTileFromBarnToEnclosure : MoveTileFromBarnToEnclosure //kassem
        val moveVendingStallEnclosureToBarn : MoveVendingStallEnclosureToBarn //kassem
        val moveVendingStallEnclosureToEnclosure : MoveVendingStallEnclosureToEnclosure// kassem
        val purchaseTile : PurchaseTile // sanad

//        if (purchaseTilePossible()){
//            possibleMoneyMoves.add(purchaseTile)
//        }
        throw NotImplementedError()
    }

    /**
     * function to determine if the other players in the game have a tile in their Barn
     */
    private fun otherPlayerHasTileInBarn(currentPlayer: Player) : Map<Player, Set<Tile>>{
        val rootService = RootService()
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val purchasableTiles = HashMap<Player, Set<Tile>>()
        for(player in zoolorettoGame.currentGameState.players){
            if(currentPlayer != player) {
                /*purchasableTiles.put(player,)*/
                for (enclosure in player.playerEnclosure) {
                    if (enclosure.isBarn) {
                        for (tile in enclosure.animalTiles) {

                        }
                        for (tile in enclosure.vendingStalls) {
                        }
                    }

                }
            }
        }
    return HashMap()
    }

    private fun moveTileFromBarnToEnclosurePossible(): Boolean {
        //note: don't forget to check chosenTruck is empty
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()
        if (currentPlayer.coins < 1) {
            return false
        }
        if (currentPlayer.barn.animalTiles.size == 0 && currentPlayer.barn.vendingStalls.size == 0){
            return false
        }
        for(animalTile in currentPlayer.barn.animalTiles){
            for(enclosure in currentPlayer.playerEnclosure){
                if((animalTile.species == enclosure.animalTiles[0].species &&
                    enclosure.animalTiles.size < enclosure.maxAnimalSlots) || (enclosure.animalTiles.size == 0)){
                    return true
                }
            }
        }
        if(currentPlayer.barn.vendingStalls.size != 0){
            for (enclosure in currentPlayer.playerEnclosure){
                if(enclosure.vendingStalls.size < enclosure.maxVendingStalls){
                    return true
                }
            }
        }
        return false
    }

    /**
     * function to determine if the purchaseTileAction is possible
     */
//    private fun purchaseTilePossible() : Boolean{
//        val rootService = RootService()
//        val currentPlayer = rootService.zoolorettoGame.currentGameState.players.peek()
//        return if(currentPlayer.coins >= 2){
//            otherPlayerHasTileInBarn(currentPlayer)
//        }else{
//            false
//        }
//    }

    fun determineAllTruckRelatedMoves() : List<Move>{
        throw  NotImplementedError()
    }

    /**
     * Function to determine alle possible "Exchange All Tiles" Moves (either from barn to enclosure or
     * from enclosure to enclosure)
     */
    fun determineExchangeAllTileMoves() : List<Move>{
        val currentPlayer = Player(
            "mockup player to enable type checking in IDE please replace with rootService later", Difficulty.HUMAN,
            mutableListOf(),
            barn=Enclosure(Int.MAX_VALUE, Int.MAX_VALUE, 0, Pair(10,6), isBarn = true)
        )

        val moveList = ArrayList<Move>()

        if(currentPlayer.coins >= 1){
           val combinations = determineSwapCombinations(currentPlayer)

            for((k,v) in combinations){
                TODO("Generate moves from combinations, don't forget to seperate barn and enclosure moves")
            }
        }

        return moveList
    }

    private fun determineSwapCombinations(player: Player) : Map<Enclosure, List<Enclosure>>{
        val combinations : HashMap<Enclosure, List<Enclosure>> = HashMap()

        //Create a new list to union playerEnclosure and barn, without modifying playerEnclosure directly
        val playerEnclosureAndBarn = ArrayList(player.playerEnclosure)
        playerEnclosureAndBarn.add(player.barn)

        for (j in 0..playerEnclosureAndBarn.size){
            //Enclosure of the current iteration
            val sourceEnclosure : Enclosure = playerEnclosureAndBarn[j]
            val swapTargets = ArrayList<Enclosure>()

            //Now find all possible swaps
            for(k in j .. playerEnclosureAndBarn.size){
                val targetEnclosure : Enclosure = playerEnclosureAndBarn[k]

                val targetEnclosureHasEnoughSlots = targetEnclosure.maxAnimalSlots <= sourceEnclosure.animalTiles.size
                val sourceEnclosureHasEnoughSlots = sourceEnclosure.maxAnimalSlots <= targetEnclosure.animalTiles.size

                if(targetEnclosureHasEnoughSlots &&  sourceEnclosureHasEnoughSlots){
                    swapTargets.add(targetEnclosure)
                }
            }

            //Create a new entry in the map
            combinations[sourceEnclosure] = swapTargets
        }
        return HashMap()
    }
}

