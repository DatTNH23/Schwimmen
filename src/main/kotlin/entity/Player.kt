package entity
/**
 * Entity to represent a player in the game.
 * Each Player will have a[name], [points], [handCards] his own card
 * and the information whether he has knocked.
 *
 * @param name The name of the player. It cannot be empty
 * @param handCards All 3 Cards that the player has.
 * @property points The points of all his cards (min 10, max 31)
 * @property hasKnocked The status whether the players have knocked
 * @property indexChosenCard It will be the index of card that has been chosen for swapOneCard
 */
class Player(val name: String, var handCards: MutableList<Card>){
    var indexChosenCard : Int = -1
    var points : Double = 0.0
    var hasKnocked: Boolean = false
    override fun toString() = "$name: $handCards"

}