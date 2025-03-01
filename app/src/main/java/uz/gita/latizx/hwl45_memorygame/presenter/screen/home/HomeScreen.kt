package uz.gita.latizx.hwl45_memorygame.presenter.screen.home

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.core.util.setColor
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.databinding.ScreenHomeBinding
import uz.gita.latizx.hwl45_memorygame.presenter.screen.game.dialog.InfoDialog

@AndroidEntryPoint
class HomeScreen : Fragment(R.layout.screen_home) {
    private val binding by viewBinding(ScreenHomeBinding::bind)
    private val viewModel: HomeContract.HomeViewModel by viewModels<HomeViewModelImpl>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewModel.onFragmentCreated()
        binding.apply {
            btnHard.setOnClickListener { viewModel.clickPlay(LevelEnum.HARD) }
            btnMedium.setOnClickListener { viewModel.clickPlay(LevelEnum.MEDIUM) }
            btnEasy.setOnClickListener { viewModel.clickPlay(LevelEnum.EASY) }
            btnMusic.setOnClickListener { viewModel.clickSound() }
            btnInfo.setOnClickListener { viewModel.clickInfo() }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.changeImage.collect { animateAndSetImage(binding.btnMusic, it) }
                }
                launch { viewModel.openInfoDialog.collect { openInfoDialog() } }
            }
        }
        setBackground()
        viewModel.stateMusicBtn.observe(viewLifecycleOwner) { binding.btnMusic.setImageResource(it) }
    }

    private fun openInfoDialog() {
        val dialog = InfoDialog()
        dialog.setOnClickPlayListener {
            viewModel.sound()
            dialog.dismiss()
        }
        dialog.show(parentFragmentManager, "InfoDialog")
    }

    private fun animateAndSetImage(imageButton: ImageButton, newImageRes: Int) {
        // Animatsiya: o'lchamni 0gacha kichraytirish (scale)
        val scaleDown = ObjectAnimator.ofFloat(imageButton, View.SCALE_X, 1f, 0f).apply {
            duration = 300 // 300 ms davom etadi
        }

        // Animatsiya tugashi bilan yangi rasmni o‘rnatish
        scaleDown.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                imageButton.setImageResource(newImageRes)

                // Animatsiya: o'lchamni 0dan 1gacha kattalashtirish (scale)
                val scaleUp = ObjectAnimator.ofFloat(imageButton, View.SCALE_X, 0f, 1f).apply {
                    duration = 300
                }
                scaleUp.start()
            }
        })

        scaleDown.start()
    }

    private fun setBackground() {
        val gradientDrawable = GradientDrawable(
            GradientDrawable.Orientation.TL_BR,
            intArrayOf(
                ContextCompat.getColor(requireContext(), R.color.gradient1_game),
                ContextCompat.getColor(requireContext(), R.color.gradient2_game),
                ContextCompat.getColor(requireContext(), R.color.gradient3_game),
                ContextCompat.getColor(requireContext(), R.color.gradient1_game)
            )
        )
        binding.root.background = gradientDrawable
        requireActivity().window.setColor(R.color.gradient1_game)
    }
}