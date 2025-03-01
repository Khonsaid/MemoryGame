package uz.gita.latizx.hwl45_memorygame.presenter.screen.game.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.databinding.DialogGameOverBinding

class GameOverDialog : DialogFragment(R.layout.dialog_game_over) {
    val binding by viewBinding(DialogGameOverBinding::bind)
    var onClickPlayBtnListener: (() -> Unit)? = null
    private var totalPoint = ""
    private var attempts = ""
    fun setOnClickPlayListener(block: () -> Unit) {
        onClickPlayBtnListener = block
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        dialog?.window?.apply {
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.DialogSlideAnimation
        }
        dialog?.setCancelable(false)

        binding.btnPlay.setOnClickListener {
            onClickPlayBtnListener?.invoke()
            dismiss()
        }

        binding.apply {
            tvTotal.text = totalPoint
            tvStages.text = attempts
        }
//        startWinAnimation()
    }

    private fun startWinAnimation() {
        binding.apply {
            title.animate().apply {
                alpha(1f)
                translationYBy(-50f)
                duration = 500
            }.start()
            desc.animate().apply {
                alpha(1f)
                translationXBy(-100f)
                duration = 700
                startDelay = 300
            }.start()
            btnPlay.animate().apply {
                alpha(1f)
                scaleX(1.2f)
                scaleY(1.2f)
                duration = 800
                startDelay = 500
            }.start()
        }
    }

    fun setDate(attempts: Int, totalPoint: Int) {
        this.attempts = attempts.toString()
        this.totalPoint = totalPoint.toString()
    }
}