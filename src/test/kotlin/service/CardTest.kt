package service
import entity.*
import kotlin.test.*

/**
 * Test whether the program initialize valid Cards
 */
class CardTest{
    // Some card to perform the test
    private val sevenOfSpades = Card(CardSuit.SPADES, CardValue.SEVEN)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val otherQueenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val aceOfDiamonds = Card(CardSuit.DIAMONDS, CardValue.ACE)


    // unicode characters for the suits, as those should be used by [WarCard.toString]
    private val heartsChar = '\u2665' // ♥
    private val diamondsChar = '\u2666' // ♦
    private val spadesChar = '\u2660' // ♠
    private val clubsChar = '\u2663' // ♣

    /**
     * Check if to String produces the correct strings for some test cards
     * of all four suits.
     */
    @Test
    fun testToString(){
        assertEquals(spadesChar + "7", sevenOfSpades.toString())
        assertEquals(clubsChar + "J", jackOfClubs.toString())
        assertEquals(heartsChar + "Q", queenOfHearts.toString())
        assertEquals(diamondsChar + "A", aceOfDiamonds.toString())
    }

    /**
     * Check if toString produces a 2 character string for every possible card
     * except the 10 (for which length=3 is ensured)
     */
    @Test
    fun testToStringLength() {
        CardSuit.values().forEach { suit ->
            CardValue.values().forEach { value ->
                if (value == CardValue.TEN)
                    assertEquals(3, Card(suit, value).toString().length)
                else
                    assertEquals(2, Card(suit, value).toString().length)
            }
        }
    }


    /**
     * Check if two cards with the same CardSuit/CardValue combination are equal
     * in the sense of the `==` operator, but not the same in the sense of
     * the `===` operator.
     */
    @Test
    fun testEquals() {
        assertEquals(queenOfHearts, otherQueenOfHearts)
        assertNotSame(queenOfHearts, otherQueenOfHearts)
    }

}
