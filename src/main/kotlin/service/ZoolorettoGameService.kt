package service

import entity.*
import java.io.InputStream
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

/**
 * Class to create a new game and initialise the tiles
 */

class ZoolorettoGameService(private val rootService: RootService) : AbstractRefreshingService() {

    var tiles : ArrayList<Tile> = ArrayList(116)
    var animalTiles : ArrayList<Animal> = ArrayList(104)
    var offspringTiles : ArrayList<Animal> = ArrayList(16)
    var vendingStalls : ArrayList<VendingStall> = ArrayList(12)
    var coins : ArrayList<Coin> = arrayListOf(Coin(), Coin(), Coin(), Coin(), Coin(), Coin(),
                                              Coin(), Coin(), Coin(), Coin(), Coin(), Coin()
    )
    var players = listOf<Player>()
    var numberOfPlayers = 0 // used in loadTileFromFile


    /**
     * initialises the tiles according to the number of players
     */
    private fun initialiseTiles(){

        // initialise Animal tiles
        for(specie in Species.values()){
            repeat(2){animalTiles.add(Animal(Type.MALE, specie))}
            repeat(2){animalTiles.add(Animal(Type.FEMALE, specie)) }
            repeat(2){animalTiles.add(Animal(Type.OFFSPRING, specie))}
            repeat(2){offspringTiles.add(Animal(Type.OFFSPRING, specie))}
            repeat(7){animalTiles.add(Animal(Type.NONE, specie))}
        }

        // initialise vending stalls
        repeat(3){
            for (vendings in StallType.values()){
                vendingStalls.add(VendingStall(vendings))
            }
        }

        //using the private method setTiles to define the number of tiles needed
        repeat(setTiles()){
            tiles.add(animalTiles.removeFirst())
        }
        repeat(12){
            tiles.add(vendingStalls.removeFirst())
        }
        repeat(12){
            tiles.add(coins.removeFirst())
        }
        tiles.shuffle(Random(123))
    }

    /**
     * initialises the tile stacks using the tiles initialised in [initialiseTiles]
     */
    fun initialiseTileStack() : TileStack{
        val game = rootService.zoolorettoGame
        checkNotNull(game)
        val draw = game.currentGameState.tileStack.drawStack
        val end = game.currentGameState.tileStack.endStack
        val tileStack = TileStack(draw, end)
        val offspringStack = Stack<Tile>()
        initialiseTiles()
        println(tiles.size)
        // round tiles
        val toRemove: ArrayList<Tile> = ArrayList()
        for (tile in tiles) {
            if (tile in offspringTiles) {
                toRemove.add(tile)
                offspringStack.add(tile)
            }
        }
        tiles.removeAll(toRemove)

        //square tiles
        repeat(tiles.size-15){
            draw.add(tiles.removeFirst())
        }

        repeat(15){
            end.add(tiles.removeFirst())
        }

        return tileStack
    }



    /**
     * Creates a new game with the help of [createPlayer]
     */
    fun createZoolorettoGame(playerList : List<Player>,  toShuffle : Boolean){
        //create the players using the helping method
        players = List(playerList.size) {
            createPlayer(playerList[it].playerName, playerList[it].botSkillLevel)
        }
        // if toShuffle is true we shuffle the list
        if(toShuffle){players.toMutableList().shuffle(Random(123))}
        // make the list a queue
        val playerQueue : Queue<Player> = LinkedList<Player>(players)
        val gameState : ZoolorettoGameState
        //delivery trucks for a game with 3,4 or 5 players
        val deliveryTrucks : ArrayList<DeliveryTruck> = arrayListOf(DeliveryTruck(), DeliveryTruck(), DeliveryTruck())
        // delivery trucks for a game with 2 players
        val deliveryTrucksForTwo : ArrayList<DeliveryTruck> =
            arrayListOf(DeliveryTruck(1), DeliveryTruck(2), DeliveryTruck())

        // checking the number of players to determine the number of delivery trucks
        if (playerList.size == 2){
            gameState = ZoolorettoGameState(false, false, playerQueue, TileStack(Stack(), Stack()),
               deliveryTrucksForTwo)
            gameState.bank = 26
        } else{
            var i = playerList.size - 3
            while (i-- > 0) {
                deliveryTrucks.add(DeliveryTruck())
            }
            gameState = ZoolorettoGameState(false, false, playerQueue, TileStack(Stack(), Stack()),
                deliveryTrucks)
            gameState.bank = 30-(2*playerList.size)
        }
        rootService.zoolorettoGame = ZoolorettoGame(1.52f , gameState)
        initialiseTileStack()
        onAllRefreshables { refreshAfterCreateGame() }
    }

    /**
     * returns the number of tiles according to the number of players
     */
    private fun setTiles() : Int {
        val game = rootService.zoolorettoGame
        checkNotNull(game)
        return when(game.currentGameState.players.size){
            5 -> 104
            4 -> 91
            3 -> 78
            2 -> 65
            else -> throw IllegalArgumentException("Number of players to be created is not between 2 and 5.")
        }
    }

    /**
     * finishes the game
     */
    fun endGame(){
        for(player in rootService.zoolorettoGame!!.currentGameState.players){
            rootService.scoreService.determineScore(player)
        }
        rootService.scoreService.determineWinner()
        this.onAllRefreshables { refreshAfterGameEnd() }
    }

    /**
     * returns a player given their [name] and [Difficulty]
     */
    fun createPlayer(name : String, difficulty : Difficulty) : Player {
        require(name != ""){"The player name must not be empty!"}
        val player = Player(name, difficulty)
        player.coins = 2
        player.playerEnclosure.add(Enclosure(5, 1, 2, Pair(8,5), false))
        player.playerEnclosure.add(Enclosure(4, 2, 1, Pair(5,4), false))
        player.playerEnclosure.add(Enclosure(6, 1, 0, Pair(10,6), false))
        return player
    }

    /**
     * returns a [TileStack] created from a file in the format txt to which [path] is the path.
     */
    fun loadTileStackFromFile(path : String) : TileStack {
        val game = rootService.zoolorettoGame
        checkNotNull(game)

        var coins : String; var vendingStalls : String; var animals : String
        val result : ArrayList<Tile> = ArrayList()
        val draw = game.currentGameState.tileStack.drawStack
        val end = game.currentGameState.tileStack.endStack
        val tileStack = TileStack(draw, end)

        val reader : InputStream = File(path).inputStream()

        val lines = reader.bufferedReader().readLines()
        for(line in lines){
             numberOfPlayers = lines[0].toInt()
            if (line == "C"){
                coins = line
                result.add(stringToCoin(coins))}
            else if (line.contains("v")){
                vendingStalls = line
                result.add(stringToVending(vendingStalls))
            } else if( line.contains("m") || line.contains("w") || line.contains("-")){
                animals = line
                result.add(stringToAnimal(animals))
            }

        }
        for(i in 1..lines.size-16){draw.add(result.removeFirst())}
        for(j in lines.size-15.. lines.size){end.add(result.removeFirst())}
        return tileStack
    }

    /**
     * converts a string into a [Coin]
     */
    private fun stringToCoin(coin : String ) : Coin {
        return when(coin) {
            "C" -> Coin()
            else -> throw IllegalArgumentException("Illegal coin identifier: $coin")
        }
    }

    /**
     * converts a string into a [VendingStall]
     */
    private fun stringToVending(vendingType : String ) : VendingStall {
        return when(vendingType) {
            "v1" -> VendingStall(StallType.VENDING1)
            "v2" -> VendingStall(StallType.VENDING2)
            "v3" -> VendingStall(StallType.VENDING3)
            "v4" -> VendingStall(StallType.VENDING4)
            else -> throw IllegalArgumentException("Illegal stall type identifier: $vendingType")
        }
    }

    /**
     * converts a string into an [Animal]
     */
    private fun stringToAnimal(animal : String) : Animal{
        return when(animal){
            "Fm" -> Animal(Type.MALE, Species.F)
            "Fw" -> Animal(Type.FEMALE, Species.F)
            "F-" -> Animal(Type.OFFSPRING, Species.F)
            "Pm" -> Animal(Type.MALE, Species.P)
            "Pw" -> Animal(Type.FEMALE, Species.P)
            "P-" -> Animal(Type.OFFSPRING, Species.P)
            "Km" -> Animal(Type.MALE, Species.K)
            "Kw" -> Animal(Type.FEMALE, Species.K)
            "K-" -> Animal(Type.OFFSPRING, Species.K)
            "Sm" -> Animal(Type.MALE, Species.S)
            "Sw" -> Animal(Type.FEMALE, Species.S)
            "S-" -> Animal(Type.OFFSPRING, Species.S)
            "Lm" -> Animal(Type.MALE, Species.L)
            "Lw" -> Animal(Type.FEMALE, Species.L)
            "L-" -> Animal(Type.OFFSPRING, Species.L)
            "Zm" -> Animal(Type.MALE, Species.Z)
            "Zw" -> Animal(Type.FEMALE, Species.Z)
            "Z-" -> Animal(Type.OFFSPRING, Species.Z)
            "Em" -> Animal(Type.MALE, Species.E)
            "Ew" -> Animal(Type.FEMALE, Species.E)
            "E-" -> Animal(Type.OFFSPRING, Species.E)
            "Um" -> Animal(Type.MALE, Species.U)
            "Uw" -> Animal(Type.FEMALE, Species.U)
            "U-" -> Animal(Type.OFFSPRING, Species.U)
            else -> throw IllegalArgumentException("Illegal animal identifier: $animal")
        }
    }
}
