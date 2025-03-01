package uz.gita.latizx.hwl45_memorygame.presenter.screen.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.latizx.hwl45_memorygame.core.util.GameSoundSetting
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel
import uz.gita.latizx.hwl45_memorygame.domain.usecase.GetAllCardsUseCase

@HiltViewModel(assistedFactory = GameViewModelImpl.Factory::class)
class GameViewModelImpl @AssistedInject constructor(
    private val getAllCards: GetAllCardsUseCase,
    private val direction: GameScreenDirection,
    private val sound: GameSoundSetting,
    @Assisted private val level: LevelEnum
) : GameContract.GameViewModel, ViewModel() {
    @AssistedFactory
    interface Factory {
        fun create(level: LevelEnum): GameViewModelImpl
    }

    override val images = MutableStateFlow<List<CardModel>>(emptyList())
    override val hide = MutableSharedFlow<Int>()
    override val close = MutableSharedFlow<Int>()
    override val canClick = MutableSharedFlow<Boolean>()
    override val showGameOverDialog = MutableSharedFlow<Pair<Int, Int>>()
    override val showWinDialog = MutableSharedFlow<Pair<Int, Int>>()
    override val showExitDialog = MutableSharedFlow<Unit>()
    override val attemptsFlow = MutableSharedFlow<Int>()

    override val open = MutableSharedFlow<Int>()
    override val closeWithAction = MutableSharedFlow<Int>()
    override val openAction = MutableSharedFlow<Int>()
    override var hideWithAction = MutableSharedFlow<Int>()
    private var findCards = 0
    private var firstIndex = -1
    private var secondIndex = -1
    private var attempts = 0
//    private var totalPoint = 0

    private val cardClicks = ArrayList<CardModel>()

    init {
        loadData()
    }

    override fun checkMatchingCards(clickedCard: CardModel, clickedIndex: Int) {
        viewModelScope.launch {
            if (firstIndex != -1 && secondIndex != -1) return@launch
            if (firstIndex == clickedIndex || secondIndex == clickedIndex) return@launch

            if (firstIndex == -1) {
                cardClicks.add(clickedCard)
                firstIndex = clickedIndex
                open.emit(clickedIndex)
                sound.playSound(sound.openSound)
            } else {
                cardClicks.add(clickedCard)
                secondIndex = clickedIndex
                openAction.emit(clickedIndex)
                sound.playSound(sound.openSound)

                delay(1000)
                if (cardClicks[0].id == cardClicks[1].id) {
                    hide.emit(firstIndex)
                    hideWithAction.emit(secondIndex)
                    attemptsFlow.emit(++attempts)
                    sound.playSound(sound.findSound)
                } else {
                    close.emit(firstIndex)
                    closeWithAction.emit(secondIndex)
                    attemptsFlow.emit(++attempts)
//                    ++totalPoint
                    sound.playSound(sound.closeSound)
                }
                firstIndex = -1
                secondIndex = -1

                cardClicks.clear()
            }
        }
    }

    override fun findCards() {
        ++findCards
        if (isFinish()) {
            sound.playSound(sound.winSound)
            viewModelScope.launch { showWinDialog.emit(Pair(findCards, attempts)) }
        }
    }

    override fun clickHome() {
        viewModelScope.launch { showExitDialog.emit(Unit) }
        sound.playSound(sound.clickSound)
    }

    override fun clickExit() {
        viewModelScope.launch { direction.backToPrevScreen() }
        sound.playSound(sound.clickSound)
    }

    private fun isFinish(): Boolean {
        return findCards == (level.rowCount * level.columnCount) / 2
    }

    override fun resGame() {
        cardClicks.clear()
        firstIndex = -1
        secondIndex = -1
        findCards = 0
//        totalPoint = 0
        loadData()
    }

    override fun timeOut() {
        viewModelScope.launch { showGameOverDialog.emit(Pair(findCards, attempts)) }
        sound.playSound(sound.loseSound)
    }

    private fun loadData() {
        getAllCards(level).onEach {
            images.emit(it)
        }.launchIn(viewModelScope)
    }
}