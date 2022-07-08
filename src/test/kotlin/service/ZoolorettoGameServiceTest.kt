package service
import entity.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.util.*
import kotlin.test.*

/**
 * Tests if the GameService works correct.
 */
class ZoolorettoGameServiceTest {

    private val rootServiceOne = RootService()
    private val rootServiceTwo = RootService()
    private val rootServiceThree = RootService()

    // deliveryTrucks
    private val deliveryTrucks = mutableListOf<DeliveryTruck>()
    private val deliveryTruck1 = DeliveryTruck()
    private val deliveryTruck2 = DeliveryTruck()
    private val deliveryTruck3 = DeliveryTruck()

    // stall to add on the delivery truck
    private val stall = VendingStall(StallType.VENDING1)

    /**
     * Some tiles for tests
     */
    private val animal1 = Animal(Type.MALE, Species.F)
    private val animal2 = Animal(Type.FEMALE, Species.F)


    // tileStack for testing and tilestack for reading from file
    private val tileStack = TileStack(Stack<Tile>(), Stack<Tile>())
    private var tileStackFromFile = TileStack(Stack<Tile>(), Stack<Tile>())

    // playerEnclosures and a barn to create players.
    private val enclosure1 = Enclosure(6, 1, 0, Pair(10,6), false)
    private val enclosure2 = Enclosure(5, 1, 0, Pair(8,5), false)
    private val enclosure3 = Enclosure(4, 2, 0, Pair(5,4), false)
    private val playerEnclosures = mutableListOf(enclosure1, enclosure2, enclosure3)

    //creates Players
    private val player1 = Player("Player 1", Difficulty.HUMAN)
    private val player2 = Player("Player 2", Difficulty.HUMAN)
    private val player3 = Player("Player 3", Difficulty.HUMAN)
    private val players = listOf(player1, player2)
    private val players2 = listOf(player1, player2, player3)



    /**
     * Tests if createZoolorettoGame works correct while creating a game with not shuffled
     * players
     */

    @Test
    fun testCreateZoolorettoGameOne(){
        //adds delivery trucks
        deliveryTrucks.add(deliveryTruck1)
        deliveryTrucks.add(deliveryTruck2)
        deliveryTrucks.add(deliveryTruck3)


        // First game with list of players and no shuffle
        rootServiceOne.zoolorettoGameService.createZoolorettoGame(players,false)
        val game1 = rootServiceOne.zoolorettoGame!!.currentGameState

        // Game should be initialized and created
        assertNotNull(game1)

        // First game should have 2 players and 3 delivery trucks
        assertEquals(game1.players.size, 2)
        assertEquals(game1.deliveryTrucks.size, 3)


    }
    /**
     * Tests if createZoolorettoGame works correct while creating a game with not shuffled
     * players
     */

    @Test
    fun testCreateZoolorettoGameTwo(){

        // Second game with with shuffled list of players
        rootServiceTwo.zoolorettoGameService.createZoolorettoGame(players,true)
        val game2 = rootServiceTwo.zoolorettoGame!!.currentGameState

        // Game should be initialized and created
        assertNotNull(game2)

        //Checks if all the players are in a game if the randomize boolean is true and have enough coins to play
        assert(game2.players.contains(player1))
        assert(game2.players.contains(player2))
        assertEquals(player1.coins, 2)
        assertEquals(player2.coins, 2)
        //Checks if delivery truck has a proper max size
        Assertions.assertEquals(deliveryTruck1.maxSize, 3)
        //Adds tiles on the third and second truck
        deliveryTruck3.tilesOnTruck.add(stall)
        deliveryTruck3.tilesOnTruck.add(animal1)
        deliveryTruck2.tilesOnTruck.add(animal2)
        //Checks the size of the delivery trucks
        Assertions.assertEquals(2, deliveryTruck3.tilesOnTruck.size)
        Assertions.assertEquals(1, deliveryTruck2.tilesOnTruck.size)
    }

    /**
     * Tests if createPlayer works correct and creates players
     */
    @Test
    fun testCreatePlayer(){
        val players : Queue<Player> = LinkedList()

        val player3 = rootServiceOne.zoolorettoGameService.createPlayer("Player 3", Difficulty.HUMAN)
        val player4 = rootServiceOne.zoolorettoGameService.createPlayer("Player 4", Difficulty.EASY)
        players.add(player3)
        players.add(player4)


        player4.playerEnclosure = playerEnclosures
        assertFalse(player3.passed)
        assertEquals(2, player3.coins)
        assertEquals(null, player4.chosenTruck)
        assertEquals(0, player4.score)
        assertTrue(player4.playerName == "Player 4"
                && player4.botSkillLevel == Difficulty.EASY
                && player4.playerEnclosure == playerEnclosures)
    }

    /**
     * Test 1 for tilestack loading - Tests if loadTileStackFromFile  works correct and tiles are loaded correctly
     */
    @Test
    fun testLoadTileStackFromFile1(){
        // create a game
        rootServiceThree.zoolorettoGameService.createZoolorettoGame(players, false)
        // Apply reading method to get a tilestack from file
        tileStackFromFile = rootServiceThree.zoolorettoGameService.loadTileStackFromFile(
            "src/test/kotlin/service/ZoolorettoFameServiceAnimalsFromFile.txt")
        // make the tileStack created with our method the game's tileStack
        rootServiceThree.zoolorettoGame!!.currentGameState.tileStack = tileStackFromFile

        // Checks the number of lines in the file
        assertEquals(16, tileStackFromFile.drawStack.size)
        assertEquals(15, tileStackFromFile.endStack.size)
    }

    /**
     * Test 2 for tilestack - tests if loadTileStackFromFile throws an Exception when the game is created with a number of players other
     * than the one in the file
     */
    @Test
    fun testLoadTileStackFromFile2(){
        // we created a game with three players but the files indicates there should only be two
        rootServiceThree.zoolorettoGameService.createZoolorettoGame(players2, false)
        assertFailsWith<IllegalArgumentException>{
            tileStackFromFile = rootServiceThree.zoolorettoGameService.loadTileStackFromFile(
                "src/test/kotlin/service/ZoolorettoFameServiceAnimalsFromFile.txt")}
    }
    /**
     * Test 3 for tilestack - tests if the tilestack, read from file, is equal to the self-created tilestack
     */
    @Test
    fun testLoadTileStackFromFile3(){
        // create a game
        rootServiceThree.zoolorettoGameService.createZoolorettoGame(players, false)
        // Apply reading method to get a tilestack from file
        tileStackFromFile = rootServiceThree.zoolorettoGameService.loadTileStackFromFile(
            "src/test/kotlin/service/ZoolorettoFameServiceAnimalsFromFile.txt")

        rootServiceThree.zoolorettoGame!!.currentGameState.tileStack = tileStackFromFile

        for (i in 1..9)  {tileStack.drawStack.add(Coin())}
        tileStack.drawStack.add(VendingStall(StallType.VENDING1))
        tileStack.drawStack.add(VendingStall(StallType.VENDING2))
        tileStack.drawStack.add(VendingStall(StallType.VENDING3))
        tileStack.drawStack.add(VendingStall(StallType.VENDING4))
        // At this point drawStacks should be unequal
        assertNotEquals(tileStackFromFile.drawStack, tileStack.drawStack)

        tileStack.drawStack.add(VendingStall(StallType.VENDING1))
        tileStack.drawStack.add(VendingStall(StallType.VENDING2))
        tileStack.drawStack.add(VendingStall(StallType.VENDING3))

        // At this point drawStacks should be equal and have a size of 16
        assertEquals(tileStackFromFile.drawStack.size, tileStack.drawStack.size)


    }
}
