package uz.gita.latizx.hwl45_memorygame.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.gita.latizx.hwl45_memorygame.data.repository.AppRepositoryImpl
import uz.gita.latizx.hwl45_memorygame.domain.repository.AppRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @[Binds Singleton]
    fun bindRepository(impl: AppRepositoryImpl): AppRepository
}