/*
 * Create by SangKwon on 2019. 10. 29.
 */

package md.winitech.com.commonapi_x

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu
import android.app.PendingIntent
import android.view.MenuItem
import android.app.ActivityManager
import android.view.View
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import md.winitech.com.commonapi_x.livedata.LiveDataActivity
import md.winitech.com.commonapi_x.retrofit.RetrofitActivity
import md.winitech.com.commonapi_x.room.RoomActivity
import md.winitech.com.commonapi_x.service.AlwaysTopService
import java.lang.Exception


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    /**
     * 브로드캐스트 리시버에 신호가 들어오면 팬딩인텐트를 통해 액티비티를 실행시킴
     * */
    class mReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val data = intent?.getStringExtra("DATA")
            Log.e("MainActivity", "onClickButton $data")
            val mIntent = Intent(context, MainActivity::class.java)
            val mPendingIntent: PendingIntent =
                PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_ONE_SHOT)
            try {
                mPendingIntent.send()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private val ACTION_OVERLAY_PERMISSION: Int = 200
    private var receiver: BroadcastReceiver? = null
    private val bcAction = "broadCastTest"
    private var isShowing = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        registerReceiver()

        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show()
        }

        btn_start.setOnClickListener {
            //            startService()
        }

        btn_stop.setOnClickListener {
            stopService()
        }

        btn_room.setOnClickListener(mOnClickListener)
        btn_liveData.setOnClickListener(mOnClickListener)
        btn_retrofit.setOnClickListener(mOnClickListener)
    }

    /**
     * onPause에서 서비스를 동작시켜서 stop 되는 타이밍을 최대한으로 벌린다
     * */
    override fun onPause() {
        super.onPause()
        startService()
    }

    override fun onResume() {
        super.onResume()
        isShowing = true
        stopService()
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService()
        unregisterReceiver()
    }

    /**
     * 백키로 앱 종료 시 서비스 동작되는 것 방지
     * */
    override fun onBackPressed() {
        super.onBackPressed()
        isShowing = false
    }

    private var mOnClickListener = (View.OnClickListener { v ->
        isShowing = false
        when (v.id) {
            R.id.btn_room -> {
                val mIntent = Intent(this, RoomActivity::class.java)
                startActivity(mIntent)
            }
            R.id.btn_liveData -> {
                val mIntent = Intent(this, LiveDataActivity::class.java)
                startActivity(mIntent)
            }
            R.id.btn_retrofit -> {
                val mIntent = Intent(this, RetrofitActivity::class.java)
                startActivity(mIntent)
            }
        }
    })

    private fun startService() {
        if (!isServiceRunning() && isShowing) {
            if (!Settings.canDrawOverlays(this)) {
                val mIntent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
                startActivityForResult(mIntent, ACTION_OVERLAY_PERMISSION)
            } else {
                val serviceIntent = Intent(this, AlwaysTopService::class.java)
                serviceIntent.putExtra("inputExtra", "Foreground Service Test")
                startForegroundService(serviceIntent)
            }
        }
    }

    private fun stopService() {
        if (isServiceRunning()) {
            val serviceIntent = Intent(this, AlwaysTopService::class.java)
            stopService(serviceIntent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_OVERLAY_PERMISSION) {
            val serviceIntent = Intent(this, AlwaysTopService::class.java)
            serviceIntent.putExtra("inputExtra", "Foreground Service Test")
            ContextCompat.startForegroundService(this, serviceIntent)
        }
    }


    /**
     * 브로드캐스트 리시버 등록 및 해제
     * */
    private fun registerReceiver() {
        if (null != receiver) receiver = null
        this.receiver = mReceiver()
        val filter = IntentFilter()
        filter.addCategory(Intent.CATEGORY_DEFAULT)
        filter.addAction(bcAction)
        LocalBroadcastManager.getInstance(this).registerReceiver(this.receiver as mReceiver, filter)
    }

    private fun unregisterReceiver() {
        if (receiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(this.receiver as mReceiver)
            receiver = null
        }
    }

    /**
     * 현재 해당 서비스가 실행중인지 확인
     * */
    fun isServiceRunning(): Boolean {
        val manager: ActivityManager = this.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (AlwaysTopService::class.java.name == service.service.className) {
                return true
            }
        }
        return false
    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}
