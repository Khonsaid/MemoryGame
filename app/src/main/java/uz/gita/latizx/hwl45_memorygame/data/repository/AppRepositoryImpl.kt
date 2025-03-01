package uz.gita.latizx.hwl45_memorygame.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel
import uz.gita.latizx.hwl45_memorygame.domain.repository.AppRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepositoryImpl @Inject constructor() : AppRepository {

    override fun getCards(level: LevelEnum): Flow<List<CardModel>> = flow {
        val count = (level.rowCount * level.columnCount) / 2

        val cards = loadData().subList(0, count).mapIndexed { index, image ->
            CardModel(index, image)
        }

        emit((cards + cards).shuffled())
    }.flowOn(Dispatchers.IO)

    private fun loadData(): List<Int> = listOf(
        R.drawable.img_koala,
        R.drawable.img1_cow,
        R.drawable.img2_butterfly,
        R.drawable.img3_crab,
        R.drawable.img4_fox,
        R.drawable.img5_hen,
        R.drawable.img6_horse,
        R.drawable.img7_lion,
        R.drawable.img8_parrot,
        R.drawable.img9_whale,
        R.drawable.img10_zebra,
        R.drawable.img11_turtle,
        R.drawable.img12_snail,
        R.drawable.img13_raccoon,
        R.drawable.img14_rabbit,
        R.drawable.img15_pigeon,
        R.drawable.img16_peacock,
        R.drawable.img17_owl,
        R.drawable.img18_orangutan,
        R.drawable.imh19_octopus,
        R.drawable.img20_ladybug,
        R.drawable.img21_hummingbird,
        R.drawable.img22_hamster_5389261,
        R.drawable.img22_hamster_5389261,
        R.drawable.img24_dog,
        R.drawable.img25_clown_fish,
        R.drawable.img26_cat,
    )
}
