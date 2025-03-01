package uz.gita.latizx.hwl45_memorygame.core.util

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.SoundPool
import dagger.hilt.android.qualifiers.ApplicationContext
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.data.source.PrefHelper
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameSoundSetting @Inject constructor(
    @ApplicationContext context: Context,
    private val pref: PrefHelper
) {
    private var soundPool: SoundPool
    private var mediaPlayer: MediaPlayer

    //    private var btnSound: Int
    var winSound: Int
        private set
    var openSound: Int
        private set
    var closeSound: Int
        private set
    var findSound: Int
        private set
    var loseSound: Int
        private set
    var timeSound: Int
        private set
    var clickSound: Int
        private set

    init {
        val audioAttributes =
            AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build()
        soundPool = SoundPool.Builder().setMaxStreams(1).setAudioAttributes(audioAttributes).build()
        mediaPlayer = MediaPlayer.create(context, R.raw.fast_feel_banana)

        openSound = soundPool.load(context, R.raw.open, 1)
        closeSound = soundPool.load(context, R.raw.close, 1)
        findSound = soundPool.load(context, R.raw.find, 1)
        winSound = soundPool.load(context, R.raw.win, 1)
        loseSound = soundPool.load(context, R.raw.game_over, 1)
        timeSound = soundPool.load(context, R.raw.time, 1)
        clickSound = soundPool.load(context, R.raw.click, 1)
    }

    fun playSound(soundId: Int) {
        if (pref.soundGameBtnEnable) soundPool.play(soundId, 1f, 1f, 0, 0, 1f)
    }
}