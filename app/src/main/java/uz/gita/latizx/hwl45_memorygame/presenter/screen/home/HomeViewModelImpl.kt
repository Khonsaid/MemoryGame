package uz.gita.latizx.hwl45_memorygame.presenter.screen.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.core.util.GameSoundSetting
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.data.source.PrefHelper
import javax.inject.Inject

@HiltViewModel
class HomeViewModelImpl @Inject constructor(
    private val direction: HomeDirection,
    private val sound: GameSoundSetting,
    private val pref: PrefHelper,
) : HomeContract.HomeViewModel, ViewModel() {
    override val openInfoDialog = MutableSharedFlow<Unit>()
    override val changeImage = MutableSharedFlow<Int>()
    override val stateMusicBtn = MutableLiveData<Int>()


    override fun onFragmentCreated() {
        stateMusicBtn.value = if (pref.soundGameBtnEnable) R.drawable.ic_music_note else R.drawable.ic_music_note_slash
    }

    override fun clickPlay(levelEnum: LevelEnum) {
        viewModelScope.launch {
            sound.playSound(sound.clickSound)
            direction.moveToGameScreen(levelEnum)
        }
    }

    override fun clickInfo() {
        sound.playSound(sound.clickSound)
        viewModelScope.launch { openInfoDialog.emit(Unit) }
    }

    override fun clickSound() {
        sound.playSound(sound.clickSound)
        val soundEnable = pref.soundGameBtnEnable
        pref.soundGameBtnEnable = !soundEnable
        stateMusicBtn()
    }

    override fun sound() {
        sound.playSound(sound.clickSound)
    }

    private fun stateMusicBtn() {
        viewModelScope.launch {
            changeImage.emit(if (pref.soundGameBtnEnable) R.drawable.ic_music_note else R.drawable.ic_music_note_slash)
        }
    }
}