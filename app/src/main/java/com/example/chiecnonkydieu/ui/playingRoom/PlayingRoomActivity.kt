package com.example.chiecnonkydieu.ui.playingRoom

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.marginTop
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.chiecnonkydieu.R
import com.example.chiecnonkydieu.adapters.LetterCardAdapter
import com.example.chiecnonkydieu.data.GameData
import com.example.chiecnonkydieu.data.GameData.gameModel
import com.example.chiecnonkydieu.data.GameModel
import com.example.chiecnonkydieu.data.GameStatus
import com.example.chiecnonkydieu.data.questionAnswerList
import com.example.chiecnonkydieu.databinding.ActivityPlayingRoomBinding
import com.example.chiecnonkydieu.ui.wheel.WheelActivity
import com.example.chiecnonkydieu.ui.wheel.WheelViewModel
import kotlinx.coroutines.launch
import rubikstudio.library.LuckyWheelView


class PlayingRoomActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayingRoomBinding
    // TODO

    var indexWheel: Int = -1
    var currentValueWheel: String = ""

    lateinit var adapter: LetterCardAdapter
    lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityPlayingRoomBinding.inflate(layoutInflater)
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

        val playingRoomViewModel: PlayingRoomViewModel by viewModels()
        lifecycleScope.launch {
            GameData.fetchGameModel(intent.getStringExtra("room_id").toString().toInt())
        }

        GameData.gameModel.observe(this, Observer { gameModel ->
            updateUi(gameModel)
        })

        //Setup recyclerview
        recyclerView = binding.recyclerviewLetterCards
        val layoutManager = LinearLayoutManager(this)

        layoutManager.orientation = LinearLayoutManager.HORIZONTAL // Set the orientation to horizontal if needed
        recyclerView.layoutManager = layoutManager
        adapter = LetterCardAdapter(GameData.gameModel.value!!.letterCardList)
        recyclerView.adapter = adapter





        binding.btnDoan.setOnClickListener {
            if (playingRoomViewModel.isUserInputMatch(binding.edtDoan.text.toString()[0])) {
                updateAdapterAndRecyclerView()
                Toast.makeText(this, "Bạn đoán đúng rồi", Toast.LENGTH_LONG).show()
            }
            else {
                Toast.makeText(this, "Bạn đoán sai rồi", Toast.LENGTH_LONG).show()
            }
            binding.edtDoan.text?.clear()

        }

        binding.btnGiai.setOnClickListener {
            showDialog(playingRoomViewModel)

        }

        binding.iconButton.setOnClickListener {
            Toast.makeText(this, "Hint click", Toast.LENGTH_LONG).show()
            playingRoomViewModel.setQuestionAndCurrentWordToBeGuessed(questionAnswerList.random())
            updateCurrentQuestionAndAnswer(GameData.gameModel.value!!)
        }

        // Wheel
        val wheelViewModel: WheelViewModel by viewModels<WheelViewModel>()
        val luckyWheelView: LuckyWheelView = binding.luckyWheel

        wheelViewModel.initLuckyItemList(this)
        luckyWheelView.setData(wheelViewModel.luckyItemsList)
        luckyWheelView.isTouchEnabled = false
        binding.llWheel.setOnClickListener {
            goToWheelActivity()
        }


    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    private fun goToWheelActivity() {
        val intent = Intent(this, WheelActivity::class.java)
        startActivity(intent)
    }

    private fun updateUi(gameModel: GameModel?) {
        if (gameModel != null) {
            updatePlayerList(gameModel)
            updateWheel(gameModel)
            updateVisibility(gameModel)
            updateCurrentQuestionAndAnswer(gameModel)
            updateAdapterAndRecyclerView()
        }
    }

    fun updateWheel(gameModel: GameModel) {
        if (!gameModel.currentSpinValue.isNullOrEmpty() && gameModel.currentSpinValue != currentValueWheel ) {
            val wheelViewModel: WheelViewModel by viewModels<WheelViewModel>()
            for (i in wheelViewModel.luckyItemsList.indices) {
                if (wheelViewModel.luckyItemsList[i].secondaryText == gameModel.currentSpinValue) {
                    indexWheel = i
                    currentValueWheel = gameModel.currentSpinValue
                    break
                }
            }
            binding.luckyWheel.setRound(0)
            binding.luckyWheel.startLuckyWheelWithTargetIndex(indexWheel)
        }
    }
    fun updateVisibility(gameModel: GameModel) {

        if (gameModel.gameStatus == GameStatus.INPROGRESS) {
            binding.llHint.visibility = View.GONE
            binding.tilDoan.visibility = View.GONE
            binding.btnDoan.visibility = View.GONE
            binding.btnGiai.visibility = View.GONE
        }
        else {
            binding.llHint.visibility = View.VISIBLE
            binding.tilDoan.visibility = View.VISIBLE
            binding.btnDoan.visibility = View.VISIBLE
            binding.btnGiai.visibility = View.VISIBLE
        }
    }

    private fun showDialog(viewModel: PlayingRoomViewModel) {

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Nhập lời giải cho câu hỏi")
        val viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_edittext, null, false)
        val input = viewInflated.findViewById<EditText>(R.id.input)
        builder.setView(viewInflated)

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->

            if (input.text.toString() == GameData.gameModel.value!!.currentQuestionAnswer.answer) {
                Toast.makeText(this, "giai thanh cong", Toast.LENGTH_LONG).show()
                 viewModel.makeAllLetterCardReveal()
            }
            dialog.dismiss()
        }

        builder.setNegativeButton(android.R.string.cancel) { dialog, which ->
            dialog.cancel()
        }

        builder.show()




    }

    private fun updateCurrentQuestionAndAnswer(gameModel: GameModel) {
        binding.tvQuestion.text = gameModel.currentQuestionAnswer.question
        updateAdapterAndRecyclerView()
    }

    private fun updatePlayerList(gameModel: GameModel) {
        when (gameModel.playersList.size) {
            1 -> {
                // player1
                val colorResId = R.color.orange
                val backgroundColor = ContextCompat.getColor(this, colorResId)
                binding.cvPlayer1.setCardBackgroundColor(backgroundColor)
                binding.imgLogo1.setImageResource(R.drawable.ic_man)
                binding.tvPlayerName1.text = gameModel.playersList[0].name
                binding.tvScore1.text = gameModel.playersList[0].score.toString()
            }
            2 -> {
                // player1 and player 2
                val colorResId = R.color.orange
                val backgroundColor = ContextCompat.getColor(this, colorResId)
                binding.imgLogo1.setImageResource(R.drawable.ic_man)
                binding.imgLogo2.setImageResource(R.drawable.ic_man)
                binding.tvPlayerName1.text = gameModel.playersList[0].name
                binding.tvPlayerName2.text = gameModel.playersList[1].name

                binding.tvScore1.text = gameModel.playersList[0].score.toString()
                binding.tvScore2.text = gameModel.playersList[1].score.toString()


            }

            3 -> {
                // 3 player
                val colorResId = R.color.orange
                val backgroundColor = ContextCompat.getColor(this, colorResId)
                binding.imgLogo1.setImageResource(R.drawable.ic_man)
                binding.imgLogo2.setImageResource(R.drawable.ic_man)
                binding.imgLogo3.setImageResource(R.drawable.ic_man)
                binding.tvPlayerName1.text = gameModel.playersList[0].name
                binding.tvPlayerName2.text = gameModel.playersList[1].name
                binding.tvPlayerName3.text = gameModel.playersList[2].name

                binding.tvScore1.text = gameModel.playersList[0].score.toString()
                binding.tvScore2.text = gameModel.playersList[1].score.toString()
                binding.tvScore3.text = gameModel.playersList[2].score.toString()
            }
        }
    }

    private fun updateAdapterAndRecyclerView() {
        adapter = LetterCardAdapter(GameData.gameModel.value!!.letterCardList)
        recyclerView.adapter = adapter
        adapter.notifyDataSetChanged()
    }
}