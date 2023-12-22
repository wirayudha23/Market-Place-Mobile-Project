package com.example.ichoosecoffetoday.onBoarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.ichoosecoffetoday.Activity.MainActivity
import com.example.ichoosecoffetoday.R
import com.example.ichoosecoffetoday.login_regis.LoginActivity

class GetStartedActivity : AppCompatActivity() {
    private lateinit var startButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.ichoosecoffetoday.R.layout.activity_get_started)

        startButton = findViewById(com.example.ichoosecoffetoday.R.id.lanjut)
        startButton.setOnClickListener {
            val intent = Intent(this@GetStartedActivity, SliderScreenActivity::class.java)
//            val intent = Intent(this@GetStarted, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}