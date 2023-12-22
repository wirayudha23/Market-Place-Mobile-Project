package com.example.ichoosecoffetoday.onBoarding

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.ichoosecoffetoday.R.id.lanjut1
import android.view.View
import com.example.ichoosecoffetoday.Activity.MainActivity
import com.example.ichoosecoffetoday.R
import com.example.ichoosecoffetoday.login_regis.LoginActivity

class SliderScreenActivity : AppCompatActivity() {
    private lateinit var startButton: Button


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.ichoosecoffetoday.R.layout.activity_slider_screen)

        startButton = findViewById(com.example.ichoosecoffetoday.R.id.lanjut1)
        startButton.setOnClickListener {
            val intent = Intent(this@SliderScreenActivity, LoginActivity::class.java)
//            val intent = Intent(this@GetStarted, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}