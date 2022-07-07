package gameAI.moves

import gameAI.Move
import service.RootService

class ExpandZoo : Move {

    override fun performMove(rootService: RootService) {
        rootService.playerActionService.expandZoo()
    }

    override fun toHintString(): String {
        return "Expand Zoo"
    }
}