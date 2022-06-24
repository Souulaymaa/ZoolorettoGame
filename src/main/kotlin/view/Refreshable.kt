package view

interface Refreshable {
    fun refreshAfterCreateGame()
    fun refreshAfterPlayerAction()
    fun refreshAfterGameEnd()
    fun refreshPlayerZoo()
}