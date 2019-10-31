/*
 * Create by SangKwon on 2019. 10. 31.
 */

package md.winitech.com.commonapi_x.service

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.IBinder
import android.view.*
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.alwaus_on_touch_view.view.*
import md.winitech.com.commonapi_x.MainActivity
import md.winitech.com.commonapi_x.R

class AlwaysTopService : Service() {

    val CHANNEL_ID = "ForegroundServiceChannel"

    private var mView: View? = null
    private var windowManager: WindowManager? = null
    lateinit var params: WindowManager.LayoutParams

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("InflateParams")
    override fun onCreate() {
        super.onCreate()
        val mInflater: LayoutInflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        mView = mInflater.inflate(R.layout.alwaus_on_touch_view, null)
        mView!!.setOnTouchListener(mOnTouchListener)
        mView!!.btn_test?.setOnClickListener {
            var mIntent = Intent("broadCastTest")
            mIntent.putExtra("DATA","TEST")
            LocalBroadcastManager.getInstance(this).sendBroadcast(mIntent)
        }

        try {
            windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT
                )
            } else {
                params = WindowManager.LayoutParams(
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                            or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                            or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN
                            or WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR,
                    PixelFormat.TRANSLUCENT
                )
            }
            params.gravity = Gravity.TOP or Gravity.LEFT
            windowManager!!.addView(mView, params)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val input = intent!!.getStringExtra("inputExtra")
        createNotificationChannel()
        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0, notificationIntent, 0
        )

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Foreground Service")
            .setContentText(input)
            .setSmallIcon(R.drawable.check_box)
            .setContentIntent(pendingIntent)
            .build()

        startForeground(1, notification)
        return Service.START_NOT_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mView != null) {
            try {
                windowManager!!.removeView(mView)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val serviceChannel = NotificationChannel(
                CHANNEL_ID,
                "Foreground Service Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )

            val manager = getSystemService(NotificationManager::class.java)
            manager!!.createNotificationChannel(serviceChannel)
        }
    }

    private var isMove = false
    private var mTouchX: Float = 0.toFloat()
    private var mTouchY: Float = 0.toFloat()
    private var mViewX: Int = 0
    private var mViewY: Int = 0

    private val mOnTouchListener = View.OnTouchListener { v, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                isMove = false
                mTouchX = event.rawX
                mTouchY = event.rawY
                mViewX = params.x
                mViewY = params.y
            }
            MotionEvent.ACTION_UP -> if (!isMove) {
                Toast.makeText(applicationContext, "클릭", Toast.LENGTH_SHORT).show()
            }
            MotionEvent.ACTION_MOVE -> {
                isMove = true
                val x = (event.rawX - mTouchX).toInt()
                val y = (event.rawY - mTouchY).toInt()
                val num = 5
                if (x > -num && x < num && y > -num && y < num) {
                    isMove = false
                    return@OnTouchListener true
                }
                params.x = mViewX + x
                params.y = mViewY + y
                windowManager!!.updateViewLayout(mView, params)
            }
        }
        return@OnTouchListener true
    }
}