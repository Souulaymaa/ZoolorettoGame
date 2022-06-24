package service

import entity.*

class PlayerActionService(val rootService: RootService) : AbstractRefreshingService() {
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
    fun takeTruck(truck: DeliveryTruck) {

    }
    fun placeTileFromTruck(destination: Enclosure) {

    }

    fun placeTileFromTruck(destination: Player) {

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
}