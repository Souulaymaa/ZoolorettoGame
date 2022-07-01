package service

import entity.ZoolorettoGameState
import com.google.gson.Gson

class GameStateService(val rootService: RootService) : AbstractRefreshingService() {
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
}

