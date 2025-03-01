package uz.gita.latizx.hwl45_memorygame.core.util

import android.view.Window
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun Window.setColor(color: Int) {
    val windowInsetsController = WindowInsetsControllerCompat(this, this.decorView)
    windowInsetsController.isAppearanceLightStatusBars = false
    windowInsetsController.isAppearanceLightNavigationBars = false
    this.statusBarColor = ContextCompat.getColor(this.context, color)
    this.navigationBarColor = ContextCompat.getColor(this.context, color)
}

fun CoroutineScope.startTimer(
    durationSeconds: Int,
    delayMillis: Long = 1000L,
    onTick: (Int) -> Unit,
    onFinish: (() -> Unit)? = null
): Job {
    return launch {
        var secondsLeft = durationSeconds
        while (isActive && secondsLeft >= 0) {
            onTick(secondsLeft)
            delay(delayMillis)
            secondsLeft--
        }
        if (secondsLeft < 0) {
            onFinish?.invoke()
        }
    }
}