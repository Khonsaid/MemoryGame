package uz.gita.latizx.hwl45_memorygame.presenter.screen.home

import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.SharedFlow
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum

interface HomeContract {

    interface HomeViewModel {
        val openInfoDialog: SharedFlow<Unit>
        val changeImage: SharedFlow<Int>
        val stateMusicBtn: LiveData<Int>
        fun onFragmentCreated()
        fun clickPlay(levelEnum: LevelEnum)
        fun clickInfo()
        fun clickSound()
        fun sound()
    }

    interface Direction {
        suspend fun moveToGameScreen(levelEnum: LevelEnum)
    }
}