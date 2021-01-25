package com.example.thread

import android.animation.ObjectAnimator
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.TextView
import com.example.thread.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private var isRunning = true
    var handler: Handler = Handler(Looper.getMainLooper())
    var runnable: Runnable = Runnable { }

    lateinit var media : MediaPlayer

    private lateinit var binding: ActivityStartBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)

        media=MediaPlayer.create(this,R.raw.comedy_fun)
        media.start()
        //media.isLooping=true
        setContentView(binding.root)

        binding.btnStart.setOnClickListener {
            var intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            finish()

        }

        val thread = ThreadClass()

        thread.start()
    }

    //안드로이드에서 UI는 메인 쓰레드에서만 접근할 수 있다. 다른 쓰레드에서는 접근할 수 없다.
    //runOnUiThread메소드를 통해서 메인 쓰레드의 UI를 접근할 수 있다.


    fun TextMove(text1: TextView, posX: Float, posY: Float, duration1: Long) {
        runnable = object : Runnable {
            override fun run() {
                ObjectAnimator.ofFloat(text1, "translationY", posY).apply {
                    duration = duration1
                    start()
                }
                handler.postDelayed(runnable, duration1)
            }
        }
        handler.post(runnable)

    }
    inner class ThreadClass : Thread() {
        override fun run() {
            //runOnUiThread {
                TextMove(binding.startText, 0f, 750f, 1000L)
            //}
        }
    }
    override fun onDestroy() {
        media.pause()
        super.onDestroy()
        Log.d("확인", "종료되었습니다.")
        isRunning = false
    }

    override fun onStop() {
        super.onStop()
    }
    override fun onResume() {
        super.onResume()
    }
}