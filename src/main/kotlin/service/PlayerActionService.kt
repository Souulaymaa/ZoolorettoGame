package service

import entity.*

class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {
    private val tileToEnclosuresMap = mutableMapOf<Tile, List<Enclosure>>()

    fun addTile(truck: DeliveryTruck){
        val game = rootService.zoolorettoGame!!.currentGameState

        if(!game.deliveryTrucks.contains(truck)) {
            println("wrong truck!!")
            return
        }
        var player = game.players.peek()
        if(player.passed) {
            println("the player can not add a tile!!")
            return
        }
        if(truck.tilesOnTruck.size < 3) {
            if(game.roundDisc) {
                if(!game.tileStack.endStack.isEmpty()) {
                    val copyCurrentGame = deepZoolorettoCopy(game)
                    rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

                    player = game.players.poll()
                    val tile = game.tileStack.endStack.removeFirst()
                    truck.tilesOnTruck.add(tile)
                    game.players.add(player)

                    rootService.zoolorettoGame.redoStack.clear()
                    onAllRefreshables { refreshAfterPlayerAction() }
                }
                else {
                    println("there are no more tiles!!")
                    return
                }
            }
            else if(!game.tileStack.drawStack.isEmpty()) {
                val copyCurrentGame = deepZoolorettoCopy(game)
                rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

                player = game.players.poll()
                val tile = game.tileStack.endStack.removeFirst()
                truck.tilesOnTruck.add(tile)
                game.players.add(player)
                if(game.tileStack.drawStack.isEmpty()) {
                    game.roundDisc = true
                }

                rootService.zoolorettoGame.redoStack.clear()
                onAllRefreshables { refreshAfterPlayerAction() }
            }
        }
        else {
            println("the truck is full")
            return
        }
    }

    fun moveOneTile (source: Player, destination: Enclosure, tile: Tile) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(!player.passed)
        if(player.coins == 0) {
            println("the player has not enough coins!!")
            return
        }
        if(destination.isBarn) {
            println("it is not allowed to move tiles to the barn")
            return
        }
        if(!player.playerEnclosure.contains(destination) || !player.equals(source)
            || (!player.barn.animalTiles.contains(tile) && !player.barn.vendingStalls.contains(tile))) {
            println("wrong parameter!!")
            return
        }
        if(tile is VendingStall) {
            if(destination.vendingStalls.size < destination.maxVendingStalls) {
                val copyCurrentGame = deepZoolorettoCopy(game)
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
            else {
                println("there isn't enough space!!")
                return
            }
        }
        else if (tile is Animal){
            if(destination.animalTiles.size < destination.maxAnimalSlots) {
                val copyCurrentGame = deepZoolorettoCopy(game)
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
    }

    fun moveVendingStall(source: Enclosure, destination: Enclosure) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(!player.passed)
        if(player.coins == 0) {
            println("the player doesn't have enough coins!!")
            return
        }
        if(!player.playerEnclosure.contains(source) || !player.playerEnclosure.contains(destination)) {
            println("wrong parameter!!")
            return
        }
        if(source.vendingStalls.size == 0 || destination.vendingStalls.size == destination.maxVendingStalls) {
            println("no vending stall can be moved!!")
            return
        }
        val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun moveTileFromEnclosureToBarn(source: Enclosure, destination: Player, tile: Tile) {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(player.coins >= 1)
        check(!player.passed)
        check(!source.isBarn)
        check(player.equals(destination))
        check(player.playerEnclosure.contains(source))

        val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun expandZoo() {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(!player.passed)
        if(player.coins < 3) {
            println("the player doesn't have enough coins!!")
            return
        }
        if(player.playerEnclosure.size == 5 || (player.playerEnclosure.size == 4 && game.players.size > 2)) {
            println("the player can't expand the zoo!!")
            return
        }
        val copyCurrentGame = deepZoolorettoCopy(game)
        rootService.zoolorettoGame.undoStack.add(copyCurrentGame)

        player = game.players.poll()
        player.playerEnclosure.add(Enclosure(5, 1, 0, Pair(9,5), false))
        player.coins -= 3
        game.bank += 3
        game.players.add(player)

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    fun takeTruck(truck: DeliveryTruck) : Map<Tile, List<Enclosure>> {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        require(game.deliveryTrucks.contains(truck))
        require(truck.size in 1..3 && truck.tilesOnTruck.isNotEmpty())
        require(!player.passed)

        val copyCurrentGame = deepZoolorettoCopy(game)
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
            game.players.forEach {
                it.passed = false
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
            game.players.forEach {
                it.passed = false
            }
            game.players.add(player)
        }

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
        return tileToEnclosuresMap
    }

    fun placeTileFromTruck(destination: Enclosure) : Map<Tile, List<Enclosure>> {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val truck = player.chosenTruck

        require(player.playerEnclosure.contains(destination))
        checkNotNull(truck)
        val tile = truck.tilesOnTruck[0]
        require(tileToEnclosuresMap[tile]!!.contains(destination))

        val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun placeTileFromTruck(destination: Player) : Map<Tile, List<Enclosure>> {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val truck = player.chosenTruck

        require(player.equals(destination))
        checkNotNull(truck)
        val tile = truck.tilesOnTruck[0]

        val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun exchangeAllTiles(source: Enclosure, destination: Player, animal: Animal) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(player.coins >= 1)
        check(!player.passed)
        check(player.playerEnclosure.contains(source))
        check(player.equals(destination))
        check(source.animalTiles.isNotEmpty() && player.barn.animalTiles.contains(animal))
        check(source.animalTiles[0].species != animal.species)

        val tempList: ArrayList<Animal> = arrayListOf()
        player.barn.animalTiles.forEach {
            if (it.species == animal.species) {
                tempList.add(it)
            }
        }
        check(tempList.size <= source.maxAnimalSlots)

        val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun exchangeAllTiles(source: Enclosure, destination: Enclosure) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(player.coins >= 1)
        check(!player.passed)
        check(player.playerEnclosure.contains(source) && player.playerEnclosure.contains(destination))
        check(source.animalTiles.isNotEmpty() && destination.animalTiles.isNotEmpty())
        check(source.animalTiles[0].species != destination.animalTiles[0].species)
        check(source.animalTiles.size <= destination.maxAnimalSlots &&
              destination.animalTiles.size <= source.maxAnimalSlots)

        val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun purchaseTile(player: Player, tile: Tile, destination: Enclosure) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var currentPlayer = game.players.peek()

        check(currentPlayer.coins >= 2)
        check(!player.passed)
        check(game.players.contains(player))
        check(currentPlayer.playerEnclosure.contains(destination))

        if (tile is VendingStall) {
            check(player.barn.vendingStalls.contains(tile))
            check(destination.vendingStalls.size < destination.maxVendingStalls)

            val copyCurrentGame = deepZoolorettoCopy(game)
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

            val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun discardTile(target: Tile) {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(player.coins >= 2)
        check(!player.passed)

        if (target is VendingStall) {
            check(player.barn.vendingStalls.contains(target))
            val copyCurrentGame = deepZoolorettoCopy(game)
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
            val copyCurrentGame = deepZoolorettoCopy(game)
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

    fun showHint() : String {
        return ""
    }

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

    private fun refreshTileToEnclosuresMap(map: Map<Tile, List<Enclosure>>, player: Player) {
        map.forEach {
            setTileToEnclosures(it.key, player)
        }
    }

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

    private fun checkAndAddNewBaby(game: ZoolorettoGameState, player: Player, animal: Animal, enclosure: Enclosure) {
        if (checkForNewBaby(animal, enclosure)) {
            if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
                player.barn.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
            else if (enclosure.animalTiles.size - 1 == enclosure.maxAnimalSlots) {
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
                bonusCoins(game, player, enclosure)
            }
            else {
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
        }
    }

    private fun checkAndAddNewBabyWithoutBonusCoins(player: Player, animal: Animal, enclosure: Enclosure) {
        if (checkForNewBaby(animal, enclosure)) {
            if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
                player.barn.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
            else {
                enclosure.animalTiles.add(Animal(Type.OFFSPRING, animal.species))
            }
        }
    }
}