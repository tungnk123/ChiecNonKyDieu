package com.uit.chiecnonkydieu.ui.leaderboard

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityLeaderboardBinding
import com.uit.chiecnonkydieu.adapter.LeaderboardAdapter
import com.uit.chiecnonkydieu.model.LeaderboardItem

class LeaderboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLeaderboardBinding
    private lateinit var leaderboardItemList: List<LeaderboardItem>
    private lateinit var leaderboardAdapter: LeaderboardAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLeaderboardBinding.inflate(layoutInflater)
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

        leaderboardItemList = listOf(
            LeaderboardItem(
                stt = 1,
                name = "Thanh Xuan",
                score =  80
            ),
            LeaderboardItem(
                stt = 2,
                name = "Ngoc Linh",
                score =  60
            ),
            LeaderboardItem(
                stt = 3,
                name = "Tram Anh",
                score =  50
            )
        )

        leaderboardAdapter = LeaderboardAdapter(leaderboardItemList)
        binding.rcvLeaderboard.apply {
            layoutManager = LinearLayoutManager(this@LeaderboardActivity)
            adapter = leaderboardAdapter
        }


    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}