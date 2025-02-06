package com.example.privaditaapp.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.privaditaapp.R
import kotlinx.coroutines.*

@SuppressLint("CustomSplashScreen")
class SplashScreen : AppCompatActivity() {

    @OptIn(DelicateCoroutinesApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen_layout)

        val fadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val splashScreenActivity = findViewById<FrameLayout>(R.id.splash_screen)

        splashScreenActivity.startAnimation(fadeInAnimation)

        // Delay for 3 seconds and then start the LoginActivity
        GlobalScope.launch {
            delay(2000)
            startActivity(Intent(this@SplashScreen, MainActivity::class.java))
            finish()
        }
    }
}
