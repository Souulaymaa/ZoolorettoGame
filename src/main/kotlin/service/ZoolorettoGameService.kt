package service

import entity.*
import java.util.*
import kotlin.collections.ArrayList


class ZoolorettoGameService(val rootService: RootService) : AbstractRefreshingService() {


        var tiles : ArrayList<Tile> = ArrayList(116)
        var animalTiles : ArrayList<Animal> = ArrayList(104)
        var vendingStalls : ArrayList<VendingStall> = ArrayList(12)
        var players = listOf<Player>()

        private fun initialiseTiles(){
            // initialise Animal tiles
            repeat(13){
                for(type in Type.values()){
                    for(specie in Species.values()){
                        animalTiles.add(Animal(type, specie))
                    }
                }
            }
            // initialise vending stalls
            repeat(3){
                for (vendings in StallType.values()){
                    vendingStalls.add(VendingStall(vendings))
                }
            }
            repeat(setTiles()){
                tiles.add(animalTiles.removeFirst())
            }
            repeat(12){
                tiles.add(vendingStalls.removeFirst())}
            tiles.shuffle(Random(123))

        }

        fun initialiseTileStack() : TileStack{
            val game = rootService.zoolorettoGame
            checkNotNull(game)
            val draw = game.currentGameState.tileStack.drawStack
            val end = game.currentGameState.tileStack.endStack
            var tileStack = TileStack(draw, end)

            repeat(tiles.size-15){
                draw.add(tiles.removeFirst())
            }
            repeat(15){
                end.add(tiles.removeFirst())
            }
            return tileStack
        }

        fun createZoolorettoGame(playerList : List<Player>, tileStack : TileStack, toShuffle : Boolean){
            //create the players using the helping method
            players = List(playerList.size) {
                createPlayer(playerList[it].playerName, playerList[it].botSkillLevel)
            }
            // if toShuffle is true we shuffle the list
            if(toShuffle){players.toMutableList().shuffle(Random(123))}
            // make the list a queue
            val playerQueue : Queue<Player> = LinkedList<Player>(players)
            val gameState : ZoolorettoGameState
            // checking the number of players to determine the number of delivery trucks
            if(playerList.size == 2){
                gameState = ZoolorettoGameState(false, false, playerQueue, initialiseTileStack(),
                    ArrayList<DeliveryTruck>(3))
            } else{
                gameState = ZoolorettoGameState(false, false, playerQueue, initialiseTileStack(),
                    ArrayList<DeliveryTruck>(playerList.size))
            }
            rootService.zoolorettoGame = ZoolorettoGame(1.52f , gameState)
            onAllRefreshables { refreshAfterCreateGame() }
        }

        private fun setTiles() : Int {
            return when(players.size){
                5 -> 104
                4 -> 93
                3 -> 82
                2 -> 79
                else -> throw IllegalArgumentException("Number of players to be created is not between 2 and 5.")
            }
        }

      /*  fun endGame(){
            for(player in players){
                rootService.scoreService.determineScore(player)
            }
            rootService.scoreService.determineHighscore()
            this.onAllRefreshables { refreshAfterGameEnd() }
        }*/

        fun createPlayer(name : String, difficulty : Difficulty) : Player {
            return Player(name, difficulty)
        }

        fun loadTileStackFromFile(path : String) {}


}