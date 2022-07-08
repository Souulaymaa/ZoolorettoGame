package entity
import java.io.Serializable
import java.util.Stack
/**
 * Data class that represents a stock of [Tile]
 *
 * @param endStack: A stack of tiles with a size of 15 at most.
 * @param drawStack: A stack of tiles with a size of 112 at most.
 */
data class TileStack(val drawStack : Stack<Tile>, val endStack: Stack<Tile>): Serializable {
    init {
        require(endStack.size <= 15){"The end stack of tiles cannot have more than 15 tiles."}
        require(drawStack.size <= 112){"The draw stack cannot have more than 112 tiles"}
    }
}
