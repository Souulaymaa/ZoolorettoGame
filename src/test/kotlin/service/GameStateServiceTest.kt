package service

import entity.*
import java.util.*
import kotlin.test.*

/**
 * Test Cases for [GameStateService]
 */
class GameStateServiceTest {
    private val rootService = RootService()
    private val gameStateService = GameStateService(rootService)
    private val tile = Animal(Type.FEMALE, Species.F)

    // deliveryTrucks is needed for the constructor of ZoolorettoGameState
    private val deliveryTruck1 = DeliveryTruck()
    private val deliveryTruck2 = DeliveryTruck()
    private val deliveryTruck3 = DeliveryTruck()
    private val deliveryTrucks = mutableListOf(deliveryTruck1, deliveryTruck2, deliveryTruck3)

    // tileStack is needed for the constructor of ZoolorettoGameState.
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())


    // Player Queue is also needed for the constructor of ZoolorettoGameState.
    private val player1 = Player("Player 1", Difficulty.HUMAN)
    private val player2 = Player("Player 2", Difficulty.HUMAN)
    private val players: Queue<Player> = LinkedList(listOf(player1, player2))

    /**
     * tests the [GameStateService.deepZoolorettoCopy] function.
     * Checks if the [ZoolorettoGameState] is copied correctly
     */
    @Test
    fun testDeepZoolorettoCopy(){

        val zoolorettoGameState = ZoolorettoGameState(
            players = players, tileStack = tileStack,
            deliveryTrucks = deliveryTrucks
        )
        zoolorettoGameState.tileStack.drawStack.add(tile)

        // Creates a copy of the zoolorettoGameState
        val gameCopy = gameStateService.deepZoolorettoCopy(zoolorettoGameState)

        // Ensures that zoolorettoGameState and it's copy are equal by checking the equality of the different attributes.
        assertEquals(zoolorettoGameState.players, gameCopy.players)
        assertEquals(zoolorettoGameState.deliveryTrucks, gameCopy.deliveryTrucks)
        assertEquals(zoolorettoGameState.tileStack, gameCopy.tileStack)
        assertEquals(zoolorettoGameState.tileStack.drawStack[0], gameCopy.tileStack.drawStack[0])

        // Ensures that zoolorettoGameState and it's copy have different references by checking the inequality
        //of the references of each attribute.
        assertNotSame(zoolorettoGameState.players.peek(), gameCopy.players.peek())
        assertNotSame(zoolorettoGameState.deliveryTrucks, gameCopy.deliveryTrucks)
        assertNotSame(zoolorettoGameState.tileStack, gameCopy.tileStack)
    }

    /**
     * Tests the [GameStateService.saveHighScore] and [GameStateService.loadHighScore] functions.
     * Checks if the highScore list is saved and loaded correctly.
     */
    @Test
    fun testSaveAndLoadHighScores(){
        rootService.highscore = mutableListOf(Pair("Player1",5.0), Pair("Player2", 4.0))
        val scoresInGame = rootService.highscore

        // saves the highScore list in a file.
        gameStateService.saveHighScore()

        // loads the saved highScore list.
        val scoresFromFile = gameStateService.loadHighScore()

        // Ensures that the scoresInGame and the loaded scores are equal.
        assertEquals(scoresInGame, scoresFromFile)
        assertNotSame(scoresInGame, scoresFromFile)

        scoresInGame.add(Pair("Player3", 3.0))

        gameStateService.saveHighScore()
        val newLoadedScores = gameStateService.loadHighScore()
        assertEquals(scoresInGame, newLoadedScores)
        assertNotSame(scoresInGame, newLoadedScores)
    }

    /**
     * Tests the [GameStateService.saveState] and [GameStateService.loadState] functions.
     * Checks if the [ZoolorettoGame] is saved and loaded correctly.
     */
    @Test
    fun testSaveAndLoadState(){

        val zoolorettoGameState = ZoolorettoGameState(
            players = players, tileStack = tileStack,
            deliveryTrucks = deliveryTrucks
        )

        val zoolorettoGame = ZoolorettoGame(1f, zoolorettoGameState)

        rootService.zoolorettoGame = zoolorettoGame

        val game = rootService.zoolorettoGame
        checkNotNull(game)

        // saves the zoolorettoGame in a file.
        gameStateService.saveState()

        // loads the saved zoolorettoGame from the file.
        val savedGame = gameStateService.loadState()
        checkNotNull(savedGame)

        // Ensures that the saved game and the loaded game are equal but have different references.
        assertEquals(game.currentGameState.players, savedGame.currentGameState.players)
        assertNotSame(game.currentGameState.players, savedGame.currentGameState.players)
    }

}