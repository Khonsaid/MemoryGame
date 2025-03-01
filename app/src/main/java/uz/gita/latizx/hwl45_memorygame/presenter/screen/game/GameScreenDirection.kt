package uz.gita.latizx.hwl45_memorygame.presenter.screen.game

import uz.gita.latizx.hwl45_memorygame.presenter.navigator.AppNavigator
import javax.inject.Inject

class GameScreenDirection @Inject constructor(
    private val appNavigator: AppNavigator
) : GameContract.HomeDirection {
    override suspend fun backToPrevScreen() {
        appNavigator.back()
    }
}