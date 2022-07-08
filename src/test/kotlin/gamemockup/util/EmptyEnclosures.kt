package gamemockup.util

import entity.Enclosure

class EmptyEnclosures {

    companion object {
        fun emptyEnclosureDefaultFactory(): List<Enclosure> {
            return listOf<Enclosure>(
                Enclosure(5, 1, 2, Pair(8, 5), false)
                ,Enclosure(4, 2, 1, Pair(5, 4), false)
                ,Enclosure(6, 1, 0, Pair(10, 6), false)
                ,Enclosure(5, 1, 1, Pair(9, 5), false)
            )
        }
    }
}