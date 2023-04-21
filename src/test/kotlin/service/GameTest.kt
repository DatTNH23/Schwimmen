package service
import entity.*
import kotlin.test.*

/**
 * to test all the methods and properties of Game
 */
class GameTest {

    // Give information for the first player
    private val sevenOfSpades = Card(CardSuit.SPADES, CardValue.SEVEN)
    private val jackOfClubs = Card(CardSuit.CLUBS, CardValue.JACK)
    private val queenOfHearts = Card(CardSuit.HEARTS, CardValue.QUEEN)
    private val handCardsPlayer1 : MutableList<Card> = mutableListOf(sevenOfSpades,jackOfClubs,queenOfHearts)
    private val player1 = Player("Adam",handCardsPlayer1)

    // Give information for the second player
    private val eightOfSpades = Card(CardSuit.SPADES, CardValue.EIGHT)
    private val tenOfClubs = Card(CardSuit.CLUBS, CardValue.TEN)
    private val aceOfHearts = Card(CardSuit.HEARTS, CardValue.ACE)
    private val handCardsPlayer2: MutableList<Card> = mutableListOf(eightOfSpades,tenOfClubs,aceOfHearts)
    private val player2 = Player("Ben",handCardsPlayer2)

    //Create the list of card in the middle
    private val cardInMiddle : MutableList<Card> = mutableListOf(tenOfClubs,queenOfHearts,sevenOfSpades)
    // Create the list of players
    private val listOfPlayers: List<Player> = listOf(player1,player2)
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
        // player 4 cards
        Card(CardSuit.HEARTS, CardValue.TEN),
        Card(CardSuit.HEARTS, CardValue.JACK),
        Card(CardSuit.HEARTS, CardValue.NINE),
        // the first 3 cards in mid
        Card(CardSuit.DIAMONDS, CardValue.JACK),
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
        Card(CardSuit.DIAMONDS, CardValue.TEN),
        Card(CardSuit.SPADES, CardValue.KING),
        Card(CardSuit.DIAMONDS, CardValue.ACE),
        Card(CardSuit.SPADES, CardValue.ACE)

    )
    private val newGame = Game(listOfPlayers,cardInMiddle,cards)

    /**
     * to test whether game is successfully initialized
     */
    @Test
    fun testCreateGame(){
        assertEquals(0,newGame.passCount)
        assertEquals(listOfPlayers,newGame.playerList)
    }

    /**
     * to test whether the passCount and passKnockCount work correctly
     */
    @Test
    fun testCount(){
        for (number in 1..4){
            ++newGame.passCount
            assertEquals(number,newGame.passCount)
            newGame.indexCurrentPlayer = (newGame.indexCurrentPlayer + 1) % newGame.playerList.size
            assertEquals(number%newGame.playerList.size,newGame.indexCurrentPlayer);
        }
    }

    /**
     * to test if indexCurrentPlayer, indexChosenCard work correctly
     */
    @Test
    fun testIndex(){
        for (index in 0..3){
            newGame.indexCurrentPlayer = index
            newGame.indexChosenCard = index%3
            assertEquals(index,newGame.indexCurrentPlayer)
            assertEquals(index%3,newGame.indexChosenCard)
        }
    }

    /**
     * to test if the cards are assigned correctly
     */
    @Test
    fun testCardInMid(){
        assertEquals(cardInMiddle,newGame.cardInMid)
    }

}