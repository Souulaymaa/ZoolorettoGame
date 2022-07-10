package gameai

import service.RootService

/**
 * Interface which has two not implemented methods 1) performMove and 2) toHintString
 * these two functions will be overridden from all Move Classes which Inherit from it
 */
interface Move {

    /**
     * function that simply calls on the function that needs to be implemented mainly from the playerActionService.
     * each move inheriting from this interface will use RootService to call on the required functions
     */
    fun performMove(rootService: RootService) {}

    /**
     * toHintString simply returns a string to be used as a hint during GamePlay
     */
    fun toHintString(rootService: RootService) : String{
        throw NotImplementedError()
    }

}