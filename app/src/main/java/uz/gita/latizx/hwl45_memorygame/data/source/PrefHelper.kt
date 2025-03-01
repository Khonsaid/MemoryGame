package uz.gita.latizx.hwl45_memorygame.data.source

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PrefHelper @Inject constructor(@ApplicationContext context: Context) {
    private val pref: SharedPreferences = context.getSharedPreferences("game_settings", Context.MODE_PRIVATE)

    var soundEnable: Boolean
        get() = pref.getBoolean("soundEnable", true)
        set(value) = pref.edit().putBoolean("soundEnable", value).apply()

    var soundGameBtnEnable: Boolean
        get() = pref.getBoolean("soundGameBtnEnable", true)
        set(value) = pref.edit().putBoolean("soundGameBtnEnable", value).apply()
}