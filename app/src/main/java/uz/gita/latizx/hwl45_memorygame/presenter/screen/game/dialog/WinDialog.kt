package uz.gita.latizx.hwl45_memorygame.presenter.screen.game.dialog

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.databinding.DialogWinBinding
import java.util.concurrent.TimeUnit

class WinDialog : DialogFragment(R.layout.dialog_win) {
    val binding by viewBinding(DialogWinBinding::bind)
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

        binding.btnPlayAgain.setOnClickListener {
            onClickPlayBtnListener?.invoke()
            dismiss()
        }
        binding.apply {
            tvTotal.text = totalPoint
            tvStages.text = attempts
        }
        startWinAnimation()
    }

    private fun startWinAnimation() {
        binding.tvWinMessage.text = "YOU WIN"
        val fadeInAnimator = ObjectAnimator.ofFloat(binding.tvWinMessage, "alpha", 0f, 1f).apply {
            duration = 1500 // 1.5 soniya
        }
        fadeInAnimator.start()

        val party = Party(
            speed = 0f,
            maxSpeed = 30f,
            damping = 0.9f,
            spread = 360,
            colors = listOf(0xfce18a, 0xff726d, 0xf4306d, 0xb48def),
            emitter = Emitter(duration = 4000, TimeUnit.MILLISECONDS).max(100),
            position = Position.Relative(0.5, 0.3)
        )

        // Konfetti efekti
        binding.konfettiView.start(party)
        lifecycleScope.launch { delay(1000) }

        val slideInAnimation = AnimationUtils.loadAnimation(context, R.anim.slide_in_bottom)
        binding.btnPlayAgain.startAnimation(slideInAnimation)
    }

    fun setDate(attempts: Int, totalPoint: Int) {
        this.attempts = attempts.toString()
        this.totalPoint = totalPoint.toString()
    }
}