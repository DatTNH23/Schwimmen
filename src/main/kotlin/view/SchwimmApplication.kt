package view

import service.RootService
import tools.aqua.bgw.animation.*
import tools.aqua.bgw.core.BoardGameApplication

/**
 * Schwimm application runs
 */
class SchwimmApplication : BoardGameApplication("Schwimm"), Refreshable {
    private val rootService  = RootService()

    // Scenes

    // This is where the actual game takes place
    private val gameScene = GameScene(rootService)

    // This menu scene is shown after each finished game
    private val gameFinishedMenuScene = GameFinishedMenuScene(rootService).apply {
        newGameButton.onMouseClicked = {
            this@SchwimmApplication.showMenuScene(newGameMenuScene)
        }
        quitButton.onMouseClicked = {
            exit()
        }
    }

    // This menu scene is shown after application start and if the "new game" button
    // is clicked in the gameFinishedMenuScene
    private val newGameMenuScene = NewGameMenuScene(rootService).apply {
        quitButton.onMouseClicked = {
            exit()
        }
    }
    init{
        // all scenes and the application itself need to
        // react to change done in the service layer
        rootService.addRefreshables(
            this,
            gameScene,
            gameFinishedMenuScene,
            newGameMenuScene
        )
        this.showMenuScene(newGameMenuScene)
        //this.showMenuScene(gameFinishedMenuScene)
    }
    override fun refreshAfterStartNewGame() {
        this.hideMenuScene()
        this.showGameScene(gameScene)
    }
    override fun refreshAfterGameEnd() {
        this.showMenuScene(gameFinishedMenuScene)
    }
}