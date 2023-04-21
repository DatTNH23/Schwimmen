package service

import entity.*
import view.Refreshable
import kotlin.test.*

/**
 * Class that provides tests for [GameService] and [PlayerActionService] (both at the same time,
 * as their functionality is not easily separable) by basically playing through some sample games.
 * [TestRefreshable] is used to validate correct refreshing behavior even though no GUI
 * is present.
 */

class ServiceTest {
    /**
     * starts a game with a static order of cards that can be used
     * in other tests to deterministically validate the outcome
     * of turns.
     *
     * The draw stacks of the resulting game are (top-of-stack cards first):
     * - player 1 : ♦7, ♠10, ♣Q Score: 10
     * - player 2 : ♥K, ♣9, ♣8 Score: 17
     * - player 3 : ♥Q ♠Q, ♦Q Score: 30.5 => Winner
     * - player 4 : ♥J, ♥10, ♥9 Score: 29
     *
     * @param refreshables refreshables to be added to the root service
     * right after its instantiation (so that, e.g., start new game will already
     * be observable)
     *
     * @return the root service holding the started game as [RootService.currentGame]
     */

    private fun setUpGame(vararg refreshables: Refreshable): RootService{
        val mc = RootService()
        refreshables.forEach { mc.addRefreshable(it) }
        val cards = listOf(
            // player 1 cards
            Card(CardSuit.CLUBS, CardValue.QUEEN),
            Card(CardSuit.SPADES, CardValue.TEN),
            Card(CardSuit.DIAMONDS, CardValue.SEVEN),
            //player 2 cards
            Card(CardSuit.CLUBS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.NINE),
            Card(CardSuit.HEARTS, CardValue.KING),
            // player 3 cards
            Card(CardSuit.DIAMONDS, CardValue.QUEEN),
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.HEARTS, CardValue.QUEEN),
            // player 4 cards
            Card(CardSuit.HEARTS, CardValue.TEN),
            Card(CardSuit.HEARTS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.NINE),
            // the first 3 cards in mid
            Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.DIAMONDS, CardValue.KING),
            // the other card
            Card(CardSuit.DIAMONDS, CardValue.NINE),
            Card(CardSuit.SPADES, CardValue.EIGHT),
            Card(CardSuit.HEARTS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.ACE),
            Card(CardSuit.SPADES, CardValue.NINE),
            Card(CardSuit.CLUBS, CardValue.ACE),
            Card(CardSuit.SPADES, CardValue.JACK),
            Card(CardSuit.HEARTS, CardValue.SEVEN),
            Card(CardSuit.CLUBS, CardValue.SEVEN),
            Card(CardSuit.CLUBS, CardValue.KING),
            Card(CardSuit.DIAMONDS, CardValue.EIGHT),
            Card(CardSuit.CLUBS, CardValue.TEN),
            Card(CardSuit.DIAMONDS, CardValue.TEN),
            Card(CardSuit.SPADES, CardValue.KING),
            Card(CardSuit.DIAMONDS, CardValue.ACE),
            Card(CardSuit.SPADES, CardValue.ACE)

        )
        mc.gameService.startNewGame(mutableListOf("A","B","C","D"),cards)
        return mc
    }
    /**
     * Tests the default case of starting a game: instantiate a [RootService] and then run
     * startNewGame on its [RootService.gameService].
     */

    @Test
    fun testStartNewGame(){
        val testRefreshable = TestRefreshable()
        val mc = RootService()
        mc.addRefreshable(testRefreshable)

        assertFalse(testRefreshable.refreshAfterStartNewGameCalled)
        assertNull(mc.currentGame)
        mc.gameService.startNewGame(mutableListOf("AA","BB","CC","DD"))
        assertTrue(testRefreshable.refreshAfterStartNewGameCalled)
        assertNotNull(mc.currentGame)
        // to check whether each player has enough 3 cards
        for (player in mc.currentGame!!.playerList){
            assertEquals(3,player.handCards.size)
        }
        // check if the middle cards have 3 cards
        assertEquals(3, mc.currentGame!!.cardInMid.size)
    }
    /**
     * Test a complete play through to test the proper game ending.
     * Check some arbitrary calls to [Refreshable] methods on the way.
     */
    @Test
    fun testGameEnd(){
        val mc = RootService()
        val testRefreshable = TestRefreshable()
        mc.addRefreshable(testRefreshable)

        mc.gameService.startNewGame(mutableListOf("A","B","C","D"))
        // there should be a game started...
        val currentGame = mc.currentGame
        assertNotNull(currentGame)
        // ... and refresh triggered
        assertTrue(testRefreshable.refreshAfterStartNewGameCalled)
        // Assume that all the players pass all the time
        // just renew the middle card until the end of the game
        mc.currentGame!!.passCount = 4
        repeat(5){
            mc.currentGame!!.passCount = 4
            mc.gameService.exchangeMidCards()
        }
        // game should not have ended yet
        // there should be 2 cards left
        assertFalse(testRefreshable.refreshAfterGameEndCalled)
        //perform the last renew
        mc.gameService.exchangeMidCards()
        assertTrue(testRefreshable.refreshAfterGameEndCalled)
        testRefreshable.reset()
        mc.currentGame!!.passCount = 1
        // Test the case when one player has knocked
        // game should not have ended yet
        // the last player in knocked turn has not finished
        assertFalse(testRefreshable.refreshAfterGameEndCalled)
        // the last player in knocked turn finished
        mc.playerActionService.pass()
        assertTrue(testRefreshable.refreshAfterGameEndCalled)
    }

    /**
     * Test if it is possible to start a game with only one or no player at all
     */
    @Test
    fun testFailStartWithOnlyOnePlayer(){
        val mc = RootService()
        val testRefreshable = TestRefreshable()
        mc.addRefreshable(testRefreshable)
        // there is no player
        assertFails {
            mc.gameService.startNewGame(mutableListOf())
        }
        // there is one player
        assertFails {
            mc.gameService.startNewGame(mutableListOf("A"))
        }
        // as the previous attempt failed, there should be no game started...
        assertNull(mc.currentGame)
        // ... and no refresh triggered
        assertFalse(testRefreshable.refreshAfterStartNewGameCalled)

    }

    /**
     * Test if CalculateScore give the correct score of players
     */
    @Test
    fun testCalculateScore(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)

        assertNotNull(mc.currentGame)
        assertEquals(mc.gameService.calculatePlayerScores(), mutableListOf(10.0,17.0,30.5,29.0))
        assertNotEquals(mc.gameService.calculatePlayerScores(), mutableListOf(17.0,17.0,30.0,29.0))
    }

    /**
     * Test if all the activities work
     */
    @Test
    fun testAllActivities(){
        val testRefreshable = TestRefreshable()
        val mc = setUpGame(testRefreshable)
        assertNotNull(mc.currentGame)

        // test the pass activity
        // nothing happens yet
        assertFalse(testRefreshable.refreshAfterNextPlayerCalled)
        assertEquals(0,mc.currentGame!!.indexCurrentPlayer)
        // it happens
        mc.playerActionService.pass()
        assertEquals(1, mc.currentGame!!.passCount)
        assertTrue (testRefreshable.refreshAfterNextPlayerCalled)

        // test the knock activity
        // nothing happens yet
        testRefreshable.reset()
        assertEquals(1,mc.currentGame!!.indexCurrentPlayer)
        assertFalse(testRefreshable.refreshAfterNextPlayerCalled)
        assertFalse(testRefreshable.refreshAfterPlayerKnockCalled)
        assertEquals (-1,mc.currentGame!!.whoKnocked)
        assertEquals(1, mc.currentGame!!.passCount)
        // it happens
        mc.playerActionService.knock()
        assertTrue(testRefreshable.refreshAfterNextPlayerCalled)
        assertTrue(testRefreshable.refreshAfterPlayerKnockCalled)
        assertEquals(1,mc.currentGame!!.whoKnocked)
        assertEquals(0, mc.currentGame!!.passCount)

        // test swap one card activity
        // nothing happens yet
        testRefreshable.reset()
        assertEquals(2,mc.currentGame!!.indexCurrentPlayer)
        assertEquals(0, mc.currentGame!!.passCount)
        assertEquals(-1,
            mc.currentGame!!.playerList[mc.currentGame!!.indexCurrentPlayer].indexChosenCard)
        assertEquals(-1,mc.currentGame!!.indexChosenCard)
        assertFalse(testRefreshable.refreshAfterNextPlayerCalled)
        assertFalse(testRefreshable.refreshAfterSwapCardCalled)
        // it happens
        // change the first card of the player to second card in middle
        mc.currentGame!!.playerList[mc.currentGame!!.indexCurrentPlayer].indexChosenCard = 0
        mc.currentGame!!.indexChosenCard = 1
        mc.playerActionService.swapOneCard()
        assertTrue(testRefreshable.refreshAfterNextPlayerCalled)
        assertTrue(testRefreshable.refreshAfterSwapCardCalled)
        val cardAfterSwap = listOf(Card(CardSuit.SPADES, CardValue.SEVEN),
            Card(CardSuit.SPADES, CardValue.QUEEN),
            Card(CardSuit.HEARTS, CardValue.QUEEN))
        val cardMidAfterSwap = listOf(Card(CardSuit.DIAMONDS, CardValue.JACK),
            Card(CardSuit.DIAMONDS, CardValue.QUEEN),
            Card(CardSuit.DIAMONDS, CardValue.KING))
        //test on the last player, not the current one
        assertEquals(mc.currentGame!!.playerList[mc.currentGame!!.indexCurrentPlayer-1].handCards,
            cardAfterSwap)
        assertEquals(mc.currentGame!!.cardInMid,cardMidAfterSwap)
        //test swap all cards activity
        // nothing happens
        testRefreshable.reset()
        assertEquals(3,mc.currentGame!!.indexCurrentPlayer)
        assertEquals(0, mc.currentGame!!.passCount)
        assertFalse(testRefreshable.refreshAfterNextPlayerCalled)
        assertFalse(testRefreshable.refreshAfterSwapCardCalled)
        // it happens
        mc.playerActionService.swapAllCards()
        assertTrue(testRefreshable.refreshAfterNextPlayerCalled)
        assertTrue(testRefreshable.refreshAfterSwapCardCalled)

        // test the last play of the knocked turn
        // assume that the player pass card
        // nothing happens yet
        testRefreshable.reset()
        assertEquals(0,mc.currentGame!!.indexCurrentPlayer)
        assertFalse(testRefreshable.refreshAfterNextPlayerCalled)
        assertFalse(testRefreshable.refreshAfterGameEndCalled)
        assertFalse(mc.gameService.isGameEnded())
        // it happens
        mc.playerActionService.pass()
        assertEquals(1, mc.currentGame!!.passCount)
        assertTrue (testRefreshable.refreshAfterNextPlayerCalled)
        assertTrue(testRefreshable.refreshAfterGameEndCalled)
        assertTrue(mc.gameService.isGameEnded())
    }
}