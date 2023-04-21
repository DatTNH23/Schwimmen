package entity
/**
 * Entity class that represents a game state of "Schwimmen".
 * This class contains the counter of the game and list of players
 *
 * @param playerList It contains all the information of players.
 * @param cardInMid It contains all the information of cards
 * The length of the playerList muss be at least 2 and maximum 4.
 * @property passCount Initially  it was with 0 value. It will count the number of player that passed.
 * @property isKnocked check if someone has knocked
 * It will count the number of player that has played his last turn.
 * @property indexChosenCard It will be the index of card that has been chosen for swapOneCard
 * @property indexCurrentPlayer the index of the current player
 *
 */
data class Game(var playerList: List<Player>, var cardInMid : MutableList<Card>, var cardStack:List<Card>){
    var passCount:Int = 0
    var whoKnocked = -1
    var indexCurrentPlayer = 0
    var indexChosenCard : Int = -1
    var ithCard : Int = 3 * playerList.size + 3
    init {
        require(playerList.size > 1){
            "There muss be at least 2 players to start the game"
        }
    }
}