/*
 * Create by SangKwon on 2019. 11. 4.
 */

package md.winitech.com.commonapi_x.coroutine

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock.sleep
import android.util.Log
import android.view.View
import androidx.core.os.postDelayed
import kotlinx.android.synthetic.main.activity_coroutine.*
import kotlinx.coroutines.*
import md.winitech.com.commonapi_x.R
import java.lang.Exception
import kotlin.coroutines.CoroutineContext

class CoroutineActivity : AppCompatActivity(), CoroutineScope {

    //Dispatchers.Main + job : 해당 액티비티에서만 동작하는 스코프설정
    private lateinit var job: Job
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job + exceptionHandler
    private val exceptionHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        Log.e(TAG, "CoroutineExceptionHandler")
    }
    private val TAG = "CoroutineActivity"

    val mHandler: Handler = Handler()

    /**
     * GlobalScope : Application이 종료 될 때 까지 동작
     * CoroutineScope : Activity가 종료 될 때 까지 동작
     * */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine)

        //스코프
        job = Job()

        btn_coroutine_time.setOnClickListener(mOnClickListener)     //일정 시간동안 쓰레드 동작
        btn_coroutine_fun.setOnClickListener(mOnClickListener)      //외부 함수 코루틴 호출, withContext사용 (async 와 동일하지만 await을 호출 할 필요가 없음)
        btn_coroutine_scope.setOnClickListener(mOnClickListener)    //액티비티 생명주기에 맞춘 코루틴 동작, Exception 처리
        btn_coroutine_polling.setOnClickListener(mOnClickListener)  //풀링서비스
    }

    override fun onDestroy() {
        super.onDestroy()
        //코루틴 종료 (액티비티와 생명주기를 맞춤)
        job.cancel()
    }

    //코루틴으로 외부 함수 호출
    private suspend fun doSomething(des: Any) {
        GlobalScope.launch {
            Log.e(TAG, "doSomething -$des")
        }
    }

    //폴링서비스
    var temp = 0
    @SuppressLint("SetTextI18n")
    private fun pollingService() {
        launch {
            txt_routine.text = txt_routine.text.toString() + temp
            temp++
            mHandler.postDelayed(5000){
                pollingService()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private var mOnClickListener = (View.OnClickListener {
        when (it.id) {
            R.id.btn_coroutine_time -> {

                var job = GlobalScope.launch(Dispatchers.Default) {
                    Log.e(TAG, "GlobalScope")
                    var i = 1
                    repeat(5) {
                        Log.e(TAG, "GlobalScope $i")
                        delay(1000)
                        txt_routine.text = txt_routine.text.toString() + i
                        i++
                    }

                    var value = async {
                        1 + 2
                    }.await()
                    Log.e(TAG, "$value")
                }
                runBlocking {
                    Log.e(TAG, "runBlocking")
                    delay(100)
                    Log.e(TAG, "runBlocking delay")
                    job.join()
                }
            }

            R.id.btn_coroutine_fun -> {
                GlobalScope.launch {
                    Log.e(TAG, "call doSomething")
                    doSomething("doSomething")

                    val someThing = async(Dispatchers.Default) {
                        Log.e(TAG, "call async")
                        sleep(1000)
                        doSomething("async")
                    }
                    someThing.await()

                    var name = withContext(Dispatchers.Default) {
                        Log.e(TAG, "call withContext")
                        sleep(2000)
                        doSomething("withContext")
                    }
                }
            }

            R.id.btn_coroutine_scope -> {
                launch {
                    sleep(2000)
                    Log.e(TAG, "call btn_coroutine_scope")
                    throw Exception()
                }
            }

            R.id.btn_coroutine_polling -> {
                pollingService()
            }
        }
    })
}
