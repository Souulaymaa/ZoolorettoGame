package service

import entity.*

/**
 * service layer class to calculate the score of each player and gives back a high score list.
 *
 * @param rootService
 */
class ScoreService(private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * Calculates the player's score
     * @return a score of the player
     */
    fun determineScore(player : Player) : Int {
        var score = calculateBarnScore(player) + calculateVendingStalls(player)
        player.playerEnclosure.forEach {
            score += calculateEnclosureScore(it)
        }
        player.score = score
        return score
    }

    /**
     * this method determines the winner of the zooloretto game.
     * We sort the players in descending order of the score.
     * Then we put all players with the same score in a draw list (the first player is always in draw list).
     * If there are more than one player in draw list, then we sort draw list in descending order of the coins.
     * If the first two players have the same coins, the return null, and it is tied.
     * If not, then return the first player in draw list.
     *
     * @return null, when tied, or a player, when there is only one winner
     */
    fun determineWinner(): Player? {
        val game = rootService.zoolorettoGame!!.currentGameState
        require(game.players.size in 2..5) { "there must be from two to five players!" }

        val playerList = game.players.toMutableList()
        val drawList = arrayListOf<Player>()

        playerList.sortedByDescending { it.score }

        playerList.forEach {
            if (it.score == playerList[0].score) {
                drawList.add(it)
            }
        }
        if (drawList.size > 1) {
            drawList.sortedByDescending { it.coins }
            if (drawList[0].coins == drawList[1].coins) return null
        }
        return drawList[0]
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
        else if (enclosure.animalTiles.size + 1 == enclosure.maxAnimalSlots) {
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
        var score = 0

        val vendingStall1 = arrayListOf<VendingStall>()
        val vendingStall2 = arrayListOf<VendingStall>()
        val vendingStall3 = arrayListOf<VendingStall>()
        val vendingStall4 = arrayListOf<VendingStall>()

        player.barn.vendingStalls.forEach {
            if (it == VendingStall(StallType.VENDING1)) {
                vendingStall1.add(VendingStall(StallType.VENDING1))
            }
            else if (it == (VendingStall(StallType.VENDING2))) {
                vendingStall2.add(VendingStall(StallType.VENDING2))
            }
            else if (it == (VendingStall(StallType.VENDING3))) {
                vendingStall3.add(VendingStall(StallType.VENDING3))
            }
            else {
                vendingStall4.add(VendingStall(StallType.VENDING4))
            }
        }
        val vendingStallList = arrayListOf(vendingStall1, vendingStall2, vendingStall3, vendingStall4)
        vendingStallList.forEach { if (it.isNotEmpty()) score -= 2 }

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
        val animalList = arrayListOf(flamingoList, pandaList, kamelList, schimpanseList, kaenguruList,
                                     elefantList, zebraList, leopardList)
        animalList.forEach {
            if (it.isNotEmpty()) {
                score -= 2
            }
        }
        return score
    }
    private fun calculateVendingStalls(player: Player) : Int {
        var score = 0
        val vendingStall1 = arrayListOf<VendingStall>()
        val vendingStall2 = arrayListOf<VendingStall>()
        val vendingStall3 = arrayListOf<VendingStall>()
        val vendingStall4 = arrayListOf<VendingStall>()

        player.playerEnclosure.forEach {
            if (it.vendingStalls.contains(VendingStall(StallType.VENDING1))) {
                vendingStall1.add(VendingStall(StallType.VENDING1))
            }
            if (it.vendingStalls.contains(VendingStall(StallType.VENDING2))) {
                vendingStall2.add(VendingStall(StallType.VENDING2))
            }
            if (it.vendingStalls.contains(VendingStall(StallType.VENDING3))) {
                vendingStall3.add(VendingStall(StallType.VENDING3))
            }
            if (it.vendingStalls.contains(VendingStall(StallType.VENDING4))) {
                vendingStall4.add(VendingStall(StallType.VENDING4))
            }
        }

        val vendingStallList = arrayListOf(vendingStall1, vendingStall2, vendingStall3, vendingStall4)
        vendingStallList.forEach { if (it.isNotEmpty()) score += 2 }

        return score
    }
}