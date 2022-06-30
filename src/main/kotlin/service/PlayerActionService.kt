package service

import entity.*

/**
 * Service layer class that provides the logic of all player moves in zooloretto game.
 *
 * @param rootService an Object from [RootService] class
 */
class PlayerActionService(private val rootService: RootService) : AbstractRefreshingService() {

}
