/*
 * Create by SangKwon on 2019. 11. 6.
 */

package md.winitech.com.commonapi_x.koin

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_koin.*
import md.winitech.com.commonapi_x.R
import md.winitech.com.commonapi_x.koin.component.DataPresenter
import org.koin.android.ext.android.inject

class KoinActivity : AppCompatActivity() {

    private val _presenter : DataPresenter by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_koin)

        txt_mainText.text = _presenter.std
    }
}
