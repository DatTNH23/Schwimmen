package service

import entity.Player
import view.Refreshable
/**
 * [Refreshable] implementation that refreshes nothing, but remembers
 * if a refresh method has been called (since last [reset])
 */

class TestRefreshable : Refreshable {
    var refreshAfterGameEndCalled : Boolean = false
        private set
    var refreshAfterSwapCardCalled : Boolean = false
        private set
    var refreshAfterNextPlayerCalled : Boolean = false
        private set
    var refreshAfterStartNewGameCalled : Boolean = false
        private set
    var refreshAfterPlayerKnockCalled : Boolean = false
        private set
    /**
     * resets all *Called properties to false
     */
    fun reset(){
        refreshAfterGameEndCalled = false;
        refreshAfterSwapCardCalled = false;
        refreshAfterNextPlayerCalled = false;
        refreshAfterStartNewGameCalled = false;
        refreshAfterPlayerKnockCalled = false;
    }

    override fun refreshAfterGameEnd() {
        refreshAfterGameEndCalled = true
    }

    override fun refreshAfterSwapCard(player: Player) {
        refreshAfterSwapCardCalled = true
    }

    override fun refreshAfterNextPlayer() {
        refreshAfterNextPlayerCalled = true
    }

    override fun refreshAfterPlayerKnock(player: Player) {
        refreshAfterPlayerKnockCalled = true
    }

    override fun refreshAfterStartNewGame() {
        refreshAfterStartNewGameCalled = true;
    }
}