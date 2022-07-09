package gameAI

import entity.*
import gameAI.moves.*

class MoveOracle(currentGameState: ZoolorettoGameState) {
     val currentGameStateCopy : ZoolorettoGameState

    init {
        this.currentGameStateCopy = currentGameState
    }

    /**
     * Function, that returns all current allowed moves for a given [ZoolorettoGameState] in the constructor of the
     * class and the current player. This method is the main method of this class, and only it should be called.
     *
     * Unions all truck related moves with all money moves.
     *
     * @return ArrayList of all current allowed moves
     */
    fun determineAllCurrentAllowedMoves() : List<Move> {
        val allCurrentAllowedMoves = ArrayList<Move>()

        if(!currentGameStateCopy.players.peek().passed) {
            allCurrentAllowedMoves.addAll(determineAllTruckRelatedMoves())
            allCurrentAllowedMoves.addAll(determineAllMoneyMoves())
        }
        return allCurrentAllowedMoves
    }

    /**
     * Function, that returns all truck related moves for the current player.
     *
     * Returns either add-tile-to-truck-moves or add-take-truck-moves.
     *
     * @return ArrayList of either add-tile-to-truck-moves or add-take-truck-moves
     */
     fun determineAllTruckRelatedMoves() : List<Move>{
        val possibleMoneyMoves = arrayListOf<Move>()

        val addTileToTruckMoves = determineAddTileToTruckMoves()
        val takeTruckMoves = determineTakeTruckMoves()
        possibleMoneyMoves.addAll(addTileToTruckMoves)
        possibleMoneyMoves.addAll(takeTruckMoves)
        return possibleMoneyMoves
    }

    /**
     * Function, that determines all possible [AddTileToTruck] moves assuming, that when the function is called,
     * the current player may choose draw a tile and add it on the truck.
     *
     * The function iterates over all delivery trucks and creates new [AddTileToTruck] moves
     * and returns them as a list.
     */
    private fun determineAddTileToTruckMoves() : List<Move>{
        val moves = mutableListOf<AddTileToTruck>()
        val deliveryTrucks = currentGameStateCopy.deliveryTrucks

        for(i in 0 until deliveryTrucks.size){
            val deliveryTruck = deliveryTrucks[i]
            if(deliveryTruck.tilesOnTruck.size < deliveryTruck.maxSize) {
                moves.add(AddTileToTruck(i))
            }
        }
        return moves
    }

    /**
     * Function, that determines all possible [TakeTruckAndPlaceTiles] moves.
     *
     * The function iterates over all delivery trucks. If the truck is not empty it creates a new [TakeTruckAndPlaceTiles] move
     * and finds the best option between all available trucks to take
     */
    private fun determineTakeTruckMoves() : List<Move>{
        val moves = mutableListOf<Move>()
        val playerEnclosures = currentGameStateCopy.players.peek().playerEnclosure

        val indexedTrucks = currentGameStateCopy.deliveryTrucks.mapIndexed { index, deliveryTruck ->
            Pair(
                index,
                deliveryTruck
            )
        }
        val indexedEnclosures = playerEnclosures.mapIndexed { index, enclosure -> Pair(index, enclosure) }

        val notEmptyNorFullEnclosures = indexedEnclosures.filter {
            val enclosure = it.second
            enclosure.animalTiles.isNotEmpty() && enclosure.animalTiles.size < enclosure.maxAnimalSlots
        }
        val emptyEnclosures = indexedEnclosures.filter {
            it.second.animalTiles.isEmpty()
        }


        val nonEmptyTrucks = indexedTrucks.filter { it.second.tilesOnTruck.isNotEmpty() }

        var maxScore = -1
        var chosenTruckPair : Pair<Int, DeliveryTruck> = Pair(-1, DeliveryTruck())
        //chooses the best truck to be taken
        for (indexedTruckPair in nonEmptyTrucks){
            val animalList = ArrayList<Animal>()
            val vendingList = ArrayList<VendingStall>()
            var score = 0
            //adds tile in animal lists or vendingStall lists.
            //also increments score when coin tile is found
            for(tile in indexedTruckPair.second.tilesOnTruck){
                when (tile) {
                    is Animal -> {
                        animalList.add(tile)
                    }
                    is VendingStall -> {
                        vendingList.add(tile)
                    }
                    else -> {
                        // tile is coin
                        score ++
                    }
                }
            }

            //increments score when AnimalTile has same species as one of the enclosures.
            //if no similar species were found, and at the same time there is an empty enclosure in list
            //the score will also increase
            for(animal in animalList){
                val enclosuresWithSameSpecies = notEmptyNorFullEnclosures.filter {
                    it.second.animalTiles[0].species == animal.species
                }

                if (enclosuresWithSameSpecies.isNotEmpty()){
                    score++
                }
                else if (emptyEnclosures.isNotEmpty()){
                    score++
                }
            }
            //TODO: Consider vending stalls for optimal take truck if there is some spare time
//            for(tile in vendingList){
//                for (enclosure in notEmptyNorFullEnclosures){
//                    if(enclosure.vendingStalls.size < enclosure.maxVendingStalls){
//                        score ++
//                    }
//                }
//            }
            if (maxScore <= score){
                maxScore = score
                chosenTruckPair = indexedTruckPair
            }
        }
        if (chosenTruckPair.second.tilesOnTruck.isNotEmpty()){
            moves.add(TakeTruckAndPlaceTiles(chosenTruckPair.first))
        }
        return moves
    }

    private fun determineAllMoneyMoves() : List<Move> {
        val possibleMoneyMoves = arrayListOf<Move>()

        val discardTileMoves = discardTileMoves()
        val exchangeAllTileMoves= determineExchangeAllTileMoves()
        val expandZoo = expandZooMove()
        val moveTileFromBarnToEnclosure = allMoveTileFromBarnToEnclosure()
        val moveVendingStallEnclosureToBarn = allMoveVendingStallEnclosureToBarn()
        val moveVendingStallEnclosureToEnclosure = allMoveVendingStallEnclosureToEnclosure()
        val purchaseTile = purchaseTileMoves()

        possibleMoneyMoves.addAll(discardTileMoves)
        possibleMoneyMoves.addAll(exchangeAllTileMoves)
        possibleMoneyMoves.addAll(expandZoo)
        possibleMoneyMoves.addAll(moveTileFromBarnToEnclosure)
        possibleMoneyMoves.addAll(moveVendingStallEnclosureToBarn)
        possibleMoneyMoves.addAll(moveVendingStallEnclosureToEnclosure)
        possibleMoneyMoves.addAll(purchaseTile)

        return possibleMoneyMoves
    }



    /**
     *function that determines whether Expand Zoo money action is feasible or not
     * @return Array list containing one move or no move at all depending on outcome
     * the move within the Array List is the ExpandZoo() move itself.
     *
     * TODO: check if two players or more are playing
     */
    fun expandZooMove() : ArrayList<Move> {
        val currentPlayer = currentGameStateCopy.players.peek()

        if(currentPlayer.coins < 3){
            return arrayListOf()
        }

        //Check if all enclosures are full
        var allEnclosuresAreFull = true
        for (enclosure in currentPlayer.playerEnclosure){
            val enclosureIsNotFull = enclosure.animalTiles.size < enclosure.maxAnimalSlots

            if (enclosureIsNotFull){
                allEnclosuresAreFull = false
            }
        }

        return if(allEnclosuresAreFull && currentGameStateCopy.players.size == 2 && currentPlayer.playerEnclosure.size < 5) {
            arrayListOf(ExpandZoo())
        } else if(allEnclosuresAreFull && currentGameStateCopy.players.size > 2 && currentPlayer.playerEnclosure.size < 4) {
            arrayListOf(ExpandZoo())
        } else{
            arrayListOf()
        }
    }

    /**
     * function that
     * @return list of all moves a player can make with "DiscardTile" action.
     */
    fun discardTileMoves(): ArrayList<Move>
    {
        val currentPlayer = currentGameStateCopy.players.peek()
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
        val discardableTiles = mutableSetOf<Tile>()

        if(currentPlayer.coins < 2){
            return discardableTiles
        }
        for(tile in currentPlayer.barn.animalTiles){
            discardableTiles.add(tile)
        }
        return discardableTiles
    }

    /**
     * function that returns a list of all move combination a player can make with "PurchaseTile" action
     */
    fun purchaseTileMoves(): ArrayList<Move>
    {
        val currentPlayer = currentGameStateCopy.players.peek()

        val combinations = purchasableOtherPlayerTiles(currentPlayer)
        val moveList = ArrayList<Move>()
        for ((player, purchasableTiles) in combinations) {
            for (tile in purchasableTiles) {
                TODO("CREATE MOVE INSTANCES WITH NEW PARAMETERS")
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
        val purchasableTiles : HashMap<Player, MutableSet<Tile>> = HashMap()
        val neededSpecies = mutableSetOf<Species>()


        if(currentPlayer.coins < 2){
            return purchasableTiles
        }

        //Initialize the MutableMap
        for(otherPlayer in currentGameStateCopy.players){
            if(currentPlayer != otherPlayer){
                purchasableTiles[otherPlayer] = HashSet()
            }
        }

        //Find all species the currentPlayer might need
        //Only add those where the enclosures are not full yet
        for(enclosure in currentPlayer.playerEnclosure){
            val enclosureIsNotFull = enclosure.animalTiles.size < enclosure.maxAnimalSlots
            if(enclosureIsNotFull){
                for (tile in enclosure.animalTiles){
                    neededSpecies.add(tile.species)
                }
            }
        }

        //Loop all other player's barn
        for(otherPlayer in currentGameStateCopy.players){
            if(otherPlayer == currentPlayer){
                continue
            }

            for (tile in otherPlayer.barn.animalTiles){
                if(tile.species in neededSpecies){
                    purchasableTiles[otherPlayer]?.add(tile)
                }
            }
        }

        return purchasableTiles
    }



    /**
     * function to determine all the possible moveTileFromBarnToEnclosure actions.
     * @return a map of each tile in the barn to a list of enclosures it can be moved to.
     */
    private fun possibleTileBarnToEnclosureMoves(): Map<Tile, List<Int>>{
        val possibleMoves : HashMap<Tile, List<Int>> = HashMap()
        val currentPlayer = currentGameStateCopy.players.peek()
        if (currentPlayer.coins < 1) {
            return possibleMoves
        }
        if (currentPlayer.barn.animalTiles.size == 0 && currentPlayer.barn.vendingStalls.size == 0){
            return possibleMoves
        }
        val playerEnclosures = currentPlayer.playerEnclosure
        for(animalTile in currentPlayer.barn.animalTiles){
            val enclosures: MutableList<Int> = mutableListOf()

            for(i in 0 until currentPlayer.playerEnclosure.size){
                val enclosure = playerEnclosures[i]
                if (enclosure.animalTiles.isEmpty() ) {
                    enclosures.add(i)
                }else if ((animalTile.species == enclosure.animalTiles[0].species &&
                            enclosure.animalTiles.size < enclosure.maxAnimalSlots)) {
                    enclosures.add(i)
                }
            }
            possibleMoves[animalTile] = enclosures
        }
        for(vendingTile in currentPlayer.barn.vendingStalls){
            val enclosures: MutableList<Int> = mutableListOf()
            for (i in 0 until currentPlayer.playerEnclosure.size){
                val enclosure = currentPlayer.playerEnclosure[i]
                if(enclosure.vendingStalls.size < enclosure.maxVendingStalls){
                    enclosures.add(i)
                }
            }
            possibleMoves[vendingTile] = enclosures
        }
        return possibleMoves
    }

    /**
     * function that returns all possible choices for moving a tile from the barn to the enclosure.
     */
    fun allMoveTileFromBarnToEnclosure(): List<Move>{
        val currentPlayer = currentGameStateCopy.players.peek()
        val combinations = possibleTileBarnToEnclosureMoves()
        val playerEnclosures = currentPlayer.playerEnclosure
        val moveList = ArrayList<Move>()
        val moveListAnimalTiles = ArrayList<Move>()
        val moveListVendingStalls = ArrayList<Move>()
        for((tile,enclosuresIndexes) in combinations){
            for(index in enclosuresIndexes){
                val enclosure = playerEnclosures[index]
                if(tile is Animal && enclosure.animalTiles.isNotEmpty()
                    && enclosure.animalTiles[0].species == tile.species
                    && enclosure.animalTiles.size < enclosure.maxAnimalSlots){
                    moveList.add(MoveTileFromBarnToEnclosure(index, tile))
                    break
                }
                else if(tile is Animal && enclosure.animalTiles.isEmpty()){
                    moveListAnimalTiles.add(MoveTileFromBarnToEnclosure(index,tile))
                }
                else if(tile is VendingStall && enclosure.vendingStalls.isEmpty()
                    && enclosure.animalTiles.isNotEmpty()){
                    moveList.add(MoveTileFromBarnToEnclosure(index, tile))
                    break
                }
                else if(tile is VendingStall && enclosure.vendingStalls.isEmpty()){
                    moveList.add(MoveTileFromBarnToEnclosure(index, tile))
                }
                else if(tile is VendingStall && enclosure.vendingStalls.isNotEmpty()
                    && enclosure.maxVendingStalls == 2
                    && enclosure.vendingStalls.size < enclosure.maxVendingStalls
                    && enclosure.vendingStalls[0].stall != tile.stall){
                    moveListVendingStalls.add(MoveTileFromBarnToEnclosure(index, tile))
                }
                else{
                    moveListVendingStalls.add(MoveTileFromBarnToEnclosure(index, tile))
                }
            }
        }
        if(moveList.isEmpty()){
            if(moveListVendingStalls.isNotEmpty()){
                moveList.add(moveListVendingStalls.first())
            }
            else if (moveListAnimalTiles.isNotEmpty()){
                moveList.add(moveListAnimalTiles.first())
            }
        }

        return moveList
    }
    /**
     * function to determine all the possible moveVendingStallEnclosureToBarn actions.
     * @return list of enclosures containing vending stalls.
     */
    private fun possibleVendingStallEnclosureToBarnMoves(): List<Int>{
        val possibleMoves: MutableList<Int> = mutableListOf()
        val currentPlayer = currentGameStateCopy.players.peek()
        val playerEnclosures = currentPlayer.playerEnclosure
        if(currentPlayer.coins < 1){
            return possibleMoves
        }
        for(i in 0 until playerEnclosures.size){
            val enclosure = playerEnclosures[i]
            if(enclosure.vendingStalls.size > 0){
                possibleMoves.add(i)
            }
        }
        return possibleMoves
    }

    /**
     * function that returns all the possible choices for moving a vending stall from an enclosure
     * to another enclosure
     */
    fun allMoveVendingStallEnclosureToEnclosure(): List<Move>{
        val combinations = possibleVendingStallEnclosureToEnclosureMoves()
        val moveList = ArrayList<Move>()
        for((sourceEnclosure, enclosures) in combinations){
            for(targetEnclosure in enclosures){
                moveList.add(MoveVendingStallEnclosureToEnclosure(sourceEnclosure, targetEnclosure))
            }
        }
        return moveList
    }

    /**
     * function to determine all the possible moveVendingStallEnclosureToEnclosure actions.
     * @return map of an enclosure containing a vending stall to a list of enclosures the vending
     * stall can be moved to.
     */
    private fun possibleVendingStallEnclosureToEnclosureMoves(): Map<Int, List<Int>>{
        val possibleMoves: HashMap<Int,List<Int>> = HashMap()
        val currentPlayer = currentGameStateCopy.players.peek()
        val playerEnclosures = currentPlayer.playerEnclosure
        if(currentPlayer.coins < 1){
            return possibleMoves
        }
        for(i in 0 until playerEnclosures.size){
            val sourceEnclosure = playerEnclosures[i]
            if(sourceEnclosure.vendingStalls.size > 0){
                val enclosures: MutableList<Int> = mutableListOf()
                for(j in 0 until playerEnclosures.size){
                    val targetEnclosure = playerEnclosures[j]
                    if (sourceEnclosure != targetEnclosure && targetEnclosure.vendingStalls.size < targetEnclosure.maxVendingStalls){
                        enclosures.add(j)
                    }
                }
                possibleMoves[i] = enclosures
            }
        }
        return possibleMoves
    }

    /**
     * function that returns all the possible choices for moving a vending stall from an enclosure to the barn.
     */
    fun allMoveVendingStallEnclosureToBarn(): List<Move>{
        val currentPlayer = currentGameStateCopy.players.peek()
        val playerEnclosures = currentPlayer.playerEnclosure
        val possibleEnclosures = possibleVendingStallEnclosureToBarnMoves()
        val moveList = ArrayList<Move>()
        for(index in possibleEnclosures){
            val enclosure = playerEnclosures[index]
            for(vendingStall in enclosure.vendingStalls){
                moveList.add(MoveVendingStallEnclosureToBarn(index, vendingStall))
            }
        }
        return moveList
    }

    /**
     * Function to determine all possible "Exchange All Tiles" Moves (either from barn to enclosure or
     * from enclosure to enclosure)
     *
     * @return List of possible "Exchange All Tiles" Moves
     */
    fun determineExchangeAllTileMoves() : List<Move>{
        val currentPlayer = currentGameStateCopy.players.peek()

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
    private fun exchangeAllTilesCombinationsToMoves(combinations: Map<Int, List<Int>>,
                                                    currentPlayer: Player): ArrayList<Move>
    {
        val moveList = ArrayList<Move>()

        for ((sourceEnclosure, targetList) in combinations) {
            for (targetEnclosure in targetList) {
                if (sourceEnclosure == 6) { //Might be unnecessary
                    moveList.addAll(createMovesForEachSpeciesInBarn(targetEnclosure, currentPlayer))
                } else if (targetEnclosure == 6) {
                    moveList.addAll(createMovesForEachSpeciesInBarn(sourceEnclosure, currentPlayer))
                } else {
                    moveList.add(ExchangeAllTilesEnclosureToEnclosure(sourceEnclosure, targetEnclosure))
                }
            }
        }

         return moveList
    }

    /**
     * Function to group the barn by its containing species and creating [ExchangeAllTilesBarnToEnclosure]
     * moves
     *
     * @param enclosureIndex the source enclosure
     * @param player the player containing the barn
     */
    private fun createMovesForEachSpeciesInBarn(enclosureIndex: Int,
                                                player: Player): ArrayList<ExchangeAllTilesBarnToEnclosure>{
        val currentPlayer = currentGameStateCopy.players.peek()
        val playerEnclosures = currentPlayer.playerEnclosure
        val speciesMap = player.barn.animalTiles.groupBy { it.species }

        val outList = arrayListOf<ExchangeAllTilesBarnToEnclosure>()

        for ((species, tiles) in speciesMap){
            val enclosure = playerEnclosures[enclosureIndex]
            if(tiles.size <= enclosure.maxAnimalSlots && enclosure.animalTiles[0].species != species){
                outList.add(ExchangeAllTilesBarnToEnclosure(enclosureIndex, species))
            }

        }
        return outList
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
    private fun determineSwapCombinations(player: Player) : Map<Int, List<Int>>{
        val combinations : HashMap<Int, List<Int>> = HashMap()

        //Create a new list to union playerEnclosure and barn, without modifying playerEnclosure directly
        val playerEnclosureAndBarn = ArrayList(player.playerEnclosure)
        playerEnclosureAndBarn.add(player.barn)

        for (j in 0 until playerEnclosureAndBarn.size){
            //Enclosure of the current iteration
            val sourceEnclosure : Enclosure = playerEnclosureAndBarn[j]
            if(sourceEnclosure.animalTiles.isEmpty()){
                continue
            }
            val swapTargets = ArrayList<Int>()

            //Now find all possible swaps
            for(k in j + 1 until playerEnclosureAndBarn.size){
                //k might be bigger or equal
                if(k >= playerEnclosureAndBarn.size){
                    break
                }

                val targetEnclosure : Enclosure = playerEnclosureAndBarn[k]
                if(targetEnclosure.animalTiles.isEmpty()){
                    continue
                }

                if(eligibleToSwap(sourceEnclosure, targetEnclosure)){
                    if(!targetEnclosure.isBarn){
                        swapTargets.add(k)
                    }
                    else {
                        swapTargets.add(6)
                    }

                }
            }

            //Create a new entry in the map
            combinations[j] = swapTargets
        }

        return combinations
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
        val targetEnclosureHasEnoughSlots = if (source.isBarn) true
        else target.maxAnimalSlots >= source.animalTiles.size
        val sourceEnclosureHasEnoughSlots = if (target.isBarn) true
        else source.maxAnimalSlots >= target.animalTiles.size

        var sameAnimalSpecies = false

        if(!source.isBarn && ! target.isBarn){
            val sourceSpecies = source.animalTiles[0].species
            val targetSpecies = target.animalTiles[0].species

            sameAnimalSpecies = sourceSpecies == targetSpecies
        }


        return targetEnclosureHasEnoughSlots && sourceEnclosureHasEnoughSlots && !sameAnimalSpecies
    }
}

