package gameai.moves

import gameai.Move
import service.RootService
/**
 * class to implement functions for ExpandZoo Move
 */
class ExpandZoo : Move {

    /**
     * performMove dimply calls on the function that needs to be implemented mainly from the playerActionService.
     * it is required so that the AI-Bot can perform moves and actions on its own.
     */
    override fun performMove(rootService: RootService) {
        rootService.playerActionService.expandZoo()
    }

    /**
     * toHintString simply returns a string to be used as a hint during when ExpandZoo
     * action takes place
     */
    override fun toHintString(rootService: RootService): String {
        return "Expand Zoo"
    }
}