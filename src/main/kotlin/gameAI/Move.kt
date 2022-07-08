package gameAI

import service.RootService

interface Move {
    fun performMove(rootService: RootService) {}

    fun toHintString(rootService: RootService) : String{
        throw NotImplementedError()
    }

}