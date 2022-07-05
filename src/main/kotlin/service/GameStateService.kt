package service

import entity.*
import com.google.gson.internal.LinkedTreeMap
import java.io.*

/**
 * Class to load and save Zooloretto as well as load [Tile] to [TileStack]
 */
class GameStateService(private val rootService: RootService) : AbstractRefreshingService() {
    private val nlTilesFileName = "/nl_tiles.csv"
    private val highscoreFileName = "highscore.json"
    private val zoolorettoFileName = "zooloretto-save.json"

    /**
     * Load [ZoolorettoGame] from file from json to [ZoolorettoGame]
     *
     * @return Deserialized [ZoolorettoGame] Object
     */
    fun loadState() : ZoolorettoGame?{
        return load<ZoolorettoGame>(zoolorettoFileName)
    }

    /**
     * Save [ZoolorettoGameState] to file as a JSON String
     */
    fun saveState() {
        save(rootService.zoolorettoGame, zoolorettoFileName)
    }

    /**
     * Generic function to load something from a JSON stored in the file [fileName].
     *
     * It is used for [GameStateService.loadHighScore] and [GameStateService.loadState]
     */
    private inline fun <reified T> load(fileName: String): T? {
        // deserialize
        val fis = FileInputStream(fileName)
        val ois = ObjectInputStream(fis)
        val loadedObject= ois.readObject() as T
        return loadedObject
    }


    /**
     * Private function to save the object [obj] to a JSON string in the file [fileName]
     */
    private fun <T> save(obj : T, fileName: String){
        // Serialize
        val fos = FileOutputStream(fileName)
        val oos = ObjectOutputStream(fos)
        oos.writeObject(obj)
        oos.flush()



    }

    /**
     * Save [RootService.highscore] to a file as JSON string
     */
    fun saveHighScore() {
        save(rootService.highscore, highscoreFileName)
    }

    /**
     * Load the highscore from a saved file.
     *
     * @return Returns the highscore list or null if the file does not exist yet.
     */
    fun loadHighScore() : MutableList<Pair<String, Double>>? {
        return load(highscoreFileName)
    }

    /**
     * Function to create a deep copy of [ZoolorettoGameState] object without
     * any references to the object's properties.
     *
     * @param toCopy Object, which to copy from
     * @return A copy of the Parameter
     */

    fun deepZoolorettoCopy(toCopy : ZoolorettoGameState) : ZoolorettoGameState {
        // Serialize
        val bos = ByteArrayOutputStream()
        val oos = ObjectOutputStream(bos)
        oos.writeObject(toCopy)
        oos.flush()

        // deserialize
        val bytes = bos.toByteArray()
        val bis = ByteArrayInputStream(bytes)
        val ois = ObjectInputStream(bis)
        val clone = ois.readObject() as ZoolorettoGameState
        return clone
    }
    companion object{
        fun deepZoolorettoCopy(toCopy : ZoolorettoGameState) : ZoolorettoGameState {
            // Serialize
            val bos = ByteArrayOutputStream()
            val oos = ObjectOutputStream(bos)
            oos.writeObject(toCopy)
            oos.flush()

            // deserialize
            val bytes = bos.toByteArray()
            val bis = ByteArrayInputStream(bytes)
            val ois = ObjectInputStream(bis)
            val clone = ois.readObject() as ZoolorettoGameState
            return clone
        }
    }

}
