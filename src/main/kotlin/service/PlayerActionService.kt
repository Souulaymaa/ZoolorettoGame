package service

import entity.*

/**
 * Service layer class that provides the logic of all player moves in zooloretto game.
 *
 * @param rootService an Object from [RootService] class
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {
    private val tileToEnclosuresMap = mutableMapOf<Tile, List<Enclosure>>()
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
    fun addTile(truck: DeliveryTruck){
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(game.deliveryTrucks.contains(truck)) {"wrong truck!"}
        check(!player.passed) {"this player has passed!"}

        require(game.tileStack.endStack.isNotEmpty()) {"There are no more tiles!"}
        require(truck.tilesOnTruck.size < 3) {"This truck is full!"}

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        if(game.roundDisc) {
            player = game.players.poll()
            val tile = game.tileStack.endStack.removeFirst()
            truck.tilesOnTruck.add(tile)
            game.players.add(player)
        }
        else if(game.tileStack.drawStack.isNotEmpty()) {
            player = game.players.poll()
            val tile = game.tileStack.drawStack.removeFirst()
            truck.tilesOnTruck.add(tile)
            game.players.add(player)
        }
        else {
            player = game.players.poll()
            val tile = game.tileStack.endStack.removeFirst()
            truck.tilesOnTruck.add(tile)
            game.players.add(player)
            game.roundDisc = true
        }
        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
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
    fun moveOneTile(source: Player, destination: Enclosure, tile: Tile) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(!player.passed) {"this player has passed!"}
        require(player.coins >= 1) {"You don't have enough coins!"}
        check(player.playerEnclosure.contains(destination))
        check(player.equals(source))
        check(player.barn.animalTiles.contains(tile) || player.barn.vendingStalls.contains(tile))

        if(tile is VendingStall) {
            require(destination.vendingStalls.size < destination.maxVendingStalls) {"There is no space!"}
            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

            player = game.players.poll()
            destination.vendingStalls.add(tile)
            player.barn.vendingStalls.remove(tile)
            player.coins--
            game.bank++
            game.players.add(player)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        else if (tile is Animal){
            require(destination.animalTiles.size < destination.maxAnimalSlots) {"There is no space!"}
            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

            if (destination.animalTiles.size == 0) {
                player = game.players.poll()
                destination.animalTiles.add(tile)
                player.barn.animalTiles.remove(tile)
                player.coins--
                game.players.add(player)
            }
            else {
                check(tile.species == destination.animalTiles[0].species)
                player = game.players.poll()
                destination.animalTiles.add(tile)
                if (destination.animalTiles.size == destination.maxAnimalSlots) {
                    bonusCoins(game, player, destination)
                }
                checkAndAddNewBaby(game, player, tile, destination)
                player.barn.animalTiles.remove(tile)
                player.coins--
                game.players.add(player)
            }

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
    }

    /**
     * this method implements moving one vending stall from enclosure to enclosure.
     *
     * @param source the enclosure, that we want to move the vending stall from it
     * @param destination the enclosure, that we want to move the vending stall to it
     */
    fun moveVendingStall(source: Enclosure, destination: Enclosure) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(!player.passed) {"this player has passed!"}
        check(player.coins >= 1) {"You don't have enough coins!"}
        check(player.playerEnclosure.contains(source) && player.playerEnclosure.contains(destination))
        check(source.vendingStalls.isNotEmpty()) {"You don't have vending stalls in this enclosure!"}
        check(destination.vendingStalls.size != destination.maxVendingStalls) {"This enclosure is full!"}

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()
        destination.vendingStalls.add(source.vendingStalls[0])
        source.vendingStalls.removeFirst()
        player.coins--
        game.bank++
        game.players.add(player)

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method implements moving one tile from enclosure to barn
     *
     * @param source the enclosure, that we want to move the tile from it
     * @param destination the player, so that we have access to the barn
     * @param tile the moving tile, if it's vending stall or an animal
     */
    fun moveTileFromEnclosureToBarn(source: Enclosure, destination: Player, tile: Tile) {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(player.coins >= 1) {"You don't have enough coins!"}
        check(!player.passed) {"this player has passed!"}
        check(!source.isBarn)
        check(player.equals(destination))
        check(player.playerEnclosure.contains(source))

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        if (tile is VendingStall) {
            check(source.vendingStalls.contains(tile))
            player = game.players.poll()
            source.vendingStalls.remove(tile)
            player.barn.vendingStalls.add(tile)
            player.coins--
            game.bank++
            game.players.add(player)
        }
        else if (tile is Animal) {
            check(source.animalTiles.contains(tile))
            player = game.players.poll()
            source.animalTiles.remove(tile)
            player.barn.animalTiles.add(tile)
            player.coins--
            game.bank++
            game.players.add(player)
        }

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method implements expanding the zoo of the player.
     * the player loses three coins and becomes one more enclosure to his own zoo.
     */
    fun expandZoo() {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(!player.passed) {"this player has passed!"}
        require(player.coins >= 3) {"You don't have enough coins!"}

        if(player.playerEnclosure.size == 5 || (player.playerEnclosure.size == 4 && game.players.size > 2)) {
            println("the player can't expand the zoo!!")
            return
        }
        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()
        player.playerEnclosure.add(Enclosure(5, 1, 0, Pair(9,5), false))
        player.coins -= 3
        game.bank += 3
        game.players.add(player)

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method implements taking one truck from the delivery truck in the game.
     *
     * @param truck the taken truck from the game
     *
     * @return a map of tile to list of enclosures, that the tile can go to it
     */
    fun takeTruck(truck: DeliveryTruck) : Map<Tile, List<Enclosure>> {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(game.deliveryTrucks.contains(truck))
        require(truck.size in 1..3 && truck.tilesOnTruck.isNotEmpty())
        require(!player.passed) {"this player has passed!"}

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        if(game.deliveryTrucks.size == 1) {
            player.chosenTruck = truck
            game.deliveryTrucks.remove(truck)
            truck.tilesOnTruck.forEach {
                when (it) {
                    is Coin -> player.coins++
                    else -> {
                        setTileToEnclosures(it, player)
                    }
                }
            }
            if (!game.roundDisc) {
                game.players.forEach {
                    it.passed = false
                }
            }
            else {
                player.passed = true
            }
        }
        else {
            player = game.players.poll()
            player.chosenTruck = truck
            player.passed = true
            game.deliveryTrucks.remove(truck)
            truck.tilesOnTruck.forEach {
                when (it) {
                    is Coin -> player.coins++
                    else -> {
                        setTileToEnclosures(it, player)
                    }
                }
            }
            game.players.add(player)
        }

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
        return tileToEnclosuresMap
    }

    /**
     * this method is done right after [takeTruck], if the player wants to put the first
     * tile on the truck in one of his enclosures.
     * The tile will be putted in the enclosure and the map (tile to enclosures) will be refreshed.
     *
     * @param destination the enclosure, that the player wants to put the tile on it
     *
     * @return map (tile to enclosures) after refreshing
     */
    fun placeTileFromTruck(destination: Enclosure) : Map<Tile, List<Enclosure>> {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val truck = player.chosenTruck

        require(player.playerEnclosure.contains(destination))
        checkNotNull(truck)
        val tile = truck.tilesOnTruck[0]
        require(tileToEnclosuresMap[tile]!!.contains(destination))

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        if(truck.tilesOnTruck.size == 1) {
            player = game.players.poll()

            if (tile is VendingStall) {
                destination.vendingStalls.add(tile)
                tileToEnclosuresMap.remove(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            else if (tile is Animal) {
                destination.animalTiles.add(tile)
                if (destination.animalTiles.size == destination.maxAnimalSlots) {
                    bonusCoins(game, player, destination)
                }
                checkAndAddNewBaby(game, player, tile, destination)
                tileToEnclosuresMap.remove(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            truck.tilesOnTruck.removeFirst()
            game.players.add(player)
            checkIfGameEnds(game)
        }
        else {
            if (tile is VendingStall) {
                destination.vendingStalls.add(tile)
                tileToEnclosuresMap.remove(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            else if (tile is Animal) {
                destination.animalTiles.add(tile)
                if (destination.animalTiles.size == destination.maxAnimalSlots) {
                    bonusCoins(game, player, destination)
                }
                checkAndAddNewBaby(game, player, tile, destination)
                tileToEnclosuresMap.remove(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            truck.tilesOnTruck.removeFirst()
        }
        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
        return tileToEnclosuresMap
    }

    /**
     * this method is done right after [takeTruck] and the next tile will be putted in the barn
     *
     * @param destination the player, that we have access to his barn
     *
     * @return map (tile to enclosures) after refreshing
     */
    fun placeTileFromTruck(destination: Player) : Map<Tile, List<Enclosure>> {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val truck = player.chosenTruck

        require(player.equals(destination))
        checkNotNull(truck)
        val tile = truck.tilesOnTruck[0]

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        if(truck.tilesOnTruck.size == 1) {
            player = game.players.poll()

            if (tile is VendingStall) {
                player.barn.vendingStalls.add(tile)
            }
            else if (tile is Animal) {
                player.barn.animalTiles.add(tile)
            }
            truck.tilesOnTruck.removeFirst()
            tileToEnclosuresMap.remove(tile)
            game.players.add(player)
            checkIfGameEnds(game)
        }
        else {
            if (tile is VendingStall) {
                player.barn.vendingStalls.add(tile)
            }
            else if (tile is Animal) {
                player.barn.animalTiles.add(tile)
            }
            truck.tilesOnTruck.removeFirst()
            tileToEnclosuresMap.remove(tile)
        }

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
        return tileToEnclosuresMap
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
    fun exchangeAllTiles(source: Enclosure, destination: Player, animal: Animal) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(player.coins >= 1) {"You don't have enough coins!"}
        check(!player.passed) {"this player has passed!"}
        check(player.playerEnclosure.contains(source))
        check(player.equals(destination))
        require(source.animalTiles.isNotEmpty()) {"This enclosure is empty!"}
        check(player.barn.animalTiles.contains(animal))
        require(source.animalTiles[0].species != animal.species) {"The animal types must be different!"}

        val tempList: ArrayList<Animal> = arrayListOf()
        player.barn.animalTiles.forEach {
            if (it.species == animal.species) {
                tempList.add(it)
            }
        }
        check(tempList.size <= source.maxAnimalSlots) {"This enclosure is too small!"}

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()
        player.coins--
        player.barn.animalTiles.removeAll(tempList.toSet())
        player.barn.animalTiles.addAll(source.animalTiles)
        source.animalTiles.clear()
        source.animalTiles.addAll(tempList)

        source.animalTiles.forEach {
            checkAndAddNewBabyWithoutBonusCoins(player, it, source)
        }
        game.players.add(player)

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method implements exchanging two types of animals between two enclosures.
     * We have to notice that in exchanging actions no credit points are given for the player,
     * if one enclosure will be full after the exchange.
     *
     * @param source the first enclosure
     * @param destination the second enclosure
     */
    fun exchangeAllTiles(source: Enclosure, destination: Enclosure) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(player.coins >= 1) {"You don't have enough coins!"}
        require(!player.passed) {"this player has passed!"}
        check(player.playerEnclosure.contains(source) && player.playerEnclosure.contains(destination))
        require(source.animalTiles.isNotEmpty() && destination.animalTiles.isNotEmpty()){"Enclosure is full"}
        check(source.animalTiles[0].species != destination.animalTiles[0].species) {"same species!!"}
        check(source.animalTiles.size <= destination.maxAnimalSlots &&
              destination.animalTiles.size <= source.maxAnimalSlots)

        val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()

        val tempAnimalTiles: ArrayList<Animal> = arrayListOf()
        tempAnimalTiles.addAll(source.animalTiles)
        source.animalTiles.clear()
        source.animalTiles.addAll(destination.animalTiles)
        destination.animalTiles.clear()
        destination.animalTiles.addAll(tempAnimalTiles)

        source.animalTiles.forEach {
            checkAndAddNewBabyWithoutBonusCoins(player, it, source)
        }
        destination.animalTiles.forEach {
            checkAndAddNewBabyWithoutBonusCoins(player, it, destination)
        }

        player.coins--
        game.players.add(player)

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method implements the purchase tile move, when a player buys a tile from another player's barn.
     *
     * @param player the other player, who the player wants to buy a tile from his barn
     * @param tile the tile, that the player wants to buy
     * @param destination the enclosure, that the player wants to put this tile in it
     */
    fun purchaseTile(player: Player, tile: Tile, destination: Enclosure) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var currentPlayer = game.players.peek()

        require(currentPlayer.coins >= 2) {"You don't have enough coins!"}
        require(!player.passed) {"this player has passed!"}
        check(game.players.contains(player))
        check(currentPlayer.playerEnclosure.contains(destination))

        if (tile is VendingStall) {
            check(player.barn.vendingStalls.contains(tile))
            check(destination.vendingStalls.size < destination.maxVendingStalls)

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

            currentPlayer = game.players.poll()
            player.coins++
            game.bank++
            currentPlayer.coins -= 2
            destination.vendingStalls.add(tile)
            player.barn.vendingStalls.remove(tile)
            game.players.add(currentPlayer)
        }
        else if (tile is Animal) {
            check(player.barn.animalTiles.contains(tile))
            check(destination.animalTiles.size < destination.maxAnimalSlots)

            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

            currentPlayer = game.players.poll()
            currentPlayer.coins -= 2
            player.coins++
            game.bank++
            if (destination.animalTiles.size == 0) {
                destination.animalTiles.add(tile)
                player.barn.animalTiles.remove(tile)
            }
            else {
                check(destination.animalTiles[0].species == tile.species)
                destination.animalTiles.add(tile)
                player.barn.animalTiles.remove(tile)
                if (destination.animalTiles.size == destination.maxAnimalSlots) {
                    bonusCoins(game, currentPlayer, destination)
                }
                checkAndAddNewBaby(game, currentPlayer, tile, destination)

                game.players.add(currentPlayer)
            }
        }
        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    /**
     * this method implements the discard move, when a player chooses to discard a tile from his barn
     * out of the game.
     *
     * @param target the tile, that the player wants to discard
     */
    fun discardTile(target: Tile) {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(player.coins >= 2) {"You don't have enough coins!"}
        require(!player.passed) {"this player has passed!"}

        if (target is VendingStall) {
            check(player.barn.vendingStalls.contains(target))
            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

            player = game.players.poll()
            player.barn.vendingStalls.remove(target)
            player.coins -= 2
            game.bank += 2
            game.players.add(player)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        else if (target is Animal) {
            check(player.barn.animalTiles.contains(target))
            val copyCurrentGame = rootService.gameStateService.deepZoolorettoCopy(game)
            rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

            player = game.players.poll()
            player.barn.animalTiles.remove(target)
            player.coins -= 2
            game.bank += 2
            game.players.add(player)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
    }

    /**
     * this method gives a hint to the player and shows him what he could play
     *
     * @return the showing message as a String
     */
    fun showHint() : String {
        return ""
    }

    /**
     * this method maps each tile to the available enclosures.
     *
     * @param tile the tile, that we want to find his available enclosures
     * @param player the player, so that we have access to his enclosures and barn
     */
    private fun setTileToEnclosures(tile: Tile, player: Player) {
        val availableEnclosures = mutableListOf<Enclosure>()
        availableEnclosures.add(player.barn)

        if (tile is VendingStall) {
            player.playerEnclosure.forEach {
                if(it.vendingStalls.size < it.maxVendingStalls) {
                    availableEnclosures.add(it)
                }
            }
        }
        else if (tile is Animal){
            player.playerEnclosure.forEach {
                if (it.animalTiles.size == 0) {
                    availableEnclosures.add(it)
                }
                else if (tile.species == it.animalTiles[0].species){
                    availableEnclosures.add(it)
                }
            }
        }
        tileToEnclosuresMap[tile] = availableEnclosures
    }

    /**
     * this method refreshes the map (tile -> enclosures) after we remove one tile from it and
     * place it in any enclosure in the zoo.
     *
     * @param map the to refresh map of tile to enclosures
     * @param player the player, that we have access to his enclosures and barn
     */
    private fun refreshTileToEnclosuresMap(map: Map<Tile, List<Enclosure>>, player: Player) {
        map.forEach {
            setTileToEnclosures(it.key, player)
        }
    }

    /**
     * this method checks if an animal can become a new baby in a specified enclosure.
     *
     * @param animal the animal, that we want to check if it becomes a new child
     * @param enclosure the enclosure, that we want to check in it
     *
     * @return true if animal becomes a child, otherwise false
     */
    private fun checkForNewBaby(animal: Animal, enclosure: Enclosure) : Boolean {
        if (animal.hasChild || animal.type == Type.OFFSPRING || animal.type == Type.NONE) {
            return false
        }
        enclosure.animalTiles.forEach {
            if (!it.hasChild && it.type != Type.NONE && it.type != Type.OFFSPRING) {
                if ((animal.type == Type.MALE && it.type == Type.FEMALE) ||
                    (animal.type == Type.FEMALE && it.type == Type.MALE)) {
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
            game.bank -+ enclosure.bonusCoins
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
            if (enclosure.animalTiles.size >= enclosure.maxAnimalSlots) {
                player.barn.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
            else if (enclosure.animalTiles.size + 1 == enclosure.maxAnimalSlots) {
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
                bonusCoins(game, player, enclosure)
            }
            else {
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
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
    private fun checkAndAddNewBabyWithoutBonusCoins(player: Player, animal: Animal, enclosure: Enclosure) {
        if (checkForNewBaby(animal, enclosure)) {
            if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
                player.barn.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
            else if (enclosure.animalTiles.size < enclosure.maxAnimalSlots){
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
        }
    }

    /**
     * this method checks if the game ends.
     * When roundDisc true is, then the game ends, when the round is over.
     * That means that each player has taken a truck and passed.
     *
     * @param game to get access to the players in the game
     */
    private fun checkIfGameEnds(game: ZoolorettoGameState) {
        if(game.roundDisc) {
            var end = true
            game.players.forEach {
                if (!it.passed) end = false
            }
            if (end) rootService.zoolorettoGameService.endGame()
        }
    }
}