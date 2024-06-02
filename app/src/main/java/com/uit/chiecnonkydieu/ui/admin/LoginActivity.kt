package com.uit.chiecnonkydieu.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.uit.chiechnonkydieu.R
import com.uit.chiechnonkydieu.databinding.ActivityLoginBinding
import com.uit.chiecnonkydieu.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.btnDangNhap.setOnClickListener {
            if (binding.edtTaikhoan.text.toString() == "admin") {
                val intent: Intent = Intent(this, HomeAdminActivity::class.java)
                startActivity(intent)
            }
            else {
                val intent: Intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
}