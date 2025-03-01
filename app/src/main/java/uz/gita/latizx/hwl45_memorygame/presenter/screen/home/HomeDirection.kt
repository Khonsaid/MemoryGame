package uz.gita.latizx.hwl45_memorygame.presenter.screen.home

import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.presenter.navigator.AppNavigator
import javax.inject.Inject

class HomeDirection @Inject constructor(private val appNavigator: AppNavigator) : HomeContract.Direction {
    override suspend fun moveToGameScreen(levelEnum: LevelEnum) {
        appNavigator.navigateTo(HomeScreenDirections.actionHomeScreenToGameScreen(levelEnum))
    }
}