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
     *
     * @return List of possible "Exchange All Tiles" Moves
     */
    fun determineExchangeAllTileMoves() : List<Move>{
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()

        var moveList = ArrayList<Move>()

        if(currentPlayer.coins >= 1){
           val combinations = determineSwapCombinations(currentPlayer)

           moveList = exchangeAllTilesCombinationsToMoves(combinations, currentPlayer)
        }

        return moveList
    }

    /**
     * Function to "flatten" the map of [combinations] into a List of moves. Notice that once we hit a barn enclosure
     * we use the Player and the [ExchangeAllTilesBarnToEnclosure] class and [ExchangeAllTilesEnclosureToEnclosure]
     * otherwise.
     *
     * @param combinations Mapping from an enclosure to possible swap targets
     * @param currentPlayer Parameter to fit the [ExchangeAllTilesBarnToEnclosure] cases
     * @return List of all possible move combinations as Move type
     */
    private fun exchangeAllTilesCombinationsToMoves(combinations: Map<Enclosure, List<Enclosure>>,
                                                    currentPlayer: Player): ArrayList<Move>
     {
        val moveList = ArrayList<Move>()

        for ((sourceEnclosure, targetList) in combinations) {
            for (targetEnclosure in targetList) {
                if (sourceEnclosure.isBarn) {
                    moveList.add(ExchangeAllTilesBarnToEnclosure(targetEnclosure, currentPlayer))
                } else if (targetEnclosure.isBarn) {
                    moveList.add(ExchangeAllTilesBarnToEnclosure(sourceEnclosure, currentPlayer))
                } else {
                    moveList.add(ExchangeAllTilesEnclosureToEnclosure(sourceEnclosure, targetEnclosure))
                }
            }
        }

         return moveList
    }

    /**
     * Function to determine valid swap combinations of a player's enclosures and barn. The function first unions
     * the player's barn with the player's enclosures in a new list. Then a nested for-loop creates
     * all swap combinations. Due to the symmetry of swaps, the nested for-loop can start at one enclosure further
     * than the outer loop.
     *
     * @param player Player with a barn and enclosure
     * @return combinations Valid swap combinations
     */
    private fun determineSwapCombinations(player: Player) : Map<Enclosure, List<Enclosure>>{
        val combinations : HashMap<Enclosure, List<Enclosure>> = HashMap()

        //Create a new list to union playerEnclosure and barn, without modifying playerEnclosure directly
        val playerEnclosureAndBarn = ArrayList(player.playerEnclosure)
        playerEnclosureAndBarn.add(player.barn)

        for (j in 0 until playerEnclosureAndBarn.size){
            //Enclosure of the current iteration
            val sourceEnclosure : Enclosure = playerEnclosureAndBarn[j]
            val swapTargets = ArrayList<Enclosure>()

            //Now find all possible swaps
            for(k in j + 1 until playerEnclosureAndBarn.size){
                //k might be bigger or equal
                if(k >= playerEnclosureAndBarn.size){
                    break
                }

                val targetEnclosure : Enclosure = playerEnclosureAndBarn[k]

                if(egibleToSwap(sourceEnclosure, targetEnclosure)){
                    swapTargets.add(targetEnclosure)
                }
            }

            //Create a new entry in the map
            combinations[sourceEnclosure] = swapTargets
        }

        return combinations;
    }

    /**
     * Function to check if two enclosures are compatible by their sizes when swapping
     *
     * @param source first enclosure to swap, naming does not matter due to symmetry
     * @param target second enclosure to swap, naming does not matter due to symmetry
     * @return true if enclosures are compatible, false otherwise
     */
    private fun egibleToSwap(source : Enclosure, target :Enclosure): Boolean {
        val targetEnclosureHasEnoughSlots = target.maxAnimalSlots <= source.animalTiles.size
        val sourceEnclosureHasEnoughSlots = source.maxAnimalSlots <= target.animalTiles.size

        return targetEnclosureHasEnoughSlots && sourceEnclosureHasEnoughSlots
    }
}

