package uz.gita.latizx.hwl45_memorygame.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uz.gita.latizx.hwl45_memorygame.domain.usecase.GetAllCardsUseCase
import uz.gita.latizx.hwl45_memorygame.domain.usecase.impl.GetAllCardsUseCaseImpl

@Module
@InstallIn(ViewModelComponent::class)
interface UseCaseModule {

    @Binds
    fun bindGetAllUseCase(impl: GetAllCardsUseCaseImpl): GetAllCardsUseCase
}