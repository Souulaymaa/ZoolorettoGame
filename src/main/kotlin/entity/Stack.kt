package entity

/**
 * Class to simulate a Stack
 */
data class Stack<T>(val stackList: List<T> = listOf()) {

    /**
     * @return [Pair] of the modified [Stock] and the last Element [T]
     */
    fun pop() : Pair<Stack<T>,T>{
        val lastElement = this.stackList[this.stackList.size - 1]
        val newStock = this.copy(stackList = this.stackList.dropLast(1))
        return Pair(newStock,lastElement)
    }

    /**
     * Return the Element on top of the Stack.
     * The Element is still on top of the Stack
     *
     * @return first Element of the [Stack]
     */
    fun peek() : T{
        return this.stackList[this.stackList.size - 1]
    }

    /**
     * Puts an Element on top of the Stack
     * @return the modified [Stack]
     */
    fun push(element : T): Stack<T>{
        return this.copy(stackList = this.stackList.plus(element))
    }

    /**
     * The size of the [Stack]
     */
    fun size() = this.stackList.size
}