package com.example.thread

import android.animation.ObjectAnimator
import android.hardware.display.DisplayManager
import android.media.MediaPlayer
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.example.thread.databinding.ActivityMainBinding
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private var isRunning =true
    var handler:Handler = Handler(Looper.getMainLooper())
    var runnable:Runnable= Runnable {  }
    lateinit var media2 : MediaPlayer
    var score=0
    var imageArr=ArrayList<ImageView>()
    private var time:Long =10000
    private var main_delay :Long = 800
    private val intArr = arrayOfNulls<Int>(100)

    private lateinit var binding:ActivityMainBinding

    val display =DisplayMetrics()
    private var width : Int ?=null
    private var height : Int ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        media2=MediaPlayer.create(this,R.raw.attack)

        imageArr= arrayListOf(binding.imageView,binding.imageView2,binding.imageView3,
                            binding.imageView4,binding.imageView5,binding.imageView6,
                            binding.imageView7,binding.imageView8,binding.imageView9)

        hideImage(main_delay)
        binding.roundUp.setOnClickListener(this)


        //이걸 쓰레드에 넣었다가 재시작
        object:CountDownTimer(time, 1000){
            override fun onFinish() {
                binding.time.text="Game Over"
                handler.removeCallbacks(runnable)
                for(image in imageArr){
                    image.visibility= View.INVISIBLE
                }
                media2.pause()


            }
            override fun onTick(millisUntilFinished: Long) {
                binding.time.text="Time:"+millisUntilFinished/1000
            }
        }.start()

        //val thread = ThreadClass()

        width=display.widthPixels
        height=display.heightPixels
        Log.d("확인","height:"+height)
        //thread.start()


    }

    fun hideImage(deley :Long) {

        runnable = object : Runnable {
            override fun run() {
                for (image in imageArr) {
                    image.visibility = View.INVISIBLE
                }

                val random = Random()
                val index = random.nextInt(8 - 0)
                imageArr[index].visibility = View.VISIBLE

                handler.postDelayed(runnable, deley)

            }
        }
        handler.post(runnable)
    }

    fun increaseScore(view: View) {
        score++
        media2.start()
        binding.score.text = "Score: " + score
        media2.start()
    }
    override fun onDestroy() {
        Log.d("확인","종료되었습니다.")
        isRunning=false
        super.onDestroy()
    }

    override fun onStop() {
        media2.pause()
        super.onStop()
    }

    override fun onClick(v: View?) {
        main_delay-=50
        //hideImage(main_deley)

    }


}