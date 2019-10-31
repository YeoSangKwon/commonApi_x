/*
 * Create by SangKwon on 2019. 10. 29.
 */

package md.winitech.com.commonapi_x.livedata

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_live_data.*
import md.winitech.com.commonapi_x.R

class LiveDataActivity : AppCompatActivity() {

    lateinit var model: LiveDataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_live_data)

        model = ViewModelProviders.of(this).get(LiveDataViewModel::class.java)

        val dataObserver: Observer<String> = Observer {
            txt_sample.text = it
        }

        model.mCurrentName.observe(this, dataObserver)

        btn_setData.setOnClickListener {
            val anotherText = edt_input.text.toString()
            model.mCurrentName.setValue(anotherText)
        }
    }

    override fun onPause() {
        super.onPause()
        model.mCurrentName.value = "onPause"
    }
}
