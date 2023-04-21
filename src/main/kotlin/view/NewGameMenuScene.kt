package view

import service.RootService
import tools.aqua.bgw.components.uicomponents.Button
import tools.aqua.bgw.components.uicomponents.Label
import tools.aqua.bgw.components.uicomponents.TextField
import tools.aqua.bgw.core.MenuScene
import tools.aqua.bgw.util.Font
import tools.aqua.bgw.visual.ColorVisual
import tools.aqua.bgw.visual.ImageVisual
import java.awt.Color

/**
 * [MenuScene] that is used for starting a new game. It is displayed directly at program start or reached
 * when "new game" is clicked in [GameFinishedMenuScene]. After providing the names of both players,
 * [startButton] can be pressed. There is also a [quitButton] to end the program.
 */
class NewGameMenuScene(private val rootService: RootService) : MenuScene(1920,1080),Refreshable {
    private var numberOfPlayer = 2
    private val buttonColor = ColorVisual(123, 31, 162)
    private val labelFont = Font(size = 30, color = Color.WHITE)
    private val headlineLabel = Label(
        width = 400, height = 50,
        posX = 795, posY = 370,
        text = "Schwimmen Game",
        font = Font(size = 45, color = Color.WHITE),
    )
    private val introLine = Label(
        width = 400, height = 50,
        posX = 795, posY = 400,
        text = "a wonderful card game",
        font = labelFont,
    )

    private val p1Label = Label(
        width = 300, height = 50,
        posX = 650, posY = 450,
        text = "Player 1:",
        font = labelFont,
    )
    private val p1Input: TextField = TextField(
        width = 300, height = 50,
        posX = 895, posY = 450,
        font = Font(size = 30)
    )
    private val p2Label = Label(
        width = 300, height = 50,
        posX = 650, posY = 525,
        text = "Player 2:",
        font = labelFont,
    )
    private val p2Input: TextField = TextField(
        width = 300, height = 50,
        posX = 895, posY = 525,
        font = Font(size = 30)
    )
    private val p3Label = Label(
        width = 300, height = 50,
        posX = 650, posY = 600,
        text = "Player 3:",
        font = labelFont
    )
    private val p3Input: TextField = TextField(
        width = 300, height = 50,
        posX = 895, posY = 600,
        font = Font(size = 30)
    )
    private val p3Button = Button(
        width = 100, height = 50,
        posX = 1235, posY = p3Input.posY,
        text = "-",
        font = Font(size = 30),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            --numberOfPlayer
            if (p4Input.isVisible){
                p3Input.text = p4Input.text
                changeP4Visible(false)
            } else changeP3Visible(false)
            newPlayerButton.posY -= 75
            newPlayerButton.isVisible = true
        }
    }
    private val p4Label = Label(
        width = 300, height = 50,
        posX = 650, posY = 675,
        text = "Player 4:",
        font = labelFont
    )
    private val p4Input: TextField = TextField(
        width = 300, height = 50,
        posX = 895, posY = 675,
        font = Font(size = 30)
    )
    private val p4Button = Button(
        width = 100, height = 50,
        posX = 1235, posY = p4Input.posY,
        text = "-",
        font = Font(size = 30),
        visual = buttonColor
    ).apply {
        onMouseClicked = {
            --numberOfPlayer
            changeP4Visible(false)
            newPlayerButton.posY -= 75
            newPlayerButton.isVisible = true
        }
    }
    private val newPlayerButton = Button(
        width = 300, height = 50,
        posX = 895, posY = 600,
        text = "New Player",
        font = Font(size = 30),
        visual = buttonColor
    ).apply {
        onMouseClicked ={
            ++numberOfPlayer
            this.posY += 75
            if (numberOfPlayer == 3){
                changeP3Visible(true)
            }
            else{
                changeP4Visible(true)
                this.isVisible = false
            }
        }
    }
    val quitButton = Button(
        width = 300, height = 50,
        posX = 300, posY = 900,
        text = "Exit",
        visual = buttonColor,
        font = Font(size = 30)
    )
    private val startButton = Button(
        width = 300, height = 50,
        posX = 1500, posY = 900,
        text = "Start",
        visual = buttonColor,
        font = Font(size = 30)
    ).apply {
        onMouseClicked ={
                val playerList = mutableListOf<String>()
                if (p1Input.text.trim().isNotEmpty())
                    playerList.add(p1Input.text.trim())
                 if (p2Input.text.trim().isNotEmpty())
                    playerList.add(p2Input.text.trim())
                if (p3Input.text.trim().isNotEmpty())
                    playerList.add(p3Input.text.trim())
                if (p4Input.text.trim().isNotEmpty())
                    playerList.add(p4Input.text.trim())
                rootService.gameService.startNewGame(
                    playerList
                )
                println(playerList)
        }
    }
    private fun changeP3Visible(state:Boolean){
        p3Label.isVisible = state
        p3Input.isVisible = state
        p3Button.isVisible = state
        if (!state) p3Input.text = ""
    }
    private fun changeP4Visible(state: Boolean){
        p4Label.isVisible = state
        p4Input.isVisible = state
        p4Button.isVisible = state
        if (!state) p4Input.text = ""
    }

    /**
     * reset everything
     */
    override fun refreshAfterGameEnd() {
        p1Input.text = ""
        p2Input.text = ""
        changeP3Visible(false)
        changeP4Visible(false)
        newPlayerButton.posY = 600.0
        newPlayerButton.isVisible = true
        numberOfPlayer = 2
    }
    init {
        opacity = .5
        background = ImageVisual("background.png")
        changeP3Visible(false)
        changeP4Visible(false)
        addComponents(
            headlineLabel,
            introLine,
            p1Label, p1Input,
            p2Label, p2Input,
            p3Label, p3Input, p3Button,
            p4Label, p4Input, p4Button,
            newPlayerButton,
            startButton, quitButton
        )
    }
}