package view

import tools.aqua.bgw.core.BoardGameApplication

/**
 * it is just sopraApplication
 */
class SopraApplication : BoardGameApplication("SoPra Game") {

   private val helloScene = HelloScene()

   init {
        this.showGameScene(helloScene)
    }

}

