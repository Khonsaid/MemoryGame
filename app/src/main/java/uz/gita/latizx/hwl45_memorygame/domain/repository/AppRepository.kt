package uz.gita.latizx.hwl45_memorygame.domain.repository

import kotlinx.coroutines.flow.Flow
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum

interface AppRepository {
    fun getCards(level: LevelEnum): Flow<List<CardModel>>
}