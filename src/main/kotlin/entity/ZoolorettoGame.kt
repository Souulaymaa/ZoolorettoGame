package entity

/**
 * Data class that represents the whole Zooloretto game.
 *
 * @param simSpeed: Simulation speed
 * @param currentGameState: current "game state"
 * @param undoStack: Stack to undo the current action
 * @param redoStack: Stack to redo the current action
 */
data class ZoolorettoGame (val simSpeed: Float, val currentGameState: ZoolorettoGameState) {

    val undoStack = Stack<ZoolorettoGameState>()
    val redoStack = Stack<ZoolorettoGameState>()

}