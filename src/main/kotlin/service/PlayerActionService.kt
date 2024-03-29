package service

import entity.*

/**
 * Service layer class that provides the logic of all player moves in zooloretto game.
 *
 * @param rootService an Object from [RootService] class
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * this method implements the add tile move in zooloretto game.
     * First we check if roundDisc is true, if so then we draw the tile from endStack
     * , but if roundDisc is false then we draw the tile from drawStack.
     * We have to note here if the drawStack is empty and roundDisc is still false,
     * so we put roundDisc true and draw one tile from the endStack.
     * At the end we put that tile on one of the trucks.
     *
     * @param truck the truck that the player wants to put the tile on it
     */
    fun addTile(indexTruck : Int){
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = zooGame.currentGameState
        var player = game.players.peek()

        require(game.deliveryTrucks.isNotEmpty()) {"There are no trucks!"}
        require(indexTruck in 0 until game.deliveryTrucks.size) {"wrong index!"}
        val truck = game.deliveryTrucks[indexTruck]
        require(!player.passed) {"this player has passed!"}
        require(game.tileStack.endStack.isNotEmpty()) {"There are no more tiles!"}
        require(truck.tilesOnTruck.size < truck.maxSize) {"This truck is full!"}

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        zooGame.undoStack.add(copyCurrentGame)

        val tile : Tile
        if(game.roundDisc) {
            tile = game.tileStack.endStack.removeFirst()
        }
        else if(game.tileStack.drawStack.isNotEmpty()) {
            tile = game.tileStack.drawStack.removeFirst()
        }
        else {
            game.roundDisc = true
            tile = game.tileStack.endStack.removeFirst()
        }
        truck.tilesOnTruck.add(tile)
        player = game.players.poll()
        game.players.add(player)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements moving one tile from barn to enclosure.
     * It is important to know that if the enclosure is full after moving the tile,
     * then the player receives one coin, or if to animals have a new child and the
     * enclosure is full then receives the player one coin.
     *
     * @param source the player, so that we have access to the barn of the player
     * @param destination the enclosure that we want to move one tile to it
     * @param tile the to move tile
     */
    fun moveOneTile(indexEnclosure: Int, tile: Tile) {
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = zooGame.currentGameState
        var player = game.players.peek()

        require(!player.passed) {"this player has passed!"}
        require(indexEnclosure in 0 until player.playerEnclosure.size) {"wrong index!"}
        val enclosure = player.playerEnclosure[indexEnclosure]
        require(player.coins >= 1) {"You don't have enough coins!"}

        if(tile is VendingStall) {
            require(player.barn.vendingStalls.contains(tile))
            require(enclosure.vendingStalls.size < enclosure.maxVendingStalls) {"There is no space!"}

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            enclosure.vendingStalls.add(tile)
            player.barn.vendingStalls.remove(tile)
        }
        else if (tile is Animal){
            require(player.barn.animalTiles.contains(tile))
            require(enclosure.animalTiles.size < enclosure.maxAnimalSlots) {"There is no space!"}
            if (enclosure.animalTiles.isNotEmpty()) {
                require(enclosure.animalTiles[0].species == tile.species) {"Different Species!"}
            }

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            enclosure.animalTiles.add(tile)
            player.barn.animalTiles.remove(tile)

            if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
                bonusCoins(game, player, enclosure)
            }
            checkAndAddNewBaby(game, player, tile, enclosure)
        }
        player.coins--
        game.bank++
        player = game.players.poll()
        game.players.add(player)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements moving one vending stall from enclosure to enclosure.
     *
     * @param source the enclosure, that we want to move the vending stall from it
     * @param destination the enclosure, that we want to move the vending stall to it
     */
    fun moveVendingStall(indexSource: Int, indexDestination: Int) {
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(!player.passed) { "this player has passed!" }
        require(player.coins >= 1) { "You don't have enough coins!" }
        require(indexSource in 0 until player.playerEnclosure.size) {"wrong index!"}
        require(indexDestination in 0 until player.playerEnclosure.size) {"wrong index!"}
        require(indexSource != indexDestination)
        val source = player.playerEnclosure[indexSource]
        val destination = player.playerEnclosure[indexDestination]
        require(source.vendingStalls.isNotEmpty()) { "You don't have vending stalls in this enclosure!" }
        require(destination.vendingStalls.size < destination.maxVendingStalls) { "This enclosure is full!" }

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        zooGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()
        destination.vendingStalls.add(source.vendingStalls[0])
        source.vendingStalls.removeFirst()
        player.coins--
        game.bank++
        game.players.add(player)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements moving one tile from enclosure to barn
     *
     * @param source the enclosure, that we want to move the tile from it
     * @param destination the player, so that we have access to the barn
     * @param tile the moving tile, if it's vending stall or an animal
     */
    fun moveTileFromEnclosureToBarn(indexEnclosure: Int, tile: Tile) {

        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = zooGame.currentGameState
        var player = game.players.peek()

        require(player.coins >= 1) {"You don't have enough coins!"}
        require(!player.passed) {"this player has passed!"}
        require(indexEnclosure in 0 until player.playerEnclosure.size) {"wrong index!"}
        val source = player.playerEnclosure[indexEnclosure]
        require(!source.isBarn) {"From Enclosure to barn!"}

        if (tile is VendingStall) {
            require(source.vendingStalls.contains(tile))

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            source.vendingStalls.remove(tile)
            player.barn.vendingStalls.add(tile)

        }
        else if (tile is Animal) {
            require(source.animalTiles.contains(tile))

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            source.animalTiles.remove(tile)
            player.barn.animalTiles.add(tile)
        }
        player.coins--
        game.bank++
        player = game.players.poll()
        game.players.add(player)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements expanding the zoo of the player.
     * the player loses three coins and becomes one more enclosure to his own zoo.
     */
    fun expandZoo() {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        check(!player.passed) { "this player has passed!" }
        require(player.coins >= 3) { "You don't have enough coins!" }

        if (player.playerEnclosure.size == 5 || (player.playerEnclosure.size == 4 && game.players.size > 2)) {
            throw IllegalArgumentException("\"the player can't expand the zoo!!\"")
        }
        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        zooGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()
        player.playerEnclosure.add(Enclosure(5, 1, 1, Pair(9, 5), false))
        player.coins -= 3
        game.bank += 3
        game.players.add(player)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements taking one truck from the delivery truck in the game.
     *
     * @param truck the taken truck from the game
     */
    fun takeTruck(indexTruck: Int) {

        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = zooGame.currentGameState
        val player = game.players.peek()

        require(!player.passed) { "this player has passed!" }
        require(game.deliveryTrucks.isNotEmpty()) {"there are no trucks!"}
        require(indexTruck in 0 until game.deliveryTrucks.size) {"wrong index!"}
        val truck = game.deliveryTrucks[indexTruck]
        require(truck.maxSize in 1..3 && truck.tilesOnTruck.isNotEmpty())

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        zooGame.undoStack.add(copyCurrentGame)

        player.chosenTruck = truck
        player.passed = true
        truck.tilesOnTruck.forEach {
            if (it is Coin) {
                player.coins++
            }
        }
        val tempList = arrayListOf<Tile>()
        tempList.addAll(truck.tilesOnTruck)
        truck.tilesOnTruck.clear()
        tempList.forEach {
            if (it is Animal || it is VendingStall) {
                truck.tilesOnTruck.add(it)
            }
        }
        game.deliveryTrucks.remove(truck)

        zooGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method is done right after [takeTruck], if the player wants to put the first
     * tile on the truck in one of his enclosures.
     * The tile will be putted in the enclosure and the map (tile to enclosures) will be refreshed.
     *
     * @param destination the enclosure, that the player wants to put the tile on it
     */
    fun placeTileFromTruck(indexEnclosure: Int) {
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = zooGame.currentGameState
        var player = game.players.peek()
        val truck = player.chosenTruck
        checkNotNull(truck)

        require(player.passed) {"this player didn't take a truck!"}
        require(truck.tilesOnTruck.isNotEmpty()) {"the truck is empty!"}
        require(indexEnclosure in 0 until player.playerEnclosure.size) {"wrong index!"}
        val destination = player.playerEnclosure[indexEnclosure]
        val tile = truck.tilesOnTruck[0]

        if (tile is VendingStall) {
            require(destination.vendingStalls.size < destination.maxVendingStalls) {"there is no place free!"}

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            destination.vendingStalls.add(tile)

        } else if (tile is Animal) {
            require(destination.animalTiles.size < destination.maxAnimalSlots) {"there is no place free!"}
            if (destination.animalTiles.isNotEmpty()) {
                require(destination.animalTiles[0].species == tile.species) {"there are different species!"}
            }

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            destination.animalTiles.add(tile)
            if (destination.animalTiles.size == destination.maxAnimalSlots) {
                bonusCoins(game, player, destination)
            }
            checkAndAddNewBaby(game, player, tile, destination)
        }
        truck.tilesOnTruck.remove(tile)
        if (truck.tilesOnTruck.isEmpty()) {
            checkIfRoundEnds(game)
            //when the round isn't over yet, then the player still passed.
            if (player.passed) {
                player = game.players.poll()
                game.players.add(player)
                zooGame.redoStack.clear()
                nextPlayer(game)
            }
        }
        zooGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method is done right after [takeTruck] and the next tile will be putted in the barn
     *
     * @param destination the player, that we have access to his barn
     */
    fun placeTileFromTruck() {
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = zooGame.currentGameState
        var player = game.players.peek()
        val truck = player.chosenTruck
        checkNotNull(truck)

        require(player.passed) {"this player didn't take a truck!"}
        require(truck.tilesOnTruck.isNotEmpty()) {"there are no tiles on the truck!"}
        val tile = truck.tilesOnTruck[0]

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        zooGame.undoStack.add(copyCurrentGame)

        if (tile is VendingStall) {
            player.barn.vendingStalls.add(tile)
        }
        else if (tile is Animal) {
            player.barn.animalTiles.add(tile)
        }
        truck.tilesOnTruck.remove(tile)
        if (truck.tilesOnTruck.isEmpty()) {
            checkIfRoundEnds(game)
            //when the round isn't over yet, then the player still passed.
            if (player.passed) {
                player = game.players.poll()
                game.players.add(player)
                zooGame.redoStack.clear()
                nextPlayer(game)
            }
        }
        zooGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method implements exchanging two types of animals between one enclosure and the barn.
     * During the exchange can a child be fathered, so we have to note this case.
     * if the child can be filled in the enclosure then it goes to enclosure, if not, then
     * it goes to the barn.
     * We have to notice that in exchanging actions no credit points are given for the player,
     * if the enclosure will be full after the exchange.
     *
     * @param source the enclosure, that we want to exchange his animals
     * @param destination the player, so that we have access to his barn
     * @param animal the animal that we want to exchange from the barn
     */
    fun exchangeAllTiles(indexEnclosure: Int, animal: Animal) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        require(player.coins >= 1) { "You don't have enough coins!" }
        check(!player.passed) { "this player has passed!" }
        require(indexEnclosure in 0 until player.playerEnclosure.size) {"wrong index!"}
        val source = player.playerEnclosure[indexEnclosure]
        require(source.animalTiles.isNotEmpty()) { "This enclosure is empty!" }
        check(player.barn.animalTiles.contains(animal))
        require(source.animalTiles[0].species != animal.species) { "The animal types must be different!" }

        val tempList: ArrayList<Animal> = arrayListOf()
        player.barn.animalTiles.forEach {
            if (it.species == animal.species) {
                tempList.add(it)
            }
        }
        check(tempList.size <= source.maxAnimalSlots) { "This enclosure is too small!" }

       // val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
       // zooGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()
        player.coins--
        game.bank++
        player.barn.animalTiles.removeAll(tempList.toSet())
        player.barn.animalTiles.addAll(source.animalTiles)
        source.animalTiles.clear()
        source.animalTiles.addAll(tempList)
        checkAndAddNewBabyWithoutBonusCoins(player, source)
        game.players.add(player)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements exchanging two types of animals between two enclosures.
     * We have to notice that in exchanging actions no credit points are given for the player,
     * if one enclosure will be full after the exchange.
     *
     * @param source the first enclosure
     * @param destination the second enclosure
     */
    fun exchangeAllTiles(indexSource: Int, indexDestination: Int) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        require(player.coins >= 1) { "You don't have enough coins!" }
        require(!player.passed) { "this player has passed!" }
        require(indexSource in 0 until player.playerEnclosure.size) {"wrong index!"}
        require(indexDestination in 0 until player.playerEnclosure.size) {"wrong index!"}
        require(indexSource != indexDestination) {"same enclosure!"}
        val source = player.playerEnclosure[indexSource]
        val destination = player.playerEnclosure[indexDestination]
        require(source.animalTiles.isNotEmpty() && destination.animalTiles.isNotEmpty()) { "Enclosure is full" }
        check(source.animalTiles[0].species != destination.animalTiles[0].species) { "same species!!" }
        check(source.animalTiles.size <= destination.maxAnimalSlots &&
                destination.animalTiles.size <= source.maxAnimalSlots)

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        zooGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()

        val tempAnimalTiles: ArrayList<Animal> = arrayListOf()
        tempAnimalTiles.addAll(source.animalTiles)
        source.animalTiles.clear()
        source.animalTiles.addAll(destination.animalTiles)
        destination.animalTiles.clear()
        destination.animalTiles.addAll(tempAnimalTiles)

        player.coins--
        game.bank++
        game.players.add(player)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements the purchase tile move, when a player buys a tile from another player's barn.
     *
     * @param player the other player, who the player wants to buy a tile from his barn
     * @param tile the tile, that the player wants to buy
     * @param destination the enclosure, that the player wants to put this tile in it
     */
    fun purchaseTile(player: Player, tile: Tile, indexEnclosure: Int) {
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = zooGame.currentGameState
        var currentPlayer = game.players.peek()

        require(currentPlayer.coins >= 2) { "You don't have enough coins!" }
        require(!player.passed) { "this player has passed!" }
        check(game.players.contains(player))
        require(indexEnclosure in 0 until player.playerEnclosure.size) {"wrong index!"}
        val destination = player.playerEnclosure[indexEnclosure]

        if (tile is VendingStall) {
            require(player.barn.vendingStalls.contains(tile))
            require(destination.vendingStalls.size < destination.maxVendingStalls)

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            destination.vendingStalls.add(tile)
            player.barn.vendingStalls.remove(tile)
            game.bank++
        }
        else if (tile is Animal) {
            require(player.barn.animalTiles.contains(tile))
            require(destination.animalTiles.size < destination.maxAnimalSlots) {"there is no space!"}
            if (destination.animalTiles.isNotEmpty()) {
                require(destination.animalTiles[0].species == tile.species)
            }
            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            destination.animalTiles.add(tile)
            player.barn.animalTiles.remove(tile)
            game.bank++
            if (destination.animalTiles.size == destination.maxAnimalSlots) {
                bonusCoins(game, currentPlayer, destination)
            }
            checkAndAddNewBaby(game, currentPlayer, tile, destination)
        }
        currentPlayer.coins -= 2
        player.coins++
        currentPlayer = game.players.poll()
        game.players.add(currentPlayer)

        zooGame.redoStack.clear()
        nextPlayer(game)
    }

    /**
     * this method implements the discard move, when a player chooses to discard a tile from his barn
     * out of the game.
     *
     * @param target the tile, that the player wants to discard
     */
    fun discardTile(target: Tile) {
        val zooGame = rootService.zoolorettoGame
        checkNotNull(zooGame)
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(player.coins >= 2) { "You don't have enough coins!" }
        require(!player.passed) { "this player has passed!" }

        if (target is VendingStall) {
            check(player.barn.vendingStalls.contains(target))
            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            player = game.players.poll()
            player.barn.vendingStalls.remove(target)
            player.coins -= 2
            game.bank += 2
            game.players.add(player)

            zooGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        } else if (target is Animal) {
            check(player.barn.animalTiles.contains(target))
            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            zooGame.undoStack.add(copyCurrentGame)

            player = game.players.poll()
            player.barn.animalTiles.remove(target)
            player.coins -= 2
            game.bank += 2
            game.players.add(player)

            zooGame.redoStack.clear()
            nextPlayer(game)
        }
    }

    /**
     * this method gives a hint to the player and shows him what he could play
     *
     * @return the showing message as a String
     */
    fun showHint(): String {
        return ""
    }

    /**
     * this method checks if an animal can become a new baby in a specified enclosure.
     *
     * @param animal the animal, that we want to check if it becomes a new child
     * @param enclosure the enclosure, that we want to check in it
     *
     * @return true if animal becomes a child, otherwise false
     */
    private fun checkForNewBaby(animal: Animal, enclosure: Enclosure): Boolean {
        if (animal.hasChild || animal.type == Type.OFFSPRING || animal.type == Type.NONE) {
            return false
        }
        enclosure.animalTiles.forEach {
            if (!it.hasChild && it.type != Type.NONE && it.type != Type.OFFSPRING) {
                if (animal.type != it.type) {
                    animal.hasChild = true
                    it.hasChild = true
                    return true
                }
            }
        }
        return false
    }

    /**
     * this method checks if a player becomes bonus coins, when he fills an enclosure.
     * It depends, how many coins there are in the bank.
     *
     * @param game the zooloretto game, so that we know how many coins there are in the bank
     * @param player the player, so that he becomes his bonus coins
     * @param enclosure the enclosure, that we want to check if it is full
     */
    private fun bonusCoins(game: ZoolorettoGameState, player: Player, enclosure: Enclosure) {
        if (game.bank >= enclosure.bonusCoins) {
            player.coins += enclosure.bonusCoins
            game.bank -= enclosure.bonusCoins
        }
        else {
            player.coins += game.bank
            game.bank = 0
        }
    }

    /**
     * this method checks if an animal becomes a child and if the player becomes bonus coins.
     *
     * @param game so that we have access to the bank for bonus coins
     * @param player to give the player bonus coins and to get access to the barn
     * @param animal that we want to check if it becomes a baby
     * @param enclosure that we want to check in it
     */
    private fun checkAndAddNewBaby(game: ZoolorettoGameState, player: Player, animal: Animal, enclosure: Enclosure) {
        if (checkForNewBaby(animal, enclosure)) {
            if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
                player.barn.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            } else if (enclosure.animalTiles.size + 1 == enclosure.maxAnimalSlots) {
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
                bonusCoins(game, player, enclosure)
            } else {
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
            rootService.zoolorettoGameService.offspringTiles.remove(Animal(Type.OFFSPRING, animal.species))
        }
    }

    /**
     * this method checks if an animal in an enclosure becomes a baby based on an exchanging action,
     * so that the player doesn't receive any bonus coins.
     *
     * @param player so that we have access to his barn, when the baby has to go to the barn
     * @param animal the animal, that we want to check, if it becomes a baby or not
     * @param enclosure the enclosure, that we want to check in it
     */
    private fun checkAndAddNewBabyWithoutBonusCoins(player: Player, enclosure: Enclosure) {
        val tempList = arrayListOf<Animal>()
        enclosure.animalTiles.forEach {
            if (checkForNewBaby(it, enclosure)) {
                tempList.add(Animal(Type.OFFSPRING, it.species))
            }
        }
        tempList.forEach {
            if (enclosure.animalTiles.size < enclosure.maxAnimalSlots) {
                enclosure.animalTiles.add(it)
            }
            else if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
                player.barn.animalTiles.add(it)
            }
        }
        rootService.zoolorettoGameService.offspringTiles.removeAll(tempList.toSet())
    }

    /**
     * this method checks if the round ends.
     * When roundDisc is true, then ends the game, when the round is over.
     * That means that each player has taken a truck and passed,
     * otherwise the round ends and each player gives his truck to the delivery trucks
     * and a new round will be started.
     *
     * @param game to get access to the players in the game
     */
    private fun checkIfRoundEnds(game: ZoolorettoGameState) {

        if (game.deliveryTrucks.isEmpty()) {
            if (game.roundDisc) {
                rootService.zoolorettoGameService.endGame()
            } else {
                game.players.forEach {
                    it.passed = false
                    game.deliveryTrucks.add(it.chosenTruck!!)
                }
            }
        }
    }

    private fun nextPlayer(game: ZoolorettoGameState) {
        var size = game.players.size
        while (size-- > 0) {
            if (game.players.peek().passed) {
                val player = game.players.poll()
                game.players.add(player)
            } else break
        }
        onAllRefreshables { refreshAfterPlayerAction() }
    }
}