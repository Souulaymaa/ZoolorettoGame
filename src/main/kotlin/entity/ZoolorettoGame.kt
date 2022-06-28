package entity
import java.util.Stack
/**
 * Data class that represents the whole Zooloretto game.
 *
 * @param simSpeed: Simulation speed
 * @param currentGameState: current "game state"
 * @property undoStack: Stack to undo the current action
 * @property redoStack: Stack to redo the current action
 */
data class ZoolorettoGame (val simSpeed: Float, var currentGameState: ZoolorettoGameState) {

    var undoStack = Stack<ZoolorettoGameState>()
    var redoStack = Stack<ZoolorettoGameState>()

}