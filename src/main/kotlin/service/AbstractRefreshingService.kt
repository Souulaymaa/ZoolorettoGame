package service

import view.Refreshable

abstract class AbstractRefreshingService {
    private val refreshables = mutableListOf<Refreshable>()

    /**
     * adds [Refreshable] to the list when [onAllRefreshables] is used.
     */
    fun addRefreshable(newRefreshable : Refreshable) {
        refreshables += newRefreshable
    }

    /**
     * Executes the passed method on all [Refreshable]s in the service
     * class that extends this [AbstractRefreshingService]
     */
    fun onAllRefreshables(method: Refreshable.() -> Unit) =
        refreshables.forEach { it.method() }
}