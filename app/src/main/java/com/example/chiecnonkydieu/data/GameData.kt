package com.example.chiecnonkydieu.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.CompletableDeferred
import kotlin.coroutines.coroutineContext

object GameData {
    private var _gameModel: MutableLiveData<GameModel> = MutableLiveData()
    val gameModel: LiveData<GameModel> = _gameModel

    const val REFERENCE_ROOM = "rooms"
    private lateinit var appContext: Context


    val database: DatabaseReference = Firebase.database.reference

    fun initialize(context: Context) {

        appContext = context.applicationContext
    }
    fun saveGameModel(model: GameModel) {
        _gameModel.postValue(model)
        database.child(REFERENCE_ROOM).child(model.gameId.toString()).setValue(model)
    }

    suspend fun joinOnlineGame(player: Player, gameId: Int): Boolean {
        val deferred = CompletableDeferred<Boolean>()

        database.child(REFERENCE_ROOM).child(gameId.toString())
            .get()
            .addOnSuccessListener { snapshot ->
                val gameModel: GameModel? = snapshot.getValue(GameModel::class.java)
                if (gameModel != null) {
                    when (gameModel.playersList.size) {
                        1 -> {
                            gameModel.playersList.add(player)
                            gameModel.gameStatus = GameStatus.JOINED1
                            saveGameModel(gameModel)
                            Toast.makeText(appContext, "Successfully joined1 game", Toast.LENGTH_LONG).show()
                            deferred.complete(true)
                        }
                        2 -> {
                            gameModel.playersList.add(player)
                            gameModel.gameStatus = GameStatus.JOINED2
                            saveGameModel(gameModel)
                            Toast.makeText(appContext, "Successfully joined2 game", Toast.LENGTH_LONG).show()
                            deferred.complete(true)
                        }
                        else -> {
                            Toast.makeText(appContext, "Game is full (3 players)", Toast.LENGTH_LONG).show()
                            deferred.complete(false)
                        }
                    }
                } else {
                    Toast.makeText(appContext, "Cannot find room with id $gameId", Toast.LENGTH_LONG).show()
                    deferred.complete(false)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(appContext, "Error: $exception", Toast.LENGTH_LONG).show()
                deferred.complete(false)
            }

        return deferred.await()
    }

    suspend fun fetchGameModel(gameId: Int) {
        database.child(REFERENCE_ROOM).child(gameId.toString())
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val gameModel: GameModel? = snapshot.getValue(GameModel::class.java)
                    if (gameModel != null) {
                        saveGameModel(gameModel)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                    Log.e("fetchGameModel", "Error fetching game model: ${error.message}")
                }
            })
    }
}