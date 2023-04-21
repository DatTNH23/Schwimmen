package view

import entity.Card
import entity.Game
import entity.Player
import service.CardImageLoader
import service.GameService
import service.RootService
import tools.aqua.bgw.animation.DelayAnimation
import tools.aqua.bgw.components.container.LinearLayout
import tools.aqua.bgw.components.gamecomponentviews.CardView
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.core.BoardGameScene
import tools.aqua.bgw.util.BidirectionalMap
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color


/**
 * This is the main thing for the Schwimm game. The scene shows the complete table at once.
 * Player 1 "sits" is on the bottom half of the screen, other players sit around
 * Each player has four activities: Knock, pass, swap one card and swap all cards
 * It has a small board to show who is playing and how much score he has
 */
class GameScene (private val rootService: RootService) : BoardGameScene(1920, 1080), Refreshable {
    private val backSide = ImageVisual(CardImageLoader().backImage)
    private val buttonColor = ColorVisual(123, 31, 162)
    private val cardImageLoader = CardImageLoader()
    //Text field to show the turn of player and score
    private val showTurn = Label(height = 100, width = 600,
        posX = 0, posY = 10, font = Font(size = 45, color = Color.WHITE))
    private val showPoints = Label(height = 100, width = 600,
         posX = 0, posY = 50, font = Font(size = 30, color = Color.WHITE))
    // show Someone knocked
    private val showKnock = Label(height = 100, width = 600,
        posX = 30, posY = 100, font = Font(size = 60, color = Color.RED))
    // currents (bottom of screen) cards
    private val playerCards = LinearLayout<CardView>(height = 200, width = 1000,
        posX = 600, posY = 830, spacing = 150)
    //cards in middle
    private val cardsInMid = LinearLayout<CardView>(height = 200, width = 1000,
        posX = 570, posY = 420, spacing = 200)
    // another cards to present another players
    // cards on top screen
    private val p1LeftCard = CardView(posX = 600, posY = 70,front = backSide)
    private val p1MidCard = CardView(posX = 895, posY = 70,front = backSide)
    private val p1RightCard = CardView(posX = 1190, posY = 70,front = backSide)
    // cards on the left side
    private val p2LeftCard = CardView(posX = 100, posY = 300,front = backSide)
    private val p2MidCard = CardView(posX = 100, posY = 500,front = backSide)
    private val p2RightCard = CardView(posX = 100, posY = 700,front = backSide)
    // cards on the right side
    private val p3LeftCard = CardView(posX = 1600, posY = 300,front = backSide)
    private val p3MidCard = CardView(posX = 1600, posY = 500,front = backSide)
    private val p3RightCard = CardView(posX = 1600, posY = 700,front = backSide)
    //stack of card
    private val midCardsPresent = CardView(posX = 895, posY = 370,front = backSide)

    //Knock Button
    private val knockButton = Button(
        width = 200, height = 50,
        posX = 350, posY =  850,
        text = "Knock", font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                rootService.playerActionService.knock()
            }
            isVisible = false
        }
    }
    //Pass Button
    private val passButton = Button(
        width= 200, height = 50,
        posX = 350, posY =  950,
        text = "Pass",
        font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                rootService.playerActionService.pass()
            }
        }
    }
    //Swap One Button
    private val swapButton = Button(
        width= 200, height = 50,
        posX = 1350, posY =  850,
        text = "Swap",
        font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                try {
                    rootService.playerActionService.swapOneCard()
                } catch (err : Exception) {
                    throw Exception("Please choose a card on your hand and a card on the table")
                }

            }
        }
    }
    //Swap All Button
    private val swapAllButton = Button(
        width= 200, height = 50,
        posX = 1350, posY =  950,
        text = "Swap All",
        font = Font(32),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            rootService.currentGame?.let {
                rootService.playerActionService.swapAllCards()
                updateShowBoard(rootService.currentGame!!)
            }
        }

    }

    /**
     * structure to hold pairs of (card, cardView) that can be used
     *
     * 1. to find the corresponding view for a card passed on by a refresh method (forward lookup)
     *
     * 2. to find the corresponding card to pass to a service method on the occurrence of
     * ui events on views (backward lookup).
     */
    private val cardMap: BidirectionalMap<Card, CardView> = BidirectionalMap()

    private fun rotateCards(){
        p2MidCard.rotate(90)
        p2LeftCard.rotate(90)
        p2RightCard.rotate(90)

        p3LeftCard.rotate(90)
        p3MidCard.rotate(90)
        p3RightCard.rotate(90)
    }
    init {
        background = ImageVisual("background.png")
        rotateCards()
        addComponents(
            playerCards,
            p1LeftCard, p1MidCard, p1RightCard,
            p2LeftCard, p2MidCard, p2RightCard,
            p3LeftCard, p3MidCard, p3RightCard,
            midCardsPresent,
            cardsInMid,
            knockButton,passButton,swapButton,swapAllButton,
            showPoints,showTurn,showKnock
        )
    }

    override fun refreshAfterStartNewGame() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        cardMap.clear()
        updateCardsInMiddle(game,cardImageLoader)
        updateShowBoard(game)
        updateCards(game,cardImageLoader)
    }

    override fun refreshAfterNextPlayer() {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }

        val delay = DelayAnimation(500)
        delay.onFinished = {
            updateShowBoard(game)
            updateCards(game,cardImageLoader)
        }
        playAnimation(delay)
    }

    override fun refreshAfterGameEnd() {
        knockButton.isVisible = true
    }

    override fun refreshAfterSwapCard(player: Player) {
        val game = rootService.currentGame
        checkNotNull(game) { "No started game found." }
        val cardImageLoader = CardImageLoader()
        updateCards(game,cardImageLoader)
        updateCardsInMiddle(game,cardImageLoader)
        println("has refreshed after swap card")
    }
    /**
     * initialize Cards in  middle
     */
    private fun updateCardsInMiddle(game:Game, cardImageLoader: CardImageLoader){
        cardsInMid.clear()
        var chosenCardIndex = -1
        game.cardInMid.forEach {
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(it.suit, it.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            cardView.showFront()
            cardsInMid.add(cardView)
            cardMap.add(it to cardView)
        }
        cardsInMid.forEach {
            ++chosenCardIndex
            addTableCardViewClickListener(it,chosenCardIndex,cardsInMid)
        }

    }
    /**
     * Update the card of current Player
     */
    private fun updateCards(game:Game, cardImageLoader: CardImageLoader){
        playerCards.clear()
        var cardIndex = -1
        game.playerList[game.indexCurrentPlayer].handCards.forEach{
            val cardView = CardView(
                height = 200,
                width = 130,
                front = ImageVisual(cardImageLoader.frontImageFor(it.suit, it.value)),
                back = ImageVisual(cardImageLoader.backImage)
            )
            cardView.showFront()
            playerCards.add(cardView)
            cardMap.add(it to cardView)
        }
        playerCards.forEach {
            cardIndex++
            addPlayerCardViewClickListener(it,cardIndex,playerCards)
        }
    }
    /**
     * Update the score and the name of current player
     */
    private fun updateShowBoard(game:Game){
        showTurn.text = "${game.playerList[game.indexCurrentPlayer].name}'s turn"
        val score = GameService(rootService).calculateScore(game.playerList[game.indexCurrentPlayer])
        showPoints.text = "$score Points"
        if (game.whoKnocked != -1){
            showKnock.text = "${game.playerList[game.whoKnocked].name} knocked"
        } else showKnock.text = ""

    }

    private fun addTableCardViewClickListener(cardView: CardView, cardIndex: Int, tableCardViews: LinearLayout<CardView>) {
        cardView.apply {
            onMouseClicked = { selectTableCardAction(cardIndex, tableCardViews) }
        }
    }

    private fun selectTableCardAction(chosenCardIndex: Int, tableCardViews: LinearLayout<CardView>) {
        resizeCardViewsToDefault(tableCardViews)

        rootService.currentGame?.indexChosenCard = chosenCardIndex
        var cardIndex = -1
        tableCardViews.forEach {
            ++cardIndex
            if (cardIndex == chosenCardIndex) it.resize(width = 169, height=260)
        }
    }


    private fun addPlayerCardViewClickListener(cardView: CardView, cardIndex: Int, playerCardViews: LinearLayout<CardView>) {
        cardView.apply {
            onMouseClicked = { selectPlayerCardAction(cardIndex, playerCardViews) }
        }
    }

    private fun selectPlayerCardAction(chosenCardIndex: Int, playerCardViews: LinearLayout<CardView>) {
        val game = rootService.currentGame
        checkNotNull(game)
        resizeCardViewsToDefault(playerCardViews)
        val indexCurrentPlayer = game.indexCurrentPlayer
        game.playerList[indexCurrentPlayer].indexChosenCard = chosenCardIndex
        var cardIndex = -1
        playerCardViews.forEach {
            ++cardIndex
            if (cardIndex == chosenCardIndex) it.resize(width = 169, height=260)
        }
    }

    private fun resizeCardViewsToDefault(cardViews: LinearLayout<CardView>) {
        cardViews.forEach {
            it.resize(width = 130, height=200)
        }
    }
}