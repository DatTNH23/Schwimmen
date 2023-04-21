package service

import entity.*


/**
 * Provide function to play the game
 */

class GameService(private val rootService : RootService): AbstractRefreshingService() {

    /**
     * Starts a new game (overwriting a currently active one, if it exists)
     * @param playersName list name of the players with their cards;
     * @param customizedCards optional list of cards to play with.
     * If parameter is not provided, a random deck of 32 cards will be used.
     */
    fun startNewGame(playersName: MutableList<String>, customizedCards : List<Card> = defaultRandomCardList()){
        // if we want to customize the card, then do this, otherwise it will be random
        require(playersName.size > 1){
            "We need at least 2 player to start the game"
        }
        var ithCard = 0
        val playerList = mutableListOf<Player>()
        // distribute Cards from allCards to player cards
        for (name in playersName){
            val playerCards = mutableListOf<Card>()
            for (c in 1..3){
                playerCards.add(customizedCards[ithCard])
                ++ithCard
            }
            // create new player and add it to the list of the players
            val newPlayer =  Player(name,playerCards)
            playerList.add(newPlayer)
        }
        // Add card to the middle
        val carInMid = mutableListOf<Card>()
        for (c in 1..3){
            carInMid.add(customizedCards[ithCard])
            ++ithCard
        }
        val game = Game(playerList,carInMid,customizedCards)
        rootService.currentGame = game
        onAllRefreshables { refreshAfterStartNewGame() }

    }

    /**
     * renew the middle cards
     */
    fun exchangeMidCards(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        if (game.ithCard > 29) {
            // game will end
            onAllRefreshables { refreshAfterGameEnd() }
        }
        else
        if (game.passCount == game.playerList.size && !isGameEnded()){
            // reset passCount
            game.passCount = 0
            println(game.cardStack)
            for (c in 0..2){
                game.cardInMid[c] = game.cardStack[game.ithCard]
                ++game.ithCard
            }
        }

    }

    /**
     * transform the value of the card into integer
     */
    private fun transformValueCard(x : CardValue):Double{
        return when (x){
            CardValue.SEVEN -> 7.0
            CardValue.EIGHT -> 8.0
            CardValue.NINE -> 9.0
            CardValue.ACE -> 11.0
            else -> 10.0
        }
    }

    /**
     * calculate the score of a player
     */
    fun calculateScore(player : Player):Double{
        val countSuit : List<MutableList<Double>> = listOf(mutableListOf(), mutableListOf(),
            mutableListOf(), mutableListOf())

        // record number of a suits appear
        // 0 CLUBS -> "♣" 1 SPADES -> "♠"
        // 2 HEARTS -> "♥" 3 DIAMONDS -> "♦"
        for (card in player.handCards){
            val valueOfCard : Double = transformValueCard(card.value)
            when (card.suit){
                CardSuit.CLUBS -> countSuit[0].add(valueOfCard)
                CardSuit.SPADES -> countSuit[1].add(valueOfCard)
                CardSuit.HEARTS -> countSuit[2].add(valueOfCard)
                CardSuit.DIAMONDS -> countSuit[3].add(valueOfCard)
            }
        }
        var maxScore = 0.0
        if (player.handCards[0].value == player.handCards[1].value
            && player.handCards[1].value == player.handCards[2].value) maxScore = 30.5
        else{
            // calculate the points of each suit
            for (listSuit in countSuit){
                var sum = 0.0
                for (suitVal in listSuit) sum += suitVal
                if (sum > maxScore) maxScore = sum
            }
        }


        return maxScore
    }
    /**
     * to give a list of playerScore back
     */
    fun calculatePlayerScores() : MutableList<Double>{
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        val scoreList = mutableListOf<Double>()
        for (player in game.playerList){
            scoreList.add(calculateScore(player))
        }
        return scoreList
    }
    /**
     * check if the game can be ended
     * Game will end when one player has knocked,
     * and other players have finished their last turn
     * OR
     * Game will end when all players have passed,
     * there are not enough cards left in the draw pile to refill the middle with three cards
     */
    fun isGameEnded() : Boolean{
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        // when there are not enough cards to renew the card in the middle
        if (game.ithCard > 29 || game.whoKnocked == game.indexCurrentPlayer) return true
        return false
    }
    fun nextPlayer(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        game.indexCurrentPlayer = (game.indexCurrentPlayer + 1) % game.playerList.size
        onAllRefreshables {refreshAfterNextPlayer()}
    }
    /**
     * Creates a shuffled 32 cards list of all four suits and cards
     * from 7 to Ace
     */
    private fun defaultRandomCardList() = List(32) { index ->
        Card(
            CardSuit.values()[index / 8],
            CardValue.values()[(index % 8)]
        )
    }.shuffled()

}