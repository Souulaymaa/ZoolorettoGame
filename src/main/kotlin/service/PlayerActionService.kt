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
                    rootService.zoolorettoGame.undoStack.add(game)

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
                rootService.zoolorettoGame.undoStack.add(game)

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
                rootService.zoolorettoGame.undoStack.add(game)

                player = game.players.poll()
                destination.vendingStalls.add(tile)
                player.barn.vendingStalls.remove(tile)
                player.coins--
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
                rootService.zoolorettoGame.undoStack.add(game)

                player = game.players.poll()
                destination.animalTiles.add(tile)
                player.barn.animalTiles.remove(tile)
                player.coins--
                game.players.add(player)

                rootService.zoolorettoGame.redoStack.clear()
                onAllRefreshables { refreshAfterPlayerAction() }
            }
        }
    }

    fun moveVendingStall(source: Enclosure, destination: Enclosure) {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

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
        rootService.zoolorettoGame.undoStack.add(game)

        player = game.players.poll()
        destination.vendingStalls.add(source.vendingStalls[0])
        source.vendingStalls.removeFirst()
        player.coins--
        game.players.add(player)

        rootService.zoolorettoGame.redoStack.clear()
        onAllRefreshables { refreshAfterPlayerAction() }
    }

    fun moveTileFromEnclosureToBarn(source: Enclosure, destination: Player, tile: Tile) {

        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        check(!source.isBarn)
        check(player.equals(destination))
        check(player.playerEnclosure.contains(source))
        check(player.coins >= 1)

        if (tile is VendingStall) {
            //kopieren von currentGame

            check(source.vendingStalls.contains(tile))
            player = game.players.poll()
            source.vendingStalls.remove(tile)
            player.barn.vendingStalls.add(tile)
            player.coins--
            game.players.add(player)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        else if (tile is Animal) {
            //kopieren von currentGame

            check(source.animalTiles.contains(tile))
            player = game.players.poll()
            source.animalTiles.remove(tile)
            player.barn.animalTiles.add(tile)
            player.coins--
            game.players.add(player)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
    }
    fun expandZoo() {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()

        if(player.coins < 3) {
            println("the player doesn't have enough coins!!")
            return
        }
        if(player.playerEnclosure.size == 5 || (player.playerEnclosure.size == 4 && game.players.size > 2)) {
            println("the player can't expand the zoo!!")
            return
        }
        rootService.zoolorettoGame.undoStack.add(game)

        player = game.players.poll()
        player.playerEnclosure.add(Enclosure(5, 1, 0, Pair(9,5), false))
        player.coins -= 3
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

        if(game.deliveryTrucks.size == 1) {
            //kopieren von currentGame

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
            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        else {
            //Kopieren von currentGame

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

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
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

        if(truck.tilesOnTruck.size == 1) {
            //Kopieren von currentGame

            player = game.players.poll()

            if (tile is VendingStall) {
                destination.vendingStalls.add(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            else if (tile is Animal) {
                destination.animalTiles.add(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            truck.tilesOnTruck.removeFirst()
            tileToEnclosuresMap.remove(tile)
            game.players.add(player)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        else {
            //Kopieren von currentGame

            if (tile is VendingStall) {
                destination.vendingStalls.add(tile)
                tileToEnclosuresMap.remove(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            else if (tile is Animal) {
                player.barn.animalTiles.add(tile)
                tileToEnclosuresMap.remove(tile)
                refreshTileToEnclosuresMap(tileToEnclosuresMap, player)
            }
            truck.tilesOnTruck.removeFirst()
            tileToEnclosuresMap.remove(tile)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        return tileToEnclosuresMap
    }

    fun placeTileFromTruck(destination: Player) : Map<Tile, List<Enclosure>> {
        val game = rootService.zoolorettoGame!!.currentGameState
        var player = game.players.peek()
        val truck = player.chosenTruck

        require(player.equals(destination))
        checkNotNull(truck)
        val tile = truck.tilesOnTruck[0]

        if(truck.tilesOnTruck.size == 1) {
            //Kopieren von currentGame

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

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        else {
            //Kopieren von currentGame

            if (tile is VendingStall) {
                player.barn.vendingStalls.add(tile)
            }
            else if (tile is Animal) {
                player.barn.animalTiles.add(tile)
            }
            truck.tilesOnTruck.removeFirst()
            tileToEnclosuresMap.remove(tile)

            rootService.zoolorettoGame.redoStack.clear()
            onAllRefreshables { refreshAfterPlayerAction() }
        }
        return tileToEnclosuresMap
    }

    fun exchangeAllTiles(source: Enclosure, destination: Player) {

    }

    fun exchangeAllTiles(source: Enclosure, destination: Enclosure) {

    }

    fun purchaseTile(player: Player, tile: Tile) {

    }

    fun discardTile(target: Tile) {

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
        if (animal.hasChild) {
            return false
        }
        enclosure.animalTiles.forEach {
            if (!it.hasChild) {
                if (animal.type == Type.MALE && it.type == Type.FEMALE) {
                    animal.hasChild = true
                    it.hasChild = true
                    return true
                }
                if (animal.type == Type.FEMALE && it.type == Type.MALE) {
                    animal.hasChild = true
                    it.hasChild = true
                    return true
                }
            }
        }
        return false
    }
}