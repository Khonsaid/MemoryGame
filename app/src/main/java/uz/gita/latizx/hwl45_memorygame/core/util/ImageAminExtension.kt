package uz.gita.latizx.hwl45_memorygame.core.util

import android.widget.ImageView
import androidx.core.view.marginBottom
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel

fun ImageView.openImage(endAnim: () -> Unit) {
    animate()
        .setDuration(350)
        .rotationY(89f)
        .withEndAction {
            setImageResource((this.tag as CardModel).img)
            setBackgroundResource(R.drawable.bg_card)
            scaleType = ImageView.ScaleType.FIT_CENTER
            rotationY = -89f
            setPadding(20, 20, 20, 20)

            animate()
                .setDuration(350)
                .rotationY(0f)
                .withEndAction {
                    endAnim.invoke()
                }
                .start()
        }
        .start()
}

fun ImageView.openImage() {
    animate()
        .setDuration(350)
        .rotationY(89f)
        .withEndAction {
            setBackgroundResource(R.drawable.bg_card)
            setImageResource((this.tag as CardModel).img)
            scaleType = ImageView.ScaleType.FIT_CENTER
            rotationY = -89f
            setPadding(20, 30, 20, 30)

            animate()
                .setDuration(350)
                .rotationY(0f)
                .withEndAction {
                }.start()
        }.start()
}

fun ImageView.closeImage() {
    this.apply {
        animate()
            .setDuration(350)
            .rotationY(89f)
            .withEndAction {
                setImageResource(R.drawable.ic_bg_card)
                rotationY = -89f
                scaleType = ImageView.ScaleType.FIT_XY
                setPadding(10, 10, 10, 10)

                animate()
                    .setDuration(350)
                    .rotationY(0f)
                    .withEndAction { }
                    .start()
            }.start()
    }
}

fun ImageView.closeImage(endAnim: () -> Unit) {
    animate()
        .setDuration(350)
        .rotationY(89f)
        .withEndAction {
            setImageResource(R.drawable.ic_bg_card)
            rotationY = -89f
            scaleType = ImageView.ScaleType.FIT_XY
            setPadding(10, 10, 10, 10)

            animate()
                .setDuration(350)
                .rotationY(0f)
                .withEndAction { endAnim.invoke() }
                .start()
        }.start()
}

fun ImageView.hideAnim() {
    this.animate()
        .setDuration(500)
        .alpha(0.2f)
        .start()
}

fun ImageView.hindAnim(endAnim: () -> Unit) {
    this.animate()
        .setDuration(500)
        .alpha(0.2f)
        .withEndAction(endAnim)
        .start()
}