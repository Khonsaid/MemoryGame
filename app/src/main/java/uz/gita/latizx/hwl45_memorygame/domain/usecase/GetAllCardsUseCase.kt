package uz.gita.latizx.hwl45_memorygame.domain.usecase

import kotlinx.coroutines.flow.Flow
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel

interface GetAllCardsUseCase {
    operator fun invoke(levelEnum: LevelEnum): Flow<List<CardModel>>
}