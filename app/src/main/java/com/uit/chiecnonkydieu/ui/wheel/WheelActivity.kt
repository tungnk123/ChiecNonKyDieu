package com.uit.chiecnonkydieu.ui.wheel

import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityWheelBinding
import com.uit.chiecnonkydieu.AppContainer
import com.uit.chiecnonkydieu.ChiecNonKyDieuApplication
import com.uit.chiecnonkydieu.model.MintResponse
import com.uit.chiecnonkydieu.network.MintRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import rubikstudio.library.LuckyWheelView

class WheelActivity : AppCompatActivity() {
    private lateinit var binding: ActivityWheelBinding
    private lateinit var countDownTimer: CountDownTimer
    private val appContainer = AppContainer()

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

        binding.btnMissTurnDemo.setOnClickListener {
            countDownTimer.cancel()
            val indexAns = 6 // vi tri mac dinh cua miss turn item
            luckyWheelView.setRound(3)
            luckyWheelView.startLuckyWheelWithTargetIndex(indexAns)
            Toast.makeText(this, wheelViewModel.getStringItemAtIndex(indexAns), Toast.LENGTH_LONG).show()
            wheelViewModel.updateCurrentSpinValue(wheelViewModel.getStringItemAtIndex(indexAns))
        }

        binding.btnItemDemo.setOnClickListener {
            countDownTimer.cancel()
            val indexAns = 10 // vi tri mac dinh cua item demo
            luckyWheelView.setRound(3)
            luckyWheelView.startLuckyWheelWithTargetIndex(indexAns)
            Toast.makeText(this, wheelViewModel.getStringItemAtIndex(indexAns), Toast.LENGTH_LONG).show()
            wheelViewModel.updateCurrentSpinValue(wheelViewModel.getStringItemAtIndex(indexAns))


            showMintDialog("0x262dA04adF6C48Abf80eB1D486b8235A22097447")
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
    private fun showMintDialog(walletAddress: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Congratulations!")
        builder.setMessage("You won an NFT! Would you like to mint NFT to your address?")

        builder.setPositiveButton("Mint") { dialog, which ->
            // Mint another NFT
            val uuid = "82d00203-738e-4136-8514-7a8aac192bd9"
            val authorization = "Basic NWM3MDYwNDYtYzg3My00MjI4LTk5MWEtMDZhMDM3MTU3ZGNlOjZkNGlqSTdSOVVkMA=="
            val request = MintRequest(walletAddress, 1)

            try {
                appContainer.api.mintNFT(uuid, authorization, request).enqueue(object :
                    Callback<MintResponse> {
                    override fun onResponse(call: Call<MintResponse>, response: Response<MintResponse>) {
                        if (response.isSuccessful) {
                            val mintResponse = response.body()
                            if (mintResponse != null && mintResponse.data.success) {
                                Toast.makeText(this@WheelActivity, "Minting successful! Transaction hash: ${mintResponse.data.transactionHash}", Toast.LENGTH_LONG).show()
                            } else {
                                Toast.makeText(this@WheelActivity, "Minting failed: ${response.message()}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this@WheelActivity, "Minting failed: ${response.message()}", Toast.LENGTH_LONG).show()
                        }
                    }
                    override fun onFailure(call: Call<MintResponse>, t: Throwable) {
                        Toast.makeText(this@WheelActivity, "Minting error: ${t.message}", Toast.LENGTH_LONG).show()
                        Log.d("Api", t.message.toString())
                    }
                })
            } catch (e: Exception) {
                Log.d("Api", e.message.toString())
            }
        }

        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.dismiss()
        }

        builder.show()
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