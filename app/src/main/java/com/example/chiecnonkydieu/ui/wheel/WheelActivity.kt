package com.example.chiecnonkydieu.ui.wheel

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.get
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameStatus
import com.example.chiecnonkydieu.databinding.ActivityWheelBinding
import rubikstudio.library.LuckyWheelView
import rubikstudio.library.model.LuckyItem
import java.util.concurrent.locks.ReentrantLock

class WheelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWheelBinding
    private lateinit var countDownTimer: CountDownTimer

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityWheelBinding.inflate(layoutInflater)
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

        val wheelViewModel: WheelViewModel by viewModels<WheelViewModel>()
        val luckyWheelView: LuckyWheelView = binding.luckyWheel

        wheelViewModel.initLuckyItemList(this)
        luckyWheelView.setData(wheelViewModel.luckyItemsList)
        luckyWheelView.isTouchEnabled = false
        luckyWheelView.setOnTouchListener { v, event ->
            countDownTimer.cancel()
            val indexAns = wheelViewModel.getIndexAfterRotate()
            luckyWheelView.setRound(3)
            luckyWheelView.startLuckyWheelWithTargetIndex(indexAns)
            Toast.makeText(this, wheelViewModel.getStringItemAtIndex(indexAns), Toast.LENGTH_LONG).show()
            wheelViewModel.updateCurrentSpinValue(wheelViewModel.getStringItemAtIndex(indexAns))
            return@setOnTouchListener false
        }


        binding.btnQuay.setOnClickListener {
            countDownTimer.cancel()
            val indexAns = wheelViewModel.getIndexAfterRotate()
            luckyWheelView.setRound(3)
            luckyWheelView.startLuckyWheelWithTargetIndex(indexAns)
            Toast.makeText(this, wheelViewModel.getStringItemAtIndex(indexAns), Toast.LENGTH_LONG).show()
            wheelViewModel.updateCurrentSpinValue(wheelViewModel.getStringItemAtIndex(indexAns))
        }

        // count down timer
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

    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer.cancel()
    }
}