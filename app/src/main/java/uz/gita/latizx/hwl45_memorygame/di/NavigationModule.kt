package uz.gita.latizx.hwl45_memorygame.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.latizx.hwl45_memorygame.presenter.navigator.AppNavigationDispatcher
import uz.gita.latizx.hwl45_memorygame.presenter.navigator.AppNavigationHandler
import uz.gita.latizx.hwl45_memorygame.presenter.navigator.AppNavigator
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NavigationModule {

    @Binds
    fun bindAppNavigation(dispatcher: AppNavigationDispatcher): AppNavigator

    @Binds
    fun bindAppNavigationHolder(dispatcher: AppNavigationDispatcher): AppNavigationHandler

}