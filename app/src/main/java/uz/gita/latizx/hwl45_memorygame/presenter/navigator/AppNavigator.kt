package uz.gita.latizx.hwl45_memorygame.presenter.navigator

import androidx.navigation.NavDirections

interface AppNavigator {
    suspend fun navigateTo(id: NavDirections)
    suspend fun back()
}