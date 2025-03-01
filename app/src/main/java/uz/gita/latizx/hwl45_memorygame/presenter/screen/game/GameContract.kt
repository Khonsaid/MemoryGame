package uz.gita.latizx.hwl45_memorygame.presenter.screen.game

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel

interface GameContract {

    interface GameViewModel {
        val images: StateFlow<List<CardModel>>
        val hide: SharedFlow<Int>
        val close: SharedFlow<Int>
        val canClick: SharedFlow<Boolean>
        val showGameOverDialog: SharedFlow<Pair<Int, Int>>
        val showWinDialog: SharedFlow<Pair<Int, Int>>
        val showExitDialog: SharedFlow<Unit>
        val attemptsFlow: SharedFlow<Int>

        val open: SharedFlow<Int>
        val closeWithAction: SharedFlow<Int>
        val openAction: SharedFlow<Int>
        val hideWithAction: SharedFlow<Int>

        fun checkMatchingCards(clickedCard: CardModel, clickedIndex: Int)
        fun resGame()
        fun timeOut()
        fun findCards()
        fun clickHome()
        fun clickExit()
    }

    interface HomeDirection {
        suspend fun backToPrevScreen()
    }
}