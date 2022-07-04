package service

import entity.*
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import com.google.gson.Gson


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
        return loadFromJSON<ZoolorettoGame>(zoolorettoFileName)
    }

    /**
     * Save [ZoolorettoGameState] to file as a JSON String
     */
    fun saveState() {
        save(rootService.zoolorettoGame?.currentGameState, zoolorettoFileName)
    }

    /**
     * Generic function to load something from a JSON stored in the file [fileName].
     *
     * It is used for [GameStateService.loadHighscore] and [GameStateService.loadGame]
     */
    private inline fun <reified T> loadFromJSON(fileName: String): T? {
        val gson = Gson()
        val reader: BufferedReader
        try {
            reader = Files.newBufferedReader(Paths.get(fileName))
        } catch (e: IOException) {
            return null
        } catch (e:SecurityException){
            return null
        }
        reader.close()
        return gson.fromJson(fileName, T::class.java)
    }


    /**
     * Private function to save the object [obj] to a JSON string in the file [fileName]
     */
    private fun <T> save(obj : T, fileName: String){
        val gson = Gson()
        val json = gson.toJson(obj)
        val file = File(fileName)
        file.writeText(json)
    }

    /**
     * Save [RootService.highscore] to a file as JSON string
     */
    fun saveHighscore() {
        save(rootService.highscore, highscoreFileName)
    }

    /**
     * Load the highscore from a saved file.
     *
     * @return Returns the highscore list or null if the file does not exist yet.
     */
    fun loadHighscore() : List<Pair<String, Double>>? {
        return loadFromJSON<List<Pair<String, Double>>>(highscoreFileName)
    }
    /**
     * Function to create a deep copy of [ZoolorettoGameState] object without
     * any references to the object's properties.
     *
     * @param toCopy Object, which to copy from
     * @return A copy of the Parameter
     */

    fun deepZoolorettoCopy(toCopy : ZoolorettoGameState) : ZoolorettoGameState {
        val gson = Gson()
        val jsonString = gson.toJson(toCopy) // Serialization
        val gameCopy = gson.fromJson(jsonString, ZoolorettoGameState::class.java) // Deserialization

        return gameCopy
    }

    companion object{
        fun deepZoolorettoCopy(toCopy : ZoolorettoGameState) : ZoolorettoGameState {
            val gson = Gson()
            val jsonString = gson.toJson(toCopy) // Serialization
            val gameCopy = gson.fromJson(jsonString, ZoolorettoGameState::class.java) // Deserialization

            return gameCopy
        }
    }

}
