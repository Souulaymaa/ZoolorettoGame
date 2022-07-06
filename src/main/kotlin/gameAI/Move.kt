package gameAI

interface Move {

    fun performMove(){}

    fun toHintString() : String{
        throw NotImplementedError()
    }

}