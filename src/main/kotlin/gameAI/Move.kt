package gameAI

interface Move {

    fun calculateScore() : Int{
        throw NotImplementedError()
    }
    fun performMove(){}

    fun toHintString() : String{
        throw NotImplementedError()
    }

}