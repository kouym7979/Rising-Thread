package com.example.thread

import android.animation.ObjectAnimator
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.SystemClock
import android.util.Log
import android.widget.ImageView
import com.example.thread.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var isRunning =true
    var handler:Handler = Handler(Looper.getMainLooper())
    var runnable:Runnable= Runnable {  }
    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val thread = ThreadClass()

        DragonMove(binding.redDragon,600f,600f,2500L)

    }
    fun DragonMove(image1 : ImageView, posX:Float, posY:Float,duration1:Long){
        runnable=object:Runnable{
            override fun run() {
                ObjectAnimator.ofFloat(image1,"translationX",posX).apply {
                    duration=duration1
                    start()
                }
                ObjectAnimator.ofFloat(image1,"translationY",posY).apply {
                    duration=duration1
                    start()
                }
                handler.postDelayed(runnable,duration1)
            }
        }
        handler.post(runnable)

    }
    //안드로이드에서 UI는 메인 쓰레드에서만 접근할 수 있다. 다른 쓰레드에서는 접근할 수 없다.
    //runOnUiThread메소드를 통해서 메인 쓰레드의 UI를 접근할 수 있다.
    /*inner class ThreadClass:Thread(){
        override fun run(){
            while (isRunning){
                SystemClock.sleep(100)//0.1초 간격
                Log.d("확인",System.currentTimeMillis().toString())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning=false
    }*/

}