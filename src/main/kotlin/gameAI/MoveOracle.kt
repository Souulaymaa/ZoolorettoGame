package gameAI

import entity.*
import gameAI.moves.*
import service.GameStateService.Companion.deepZoolorettoCopy
import service.RootService

class MoveOracle(currentGameState: ZoolorettoGameState) {
    private val currentGameStateCopy : ZoolorettoGameState
    private val rootService = RootService()

    init {
        this.currentGameStateCopy = deepZoolorettoCopy(currentGameState)
    }

    fun determineAllCurrentAllowedMoves() : List<Move> {
        //don't forget to check if current player has taken truck or not before calculating available money action moves
        throw NotImplementedError()
    }

    private fun determineAllTruckRelatedMoves() : List<Move>{
        throw  NotImplementedError()
    }

    private fun determineAllMoneyMoves() : List<Move> {

        //don't forget to union all money moves in correct format/type.

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


        throw NotImplementedError()
    }


    /**
     *function that determines whether Expand Zoo money action is feasible or not
     * @return Array list containing one move or no move at all depending on outcome
     * the move within the Array List is the ExpandZoo() move itself.
     */
    private fun expandZooMove() : ArrayList<Move> {
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()
        val moveList = ArrayList<Move>()

        if(currentPlayer.coins < 3){
            return moveList
        }
        for (enclosure in currentPlayer.playerEnclosure){
            if (enclosure.animalTiles.isEmpty()){
                return moveList
            }
        }
        val expandZoo = ExpandZoo()
        moveList.add(expandZoo)
        return moveList
    }

    /**
     * function that
     * @return list of all moves a player can make with "DiscardTile" action.
     */
    private fun discardTileMoves(): ArrayList<Move>
    {
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()
        val moveList = ArrayList<Move>()
        val discardableTiles = discardablePlayerTiles(currentPlayer)

        for (tile in discardableTiles) {
            moveList.add(DiscardTile(tile))
        }

        return moveList
    }

    /**
     * function to determine if the player has a tile in their Barn, which would enable the "DiscardTile" action
     * @return set of Tiles that can be discarded
     */
    private fun discardablePlayerTiles(currentPlayer: Player) :  Set<Tile>{
        val zoolorettoGame = rootService.zoolorettoGame
        val discardableTiles = mutableSetOf<Tile>()
        checkNotNull(zoolorettoGame)

        if(currentPlayer.coins < 2){
            return discardableTiles
        }
        for (enclosure in currentPlayer.playerEnclosure) {
            if (!enclosure.isBarn) {
                continue                                         //skips all enclosures of player that is not barn
            }
            for (tile in enclosure.animalTiles) {
                discardableTiles.add(tile)
            }
            for (tile in enclosure.vendingStalls) {
                discardableTiles.add(tile)
            }
        }
        return discardableTiles
    }

    /**
     * function that returns a list of all move combination a player can make with "PurchaseTile" action
     */
    private fun purchaseTileMoves(): ArrayList<Move>
    {
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()

        val combinations = purchasableOtherPlayerTiles(currentPlayer)
        val moveList = ArrayList<Move>()
        for ((player, purchasableTiles) in combinations) {
            for (tile in purchasableTiles) {
                moveList.add(PurchaseTile(player, tile))
            }
        }
        return moveList
    }


    /**
     * function to determine if the other players in the game have a tile in their Barn, if so
     * maps each player with every purchasable tile found on his barn.
     * @return map of player to tiles
     */
    private fun purchasableOtherPlayerTiles(currentPlayer: Player) : Map<Player, Set<Tile>>{
        val zoolorettoGame = rootService.zoolorettoGame
        val purchasableTiles : HashMap<Player, Set<Tile>> = HashMap()

        checkNotNull(zoolorettoGame)

        if(currentPlayer.coins < 2){
            return purchasableTiles
        }
        for(player in zoolorettoGame.currentGameState.players){
            val tileSet = mutableSetOf<Tile>()
            if(currentPlayer == player) {
                continue                                             //skips the current player using continue
            }
            for (enclosure in player.playerEnclosure) {
                if (!enclosure.isBarn) {
                    continue                                         //skips all enclosures of player that is not barn
                }
                for (tile in enclosure.animalTiles) {
                    tileSet.add(tile)
                }
                for (tile in enclosure.vendingStalls) {
                    tileSet.add(tile)
                }
            }
            for(tile in tileSet){
                var found = false
                for(enclosure in currentPlayer.playerEnclosure){
                    if(tile == enclosure.animalTiles[0]){
                        found = true
                        continue
                    }
                }
                if(!found){
                    tileSet.remove(tile)
                }
            }
            purchasableTiles[player] = tileSet
        }
    return purchasableTiles
    }



    /**
     * function to determine all the possible moveTileFromBarnToEnclosure actions.
     * @return a map of each tile in the barn to a list of enclosures it can be moved to.
     */
    private fun possibleTileBarnToEnclosureMoves(): Map<Tile, List<Enclosure>>{
        val possibleMoves : HashMap<Tile, List<Enclosure>> = HashMap()
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()
        if (currentPlayer.coins < 1) {
            return possibleMoves
        }
        if (currentPlayer.barn.animalTiles.size == 0 && currentPlayer.barn.vendingStalls.size == 0){
            return possibleMoves
        }
        for(animalTile in currentPlayer.barn.animalTiles){
            var enclosures: MutableList<Enclosure> = mutableListOf()
            for(enclosure in currentPlayer.playerEnclosure){
                if((animalTile.species == enclosure.animalTiles[0].species &&
                            enclosure.animalTiles.size < enclosure.maxAnimalSlots) || (enclosure.animalTiles.size == 0)){
                    enclosures.add(enclosure)

                }
            }
            possibleMoves[animalTile] = enclosures
        }
        for(vendingTile in currentPlayer.barn.vendingStalls){
            var enclosures: MutableList<Enclosure> = mutableListOf()
            for (enclosure in currentPlayer.playerEnclosure){
                if(enclosure.vendingStalls.size < enclosure.maxVendingStalls){
                    enclosures.add(enclosure)
                }
            }
            possibleMoves[vendingTile] = enclosures
        }
        return possibleMoves
    }

    /**
     * function to determine all the possible moveVendingStallEnclosureToBarn actions.
     * @return list of enclosures containing vending stalls.
     */
    private fun possibleVendingStallEnclosureToBarnMoves(): List<Enclosure>{
        val possibleMoves: MutableList<Enclosure> = mutableListOf()
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()
        if(currentPlayer.coins < 1){
            return possibleMoves
        }
        for(enclosure in currentPlayer.playerEnclosure){
            if(enclosure.vendingStalls.size > 0){
                possibleMoves.add(enclosure)
            }
        }
        return possibleMoves
    }

    /**
     * function to determine all the possible moveVendingStallEnclosureToEnclosure actions.
     * @return map of an enclosure containing a vending stall to a list of enclosures the vending
     * stall can be moved to.
     */
    private fun possibleVendingStallEnclosureToEnclosureMoves(): Map<Enclosure, List<Enclosure>>{
        val possibleMoves: HashMap<Enclosure,List<Enclosure>> = HashMap()
        val zoolorettoGame = rootService.zoolorettoGame
        checkNotNull(zoolorettoGame)
        val currentPlayer = zoolorettoGame.currentGameState.players.peek()
        if(currentPlayer.coins < 1){
            return possibleMoves
        }
        for(sourceEnclosure in currentPlayer.playerEnclosure){
            if(sourceEnclosure.vendingStalls.size > 0){
                var enclosures: MutableList<Enclosure> = mutableListOf()
                for(targetEnclosure in currentPlayer.playerEnclosure){
                    if (sourceEnclosure != targetEnclosure && targetEnclosure.vendingStalls.size < targetEnclosure.maxVendingStalls){
                        enclosures.add(targetEnclosure)
                    }
                }
                possibleMoves[sourceEnclosure] = enclosures
            }
        }
        return possibleMoves
    }

    /**
     * Function to determine all possible "Exchange All Tiles" Moves (either from barn to enclosure or
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

                if(eligibleToSwap(sourceEnclosure, targetEnclosure)){
                    swapTargets.add(targetEnclosure)
                }
            }

            //Create a new entry in the map
            combinations[sourceEnclosure] = swapTargets
        }

        return combinations;
    }

    /**
     * Function to check if two enclosures are compatible by their sizes and animal type when swapping.
     * Two enclosures are compatible, if they can hold each other amount of animal tiles and the
     * animal types of both enclosures differ.
     *
     * @param source first enclosure to swap, naming does not matter due to symmetry
     * @param target second enclosure to swap, naming does not matter due to symmetry
     * @return true if enclosures are compatible, false otherwise
     */
    private fun eligibleToSwap(source : Enclosure, target :Enclosure): Boolean {
        val targetEnclosureHasEnoughSlots = target.maxAnimalSlots <= source.animalTiles.size
        val sourceEnclosureHasEnoughSlots = source.maxAnimalSlots <= target.animalTiles.size

        var sameAnimalSpecies = false;

        if(source.animalTiles.isNotEmpty() && target.animalTiles.isNotEmpty()){
            val sourceSpecies = source.animalTiles[0].species
            val targetSpecies = target.animalTiles[0].species

            sameAnimalSpecies = sourceSpecies == targetSpecies
        }


        return targetEnclosureHasEnoughSlots && sourceEnclosureHasEnoughSlots && !sameAnimalSpecies
    }
}

