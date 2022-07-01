package service

import entity.Animal
import entity.Enclosure
import entity.Player
import entity.Species

class ScoreService(val rootService: RootService) : AbstractRefreshingService() {
    fun determineScore(player : Player) : Int {
        var score = calculateBarnScore(player)
        player.playerEnclosure.forEach {
            score += calcuateEnclosureScore(it)
        }
        return score
    }
    /**
     * This method calculate the score in an enclosure
     *
     * @param [enclosure] the enclosure that we calculate the score in it
     * @return the total points in the enclosure
     */
    private fun calcuateEnclosureScore(enclosure: Enclosure) : Int {
        var score = enclosure.vendingStalls.size * 2

        if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
            score += enclosure.pointValues.first
        }
        else if (enclosure.animalTiles.size - 1 == enclosure.maxAnimalSlots) {
            score += enclosure.pointValues.second
        }
        else if (enclosure.vendingStalls.size != 0) {
            score += enclosure.animalTiles.size
        }
        return score
    }

    /**
     * This method calculate the score in the player's barn
     *
     * @param [player] the player who we calculate the score in his barn
     * @return the total points in the barn
     */
    private fun calculateBarnScore(player: Player) : Int {
        var score = player.barn.vendingStalls.size * 2
        val flamingoList = arrayListOf<Animal>()
        val pandaList = arrayListOf<Animal>()
        val kamelList = arrayListOf<Animal>()
        val schimpanseList = arrayListOf<Animal>()
        val leopardList = arrayListOf<Animal>()
        val zebraList = arrayListOf<Animal>()
        val elefantList = arrayListOf<Animal>()
        val kaenguruList = arrayListOf<Animal>()
        player.barn.animalTiles.forEach {
            when(it.species) {
                Species.F -> flamingoList.add(it)
                Species.P -> pandaList.add(it)
                Species.K -> kamelList.add(it)
                Species.S -> schimpanseList.add(it)
                Species.U -> kaenguruList.add(it)
                Species.E -> elefantList.add(it)
                Species.Z -> zebraList.add(it)
                Species.L -> leopardList.add(it)
            }
        }
        val animalList : ArrayList<List<Animal>> = arrayListOf()
        animalList.add(flamingoList)
        animalList.add(pandaList)
        animalList.add(kamelList)
        animalList.add(schimpanseList)
        animalList.add(kaenguruList)
        animalList.add(elefantList)
        animalList.add(zebraList)
        animalList.add(leopardList)
        animalList.forEach {
            if (it.isNotEmpty()) {
                score += 2
            }
        }
        return -1 * score
    }
}