package view

import entity.Player

/**
 * This interface provides a mechanism for the service layer classes to communicate
 * (usually to the view classes) that certain changes have been made to the entity
 * layer, so that the user interface can be updated accordingly.
 *
 * Default (empty) implementations are provided for all methods, so that implementing
 * UI classes only need to react to events relevant to them.
 *
 *
 */
interface Refreshable {
    /**
     * perform refreshes that are necessary after a new game started
     */
    fun refreshAfterStartNewGame(){}
    /**
     * perform refreshes that are necessary after the last round was played
     */
    fun refreshAfterGameEnd(){}
    /**
     * perform refreshes that are necessary after a player swap card
     */
    fun refreshAfterSwapCard(player: Player){}
    /**
     * perform refreshes that are necessary after a player finish his turn
     */
    fun refreshAfterNextPlayer(){}
    /**
     * perform refreshes that are necessary after a player knocked
     */
    fun refreshAfterPlayerKnock(player: Player){}
}