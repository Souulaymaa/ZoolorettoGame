package gameai.moveoracle

import gamemockup.ZoolorettoGameStateMockups
import org.junit.jupiter.api.Test
import service.GameStateService.Companion.deepZoolorettoCopy
import kotlin.test.assertNotSame
import kotlin.test.assertTrue

class DeepZoolorettoCopyTest {
    @Test
    fun testIfCopyIsFoundInOriginalList(){
        val original = ZoolorettoGameStateMockups.twoPlayersZoolorettoGameStateFactory()
        val copy = deepZoolorettoCopy(original)
        val originalPlayer = original.players.peek()
        val copiedPlayer = copy.players.peek()

        assertTrue { original.players.contains(copiedPlayer) }
        assertNotSame(copiedPlayer, originalPlayer)
    }
}