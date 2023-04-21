package service

import entity.Card
import tools.aqua.bgw.animation.DelayAnimation

/**
 * Service layer class that provides the logic for the four possible actions a player
 * can take in game: knock, pass, swapAllCard, swapOneCard
 */

class PlayerActionService (private val rootService: RootService) : AbstractRefreshingService() {
    /**
     * knock activity
     */
    fun knock(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        game.whoKnocked = game.indexCurrentPlayer
        // reset passCount
        game.passCount = 0
        // reset index of the chosen card
        game.indexChosenCard = -1
        game.playerList[game.indexCurrentPlayer].indexChosenCard = - 1
        onAllRefreshables {refreshAfterPlayerKnock(game.playerList[game.indexCurrentPlayer])}
        rootService.gameService.nextPlayer()
        if (rootService.gameService.isGameEnded()) onAllRefreshables { refreshAfterGameEnd() }
    }

    /**
     * pass activity
     */
    fun pass(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        ++game.passCount
        // reset index of the chosen card
        game.indexChosenCard = -1
        game.playerList[game.indexCurrentPlayer].indexChosenCard = - 1
        if (game.passCount == game.playerList.size){
            GameService(rootService).exchangeMidCards()
            onAllRefreshables { refreshAfterSwapCard(game.playerList[game.indexCurrentPlayer]) }
        }
        // go to next player
        rootService.gameService.nextPlayer()
        if (rootService.gameService.isGameEnded()) onAllRefreshables { refreshAfterGameEnd() }
    }

    /**
     * swapOneCard activity
     */
    fun swapOneCard(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        // reset passCount
        game.passCount = 0
        // call the currentPlayer
        val currentPlayer = game.playerList[game.indexCurrentPlayer]
        // swap cards
        val tempCard : Card = currentPlayer.handCards[currentPlayer.indexChosenCard]
        currentPlayer.handCards[currentPlayer.indexChosenCard] = game.cardInMid[game.indexChosenCard]
        game.cardInMid[game.indexChosenCard] = tempCard
        onAllRefreshables {refreshAfterSwapCard(game.playerList[game.indexCurrentPlayer])}
        rootService.gameService.nextPlayer()
        if (rootService.gameService.isGameEnded()) onAllRefreshables { refreshAfterGameEnd() }
    }

    /**
     * swapAllCards activity
     */
    fun swapAllCards(){
        val game = rootService.currentGame
        checkNotNull(game) { "No game currently running."}
        // reset passCount
        game.passCount = 0
        // reset index of the chosen card
        game.indexChosenCard = -1
        game.playerList[game.indexCurrentPlayer].indexChosenCard = - 1
        // call the currentPlayer
        val currentPlayer = game.playerList[game.indexCurrentPlayer]
        //swap all card
        for (cardIndex in 0..2) {
            val temp : Card = currentPlayer.handCards[cardIndex]
            currentPlayer.handCards[cardIndex] = game.cardInMid[cardIndex]
            game.cardInMid[cardIndex] = temp
        }
        onAllRefreshables { refreshAfterSwapCard(game.playerList[game.indexCurrentPlayer])}
        rootService.gameService.nextPlayer()
        if (rootService.gameService.isGameEnded()) onAllRefreshables { refreshAfterGameEnd() }
    }
}