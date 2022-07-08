package view

/**
 * Interface that provides a way for service layer classes to communicate to the view classes that
 * changes have been made that require a change of view.
 *
 * It provides default empty implementations, so that view classes only need to implement methods
 * that are relevant to them
 */
interface Refreshable {
    /**
     * perform refreshes after game is created
     */
    fun refreshAfterCreateGame() {}

    /**
     * perform refreshes after a player finished his turn
     */
    fun refreshAfterPlayerAction() {}

    /**
     * perform refreshes after Refill
     */
    fun refreshPlayerZoo() {}

    /**
     * perform refreshes after Undo
     */
    fun refreshAfterUndo() {}

    /**
     * perform refreshes after Redo
     */
    fun refreshAfterRedo() {}

    /**
     * perform refreshes after game ends
     */
    fun refreshAfterGameEnd() {}
}

