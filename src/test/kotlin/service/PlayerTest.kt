package service
import entity.*
import kotlin.test.*

/**
 * To test all the properties and method of a player
 */
class PlayerTest {
    // Create 3 cards as card in Hand
    private val sevenOfSpades = Card(CardSuit.SPADES, CardValue.SEVEN)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    // List for Cards
    private val listOfCards : MutableList<Card> = mutableListOf(sevenOfSpades,jackOfClubs,queenOfHearts)
    // Create a player to perform the test
    private val newPlayer = Player("Adam", listOfCards)

    /**
     * to test whether a player is successfully initialized
     */
    @Test
    fun testCreatePlayer(){
        // test if the name is correct
        assertEquals("Adam",newPlayer.name)
        // test if the first card is correct
        assertEquals(sevenOfSpades,newPlayer.handCards[0])
        // test if the second card is correct
        assertEquals(jackOfClubs,newPlayer.handCards[1])
        // test if the third card is correct
        assertEquals(queenOfHearts,newPlayer.handCards[2])
    }

    /**
     * to test whether toString method works
     */
    @Test
    fun testToString(){
        assertEquals("Adam: $listOfCards",newPlayer.toString())
    }

    /**
     * to test whether the point of the player is valid
     */
    @Test
    fun testPoints(){
        assertTrue(newPlayer.points < 32);
    }

    /**
     * to test whether the hasKnocked is valid
     */
    @Test
    fun testHasKnocked(){
        newPlayer.hasKnocked = true;
        assertEquals(newPlayer.hasKnocked, true);
    }
    /**
     * to test whether indexChosenCard works
     */
    @Test
    fun testIndexChosenCard(){
        for (index in 0..2){
            newPlayer.indexChosenCard = index
            assertEquals(index,newPlayer.indexChosenCard)
        }
    }
}