package uz.gita.latizx.hwl45_memorygame.domain.usecase.impl

import kotlinx.coroutines.flow.Flow
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel
import uz.gita.latizx.hwl45_memorygame.domain.repository.AppRepository
import uz.gita.latizx.hwl45_memorygame.domain.usecase.GetAllCardsUseCase
import javax.inject.Inject

class GetAllCardsUseCaseImpl @Inject constructor(
    private val repository: AppRepository
) : GetAllCardsUseCase {
    override fun invoke(levelEnum: LevelEnum): Flow<List<CardModel>> = repository.getCards(levelEnum)
}