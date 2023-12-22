package com.example.ichoosecoffetoday.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.os.Handler
import android.view.Menu
import com.example.ichoosecoffetoday.R
import com.example.ichoosecoffetoday.Activity.SplashActivity
import com.example.ichoosecoffetoday.login_regis.LoginActivity
import com.example.ichoosecoffetoday.onBoarding.GetStartedActivity


class SplashActivity : AppCompatActivity() {

    private val SPLASH_TIME_OUT:Long = 3000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_activity)

        Handler().postDelayed({
            startActivity(Intent(this,GetStartedActivity ::class.java))
            finish()
        }, SPLASH_TIME_OUT)
    }
}