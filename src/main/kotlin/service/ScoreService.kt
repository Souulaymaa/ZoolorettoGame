package service

import entity.*

class ScoreService(val rootService: RootService) : AbstractRefreshingService() {
    /**
     * Calculates the player's score
     * @return a score of the player
     */
    fun determineScore(player : Player) : Int {
        var score = 0
        val vendingStallCount = ArrayList<VendingStall>()
        player.playerEnclosure.forEach {
            score += calculateEnclosureScore(it)
        }

        for(enclosure in player.playerEnclosure){
            for (vendingStall in enclosure.vendingStalls){
                vendingStallCount.add(vendingStall)
            }
        }
        if (vendingStallCount.contains(VendingStall(StallType.VENDING1))){
            score += 2
        }
        if (vendingStallCount.contains(VendingStall(StallType.VENDING2))){
            score += 2
        }
        if (vendingStallCount.contains(VendingStall(StallType.VENDING3))){
            score += 2
        }
        if (vendingStallCount.contains(VendingStall(StallType.VENDING4))){
            score += 2
        }
        score += calculateBarnScore(player)
        return score
    }

    /**
     * Calculates the ranking of players.
     * @return a sorted map of all player with the score of each player
     */

    //please change this method to determine winner
    fun determineHighscore (players: List<Player>): Map<Player, Int> {
        require(players.size in 2 .. 5)

        val points = mutableMapOf<Player, Int>()

        for (i in players.indices){
            points[players[i]] = determineScore(players[i])
        }

        val sortedPoints: MutableMap<Player, Int> = LinkedHashMap()
        points.entries.sortedBy { it.value }.forEach { sortedPoints[it.key] = it.value }

        return sortedPoints
    }

    /**
     * This method calculate the score in an enclosure
     *
     * @param [enclosure] the enclosure that we calculate the score in it
     * @return the total points in the enclosure
     * Rules:
     * a) For a full enclosure (all spaces filled with animals), the player earns the higher
     * of the two point values shown in the enclosure.
     * b) For an enclosure with 1 empty space (all but 1 space filled with animals),
     * the player earns the lower of the two point values shown in the enclosure.
     * c) For an enclosure with two or more empty spaces, the player only scores points
     * if he has a vending stall on at least one of the stall spaces associated with the enclosure.
     * In this case, the player scores 1 point for each animal in the enclosure.
     * d) If a player has an enclosure with two or more empty spaces and no vending stall
     * in the stall spaces that are associated with the enclosure, he scores no points for the enclosure
     */
    private fun calculateEnclosureScore(enclosure: Enclosure) : Int {

        var score = 0

        if (enclosure.animalTiles.size == enclosure.maxAnimalSlots) {
            score += enclosure.pointValues.first
        }
        else if (enclosure.animalTiles.size == enclosure.maxAnimalSlots - 1) {
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
     * Rules:
     * For each vending stall type on stall spaces, the player receives 2 points.
     * For each vending stall type in his barn, the player receives minus 2 points.
     * For each animal type in his barn, the player receives minus 2 points.
     * Example: Claus has 3 elephants in his barn and receives 2 minus points for them.
     */
    private fun calculateBarnScore(player: Player) : Int {
        //you must also check here the vending stall type like above or do it with lists as well like you did with animals
        var score = 0
        val vendingStallCount = ArrayList<VendingStall>()
        for (vendingStall in player.barn.vendingStalls){
            vendingStallCount.add(vendingStall)
        }


        if (vendingStallCount.contains(VendingStall(StallType.VENDING1))){
            score -= 2
        }
        if (vendingStallCount.contains(VendingStall(StallType.VENDING2))){
            score -= 2
        }
        if (vendingStallCount.contains(VendingStall(StallType.VENDING3))){
            score -= 2
        }
        if (vendingStallCount.contains(VendingStall(StallType.VENDING4))){
            score -= 2
        }

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
                score -= 2
            }
        }
        return score
    }
}