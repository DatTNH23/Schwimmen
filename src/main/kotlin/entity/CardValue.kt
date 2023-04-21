package entity

import java.util.*

/**
 * Enum to distinguish between the 13 possible values in a french-suited card game:
 * 7-10, Jack, Queen, King, and Ace.
 *
 * The values are ordered according to their most common ordering:
 * 7<8<9 < 10 < Jack < Queen < King < Ace
 *
 */
enum class CardValue {
    SEVEN,
    EIGHT,
    NINE,
    TEN,
    JACK,
    QUEEN,
    KING,
    ACE,
    ;

    /**
     * provide a single character to represent this value.
     * Returns one of: 7/8/9/10/J/Q/K/A
     */
    override fun toString() =
        when(this) {
            SEVEN -> "7"
            EIGHT -> "8"
            NINE -> "9"
            TEN -> "10"
            JACK -> "J"
            QUEEN -> "Q"
            KING -> "K"
            ACE -> "A"
        }





}