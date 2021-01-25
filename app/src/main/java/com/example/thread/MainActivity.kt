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
    private var isCheck=false
    var handler:Handler = Handler(Looper.getMainLooper())
    var runnable:Runnable= Runnable {  }
    lateinit var media2 : MediaPlayer
    lateinit var media1 : MediaPlayer
    var score=0
    var imageArr=ArrayList<ImageView>()
    private var time:Long =10000
    private var main_delay :Long = 800
    private var timerTask:Timer?=null
    private var index : Int=1

    private lateinit var binding:ActivityMainBinding

    val display =DisplayMetrics()
    private var width : Int ?=null
    private var height : Int ?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        media2=MediaPlayer.create(this,R.raw.attack)
        media1=MediaPlayer.create(this,R.raw.comedy_fun)
        imageArr= arrayListOf(binding.imageView,binding.imageView2,binding.imageView3,
                            binding.imageView4,binding.imageView5,binding.imageView6,
                            binding.imageView7,binding.imageView8,binding.imageView9)
        media1.start()
        hideImage(main_delay)

        
        binding.level.visibility=View.GONE

        //이걸 쓰레드에 넣었다가 재시작


        object:CountDownTimer(time, 1000){
            override fun onFinish() {
                binding.time.text="Game Over"
                handler.removeCallbacks(runnable)
                for(image in imageArr){
                    image.visibility= View.INVISIBLE
                }
                media2.pause()
                binding.level.visibility=View.VISIBLE
                isRunning=false
                Log.d("확인","끝났습니다!!!")
            }
            override fun onTick(millisUntilFinished: Long) {
                binding.time.text="Time:"+millisUntilFinished/1000
            }
        }.start()

        val thread = ThreadClass()
        //인텐트 플래그 추가
        width=display.widthPixels
        height=display.heightPixels
        Log.d("확인","height:"+height)
        thread.start()


    }

    inner class ThreadClass : Thread(){
        override fun run() {
            if(isRunning){
                //hideImage(main_delay)
                runOnUiThread {
                   binding.level.setOnClickListener {
                       binding.level.visibility=View.GONE
                       isCheck=false
                       if(isCheck==false)
                           binding.level.visibility=View.GONE
                       checkTime(time)
                       isCheck=true
                       if(isCheck==true)
                           binding.level.visibility=View.VISIBLE
                       main_delay-=50
                       hideImage(main_delay)
                       Log.d("확인","쓰레드입니다!!!")
                   }

               }
            }
            else{
                Log.d("확인","쓰레드입니다else입니다!!!")
                checkTime(time)

            }
        }
    }



    fun checkTime(time:Long){
        object:CountDownTimer(time, 1000){
            override fun onFinish() {
                binding.time.text="Game Over"
                handler.removeCallbacks(runnable)
                for(image in imageArr){
                    image.visibility= View.INVISIBLE
                }
                media2.pause()
                isRunning=false

            }
            override fun onTick(millisUntilFinished: Long) {
                binding.time.text="Time:"+millisUntilFinished/1000
            }
        }.start()
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
        media1.pause()
        super.onDestroy()
    }

    override fun onStop() {
        media2.pause()
        media1.pause()
        super.onStop()
    }

    override fun onClick(v: View?) {
        main_delay-=50
        //hideImage(main_deley)

    }


}