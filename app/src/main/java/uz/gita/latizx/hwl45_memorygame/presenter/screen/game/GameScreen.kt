package uz.gita.latizx.hwl45_memorygame.presenter.screen.game

import android.animation.ObjectAnimator
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.withCreationCallback
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uz.gita.latizx.hwl45_memorygame.R
import uz.gita.latizx.hwl45_memorygame.core.util.closeImage
import uz.gita.latizx.hwl45_memorygame.core.util.hideAnim
import uz.gita.latizx.hwl45_memorygame.core.util.hindAnim
import uz.gita.latizx.hwl45_memorygame.core.util.openImage
import uz.gita.latizx.hwl45_memorygame.core.util.setColor
import uz.gita.latizx.hwl45_memorygame.core.util.startTimer
import uz.gita.latizx.hwl45_memorygame.data.LevelEnum
import uz.gita.latizx.hwl45_memorygame.data.model.CardModel
import uz.gita.latizx.hwl45_memorygame.databinding.ScreenGameBinding
import uz.gita.latizx.hwl45_memorygame.presenter.screen.game.dialog.GameOverDialog
import uz.gita.latizx.hwl45_memorygame.presenter.screen.game.dialog.WinDialog

@AndroidEntryPoint
class GameScreen : Fragment(R.layout.screen_game) {
    private val args: GameScreenArgs by navArgs()
    private val binding by viewBinding(ScreenGameBinding::bind)
    private val viewModel: GameContract.GameViewModel by viewModels<GameViewModelImpl>(
        extrasProducer = {
            defaultViewModelCreationExtras.withCreationCallback<GameViewModelImpl.Factory> { factory -> factory.create(args.level) }
        }
    )
    private val views = ArrayList<ImageView>()
    private val countdown = listOf("1", "2", "3")
    private lateinit var level: LevelEnum
    private var job: Job? = null
    private var cardWidth = 0f
    private var cardHeight = 0f
    private var centerX = 0f
    private var centerY = 0f
    private var canClick = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        level = args.level
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.btnBack.setOnClickListener { viewModel.clickHome() }
        requireActivity().onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                viewModel.clickHome()
            }
        })

        binding.apply {
            rlContainer.post {
                cardWidth = binding.rlContainer.width.toFloat() / level.rowCount
                cardHeight = (binding.rlContainer.height.toFloat() / level.columnCount)

                centerX = binding.rlContainer.width / 2f
                centerY = binding.rlContainer.height / 2f
            }
        }
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.images.onEach {
                    if (it.isNotEmpty()) {
                        loadViews(it)
                        delay(5000)
                        clickEvent()
                    }
                }.launchIn(lifecycleScope)
                delay(4400)
                job = startTimer(
                    durationSeconds = 180,
                    delayMillis = 1000L,
                    onTick = { second ->
                        val minutes = second / 60
                        val sec = second % 60
                        binding.tvTimer.text = String.format(null, "%02d:%02d", minutes, sec)
                    },
                    onFinish = {
                        viewModel.timeOut()
                    })
            }
        }
        observers()
        setBackground()
        startCountdownAnimation()
    }

    private fun loadViews(images: List<CardModel>) {
        binding.rlContainer.removeAllViews()
        val margin = 20
        val availableCardWidth = (cardWidth - margin)

        for (i in 0 until level.rowCount) {
            for (j in 0 until level.columnCount) {
                val image = ImageView(requireContext())
                val lp = RelativeLayout.LayoutParams(availableCardWidth.toInt(), (availableCardWidth.toInt() * 1.3).toInt())

                image.layoutParams = lp
                image.setBackgroundResource(R.drawable.img_bg_card_q)
                image.x = centerX - cardWidth / 2
                image.y = centerY - cardHeight / 2

                image.isEnabled = false
                image.animate().setDuration(1000)
                    .x((i * cardWidth) + (margin / 2))
                    .y((j * cardHeight) + (margin / 2) + 24)
                    .withEndAction {
                        image.openImage {
                            lifecycleScope.launch {
                                delay(1500L)
                                image.closeImage()
                                image.isEnabled = true
                            }
                        }
                    }
                binding.rlContainer.addView(image)

                image.tag = images[i * level.columnCount + j]
                views.add(image)
            }
        }
    }

    private fun clickEvent() {
        views.forEachIndexed { index, imageView ->
            imageView.setOnClickListener {
                if (!canClick) return@setOnClickListener
                viewModel.checkMatchingCards(imageView.tag as CardModel, index)
            }
        }
    }

    private fun observers() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch { viewModel.open.collect { views[it].openImage() } }
                launch { viewModel.openAction.collect { views[it].openImage { canClick = false } } }
                launch { viewModel.close.collect { views[it].closeImage() } }
                launch { viewModel.showWinDialog.collect { showWinDialog(it.first, it.second) } }
                launch { viewModel.showExitDialog.collect { showExitDialog() } }
                launch {
                    viewModel.showGameOverDialog.collect {
                        showGameOverDialog(it.first, it.second)
                    }
                }
                launch {
                    viewModel.closeWithAction.collect {
                        views[it].closeImage {
                            canClick = true
                        }
                    }
                }
                launch {
                    viewModel.hide.collect {
                        views[it].hideAnim()
                        views[it].apply {
                            isClickable = false
                            isFocusableInTouchMode = false
                            isFocusable = false
                        }
                    }
                }
                launch {
                    viewModel.hideWithAction.collect { index ->
                        views[index].hindAnim {
                            canClick = true
                            viewModel.findCards()

                            val handler = Handler(Looper.getMainLooper())
                            handler.postDelayed({
                                views[index].apply {
                                    isClickable = false
                                    isFocusable = false
                                    isFocusableInTouchMode = false
                                }
                            }, 50)
                        }
                    }
                }
            }
        }
    }

    private fun showWinDialog(attempts: Int, totalPoint: Int) {
        if (job != null && job!!.isActive) {
            job?.cancel()
        }
        val dialog = WinDialog()
        dialog.setDate(attempts, totalPoint)
        dialog.setOnClickPlayListener {
            viewModel.clickExit()
        }
        dialog.show(parentFragmentManager, "WinDialog")
    }

    private fun showExitDialog() {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_exit, null)
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
        view.findViewById<Button>(R.id.btn_yes_exit).setOnClickListener {
            viewModel.clickExit()
            dialog.dismiss()
        }
        view.findViewById<Button>(R.id.btn_no_exit).setOnClickListener { dialog.dismiss() }
        dialog.show()
    }

    private fun showGameOverDialog(attempts: Int, totalPoint: Int) {
        if (job != null && job!!.isActive) {
            job?.cancel()
        }
        val dialog = GameOverDialog()
        dialog.setDate(attempts, totalPoint)
        dialog.setOnClickPlayListener {
            viewModel.clickExit()
        }
        dialog.show(parentFragmentManager, "GameOverDialog")
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

    private fun startCountdownAnimation() {
        lifecycleScope.launch {
            for (i in countdown.indices) {
                val animator = ObjectAnimator.ofFloat(binding.tvTotal, "alpha", 1f, 0f).apply {
                    duration = 500
                }
                animator.start()
                delay(800)
                binding.tvTotal.text = countdown[i]
                val fadeInAnimator = ObjectAnimator.ofFloat(binding.tvTotal, "alpha", 0f, 1f).apply {
                    duration = 500
                }
                fadeInAnimator.start()
                delay(800)
            }

            binding.tvTotal.text = "START!"
            val startAnimator = ObjectAnimator.ofFloat(binding.tvTotal, "alpha", 0f, 1f).apply {
                duration = 1000
            }
            delay(400)
            binding.llStart.visibility = View.GONE
            startAnimator.start()
        }
    }
}