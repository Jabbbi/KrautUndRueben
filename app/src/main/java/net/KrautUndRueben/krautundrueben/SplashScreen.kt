package net.KrautUndRueben.krautundrueben

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_splash_screen.*
import java.util.*
import kotlin.concurrent.schedule

class SplashScreen : AppCompatActivity() {

    var drawableAnimation: AnimationDrawable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        imgview.setBackgroundResource(R.drawable.animation)
        drawableAnimation = imgview.background as AnimationDrawable
        startAnimation()
        Timer("Setting up", false).schedule(2000.toLong()) {
            val i = Intent(applicationContext, MainActivity::class.java)
            startActivity(i)
        }
    }

    fun startAnimation(){
        drawableAnimation!!.start()
    }

}
