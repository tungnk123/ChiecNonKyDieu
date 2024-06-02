package com.uit.chiecnonkydieu.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityCreateRoomBinding
import com.uit.chiecnonkydieu.data.GameData
import com.uit.chiecnonkydieu.data.GameModel
import com.uit.chiecnonkydieu.data.GameStatus
import com.uit.chiecnonkydieu.model.LetterCard
import com.uit.chiecnonkydieu.model.Player
import com.uit.chiecnonkydieu.data.questionAnswerList
import com.uit.chiecnonkydieu.model.QuestionAnswer
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "player_data")
val CURRENT_PLAYER = stringPreferencesKey("current_player")
class CreateRoomActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateRoomBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityCreateRoomBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.create_room_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val toolbar: Toolbar = binding.toolbar
        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)


        binding.tvMaPhong.text = (1000 .. 9999).random().toString()
        binding.btnCreateRoom.setOnClickListener {
            lifecycleScope.launch {
                GameData.initialize(applicationContext)
                val gameModel: GameModel = GameModel(
                    gameStatus = GameStatus.CREATED,
                    gameId = binding.tvMaPhong.text.toString().toInt(),
                    currentQuestionAnswer = questionAnswerList[1],
                    guessesCharacters = mutableListOf(),
                    currentPlayer = Player(name = binding.edtName.text.toString()),
                    letterCardList = getLetterCardListFromAnswer(questionAnswerList[0].DoiTuongInHoa),
                    previousQuestionAnswers = mutableListOf<QuestionAnswer>().apply {
                        add(questionAnswerList[1])
                    }
                )
                gameModel.playersList.add(Player(binding.edtName.text.toString()))
                GameData.saveGameModel(
                    gameModel
                )
                saveCurrentPlayerToPreferencesDataStore(binding.edtName.text.toString())

            }
            goToWaitingRooom()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }


    private fun saveCurrentPlayerToPreferencesDataStore(currentPlayer: String) {
        lifecycleScope.launch {
            dataStore.edit { settings ->
                settings[CURRENT_PLAYER] = currentPlayer
            }
            val savedPlayer = dataStore.data.first()[CURRENT_PLAYER]
            Log.d("DataStore", "Saved player from create: $savedPlayer")
        }
    }


    private fun getLetterCardListFromAnswer(answer: String): MutableList<LetterCard>{
        val list = mutableListOf<LetterCard>()
        for (i in answer.indices) {
            list.add(LetterCard(answer[i].toString()))
        }
        return list
    }
    private fun goToWaitingRooom() {
        val intent = Intent(this, WaitingRoomActivity::class.java)
        intent.putExtra("room_id", binding.tvMaPhong.text.toString())
        startActivity(intent)
    }
}