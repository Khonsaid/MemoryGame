package uz.gita.latizx.hwl45_memorygame.presenter.screen.game.dialog

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.core.util.GameSoundSetting
import uz.gita.latizx.hwl45_memorygame.databinding.DialogInfoBinding
import javax.inject.Inject

class InfoDialog : DialogFragment(R.layout.dialog_info) {
    val binding by viewBinding(DialogInfoBinding::bind)
    var onClickPlayBtnListener: (() -> Unit)? = null
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
    }
}