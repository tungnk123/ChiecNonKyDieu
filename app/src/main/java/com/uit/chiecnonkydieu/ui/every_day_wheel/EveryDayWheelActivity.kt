package com.uit.chiecnonkydieu.ui.wheel

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityEveryDayWheelBinding
import rubikstudio.library.LuckyWheelView
import rubikstudio.library.LuckyWheelView.LuckyRoundItemSelectedListener

class EveryDayWheelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEveryDayWheelBinding
    private lateinit var countDownTimer: CountDownTimer
    private var rewardedAd: RewardedAd? = null
    private val delayHandler = Handler(Looper.getMainLooper())

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityEveryDayWheelBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        val wheelViewModel: EveryDayWheelViewModel by viewModels()
        val luckyWheelView: LuckyWheelView = binding.luckyWheel

        wheelViewModel.initLuckyItemList(this)
        luckyWheelView.setData(wheelViewModel.luckyItemsList)
        luckyWheelView.isTouchEnabled = false
        luckyWheelView.setOnTouchListener { v, event ->
            showAds {
                countDownTimer.cancel()
                val indexAns = wheelViewModel.getIndexAfterRotate()
                luckyWheelView.setRound(3)
                luckyWheelView.startLuckyWheelWithTargetIndex(indexAns)
                wheelViewModel.updateCurrentSpinValue(wheelViewModel.getStringItemAtIndex(indexAns))
            }
            return@setOnTouchListener false
        }

        luckyWheelView.setLuckyRoundItemSelectedListener(object : LuckyRoundItemSelectedListener {
            override fun LuckyRoundItemSelected(index: Int) {
                showRewardDialogWithDelay(wheelViewModel.getStringItemAtIndex(index), 800) // 2 seconds delay
            }
        })

        binding.btnQuay.setOnClickListener {
            showAds {
                countDownTimer.cancel()
                val indexAns = wheelViewModel.getIndexAfterRotate()
                luckyWheelView.setRound(3)
                luckyWheelView.startLuckyWheelWithTargetIndex(indexAns)
                wheelViewModel.updateCurrentSpinValue(wheelViewModel.getStringItemAtIndex(indexAns))
            }
        }

        // Count down timer
        countDownTimer = object : CountDownTimer(7000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val secondsLeft = millisUntilFinished / 1000
                binding.tvConLaiXGiay.text = String.format(getString(R.string.con_lai_x_giay), secondsLeft.toString())
            }

            override fun onFinish() {
                binding.tvConLaiXGiay.text = String.format(getString(R.string.con_lai_x_giay), 0)
                binding.btnQuay.performClick()
            }
        }

        countDownTimer.start()

        // Load rewarded ad
        loadRewardedAd()
    }

    private fun loadRewardedAd() {
        val adRequest = AdRequest.Builder().build()
        RewardedAd.load(this, "ca-app-pub-3940256099942544/5224354917", adRequest, object : RewardedAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                rewardedAd = null
                Toast.makeText(this@EveryDayWheelActivity, "Failed to load ad: ${adError.message}", Toast.LENGTH_SHORT).show()
            }

            override fun onAdLoaded(ad: RewardedAd) {
                rewardedAd = ad
            }
        })
    }

    private fun showAds(onAdComplete: () -> Unit) {
        rewardedAd?.show(this) { rewardItem: RewardItem ->
            // Handle the reward
            onAdComplete()
            // Reload the ad for the next attempt
            loadRewardedAd()
        } ?: run {
            // Load the ad for the next attempt
            loadRewardedAd()
        }
    }

    private fun showRewardDialogWithDelay(reward: String, delayMillis: Long) {
        delayHandler.postDelayed({
            showRewardDialog(reward)
        }, delayMillis)
    }

    private fun showRewardDialog(reward: String) {
        AlertDialog.Builder(this)
            .setTitle("Congratulations!")
            .setMessage("You have won gem $reward")
            .setPositiveButton(android.R.string.ok, null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
        delayHandler.removeCallbacksAndMessages(null)
    }
}
