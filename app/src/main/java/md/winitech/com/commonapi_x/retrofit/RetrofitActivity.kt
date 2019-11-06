/*
 * Create by SangKwon on 2019. 10. 30.
 */

package md.winitech.com.commonapi_x.retrofit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock.sleep
import android.util.Log
import android.view.View
import androidx.core.os.postDelayed
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_retrofit.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import md.winitech.com.commonapi_x.R
import md.winitech.com.commonapi_x.retrofit.adapter.recyclerAdapter
import md.winitech.com.commonapi_x.retrofit.inf.retroKtinterface
import md.winitech.com.commonapi_x.retrofit.livedata.dataViewModel
import md.winitech.com.commonapi_x.retrofit.model.dataModelList
import md.winitech.com.commonapi_x.retrofit.okhttp3.apiClientKt
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import kotlin.coroutines.CoroutineContext

class RetrofitActivity : AppCompatActivity(), recyclerAdapter.ViewClickListener, CoroutineScope {
    private val TAG = "RetrofitActivity"

    private lateinit var job: Job
    override val coroutineContext: CoroutineContext get() = Dispatchers.Main + job
    val mHandler: Handler = Handler()

    private val url = "http://192.168.15.56:3001/crawling/"

    override fun onItemClicked(position: Int) {

    }

    override fun onItemLongClicked(position: Int) {

    }


    private val apiClient: apiClientKt = apiClientKt(URLS = this.url)
    lateinit var retrofit: Retrofit
    private lateinit var interfaceKt: retroKtinterface

    private var adapter: recyclerAdapter? = null
    private var mListdata: Call<List<dataModelList.dataModel>>? = null
    private var tempList: List<dataModelList.dataModel>? = null

    lateinit var model: dataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)
        job = Job()
        retrofit = apiClient.getClient()
        interfaceKt = retrofit.create(retroKtinterface::class.java)

        initRecycler()
        initLiveData()

        pollingService()
        btn_search.setOnClickListener(mOnClickListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

    private val mOnClickListener = (View.OnClickListener {
        mListdata = interfaceKt.getData()
        mListdata!!.enqueue(mCallback)
    })

    private var mCallback = (object : Callback<List<dataModelList.dataModel>> {
        override fun onFailure(call: Call<List<dataModelList.dataModel>>, t: Throwable) {

        }

        override fun onResponse( call: Call<List<dataModelList.dataModel>>, response: Response<List<dataModelList.dataModel>>) {
            val mList: List<dataModelList.dataModel>? = response.body()
            if (null == tempList) {
                tempList = mList
                model.setValue(mList!!)
            } else if (!tempList!!.containsAll(mList!!)) {
                adapter!!.removeItem(tempList!!)
                tempList = mList
                model.setValue(mList)
            } else {
                Log.e(TAG, "received same data")
            }
        }
    })

    private fun initRecycler() {
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.my_recycler_view)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = recyclerAdapter()
        adapter!!.setOnClickListener(this)
        recyclerView.adapter = adapter
    }

    private fun initLiveData() {
        model = ViewModelProviders.of(this).get(dataViewModel::class.java)
        val dataObserver: Observer<List<dataModelList.dataModel>> = Observer {
            dataSet(it!!)
        }
        model.dataModel.observe(this, dataObserver)
    }

    private fun pollingService() {
        launch {
            mListdata = interfaceKt.getData()
            mListdata!!.enqueue(mCallback)
            mHandler.postDelayed(10000) {
                pollingService()
            }
        }
    }

    private fun dataSet(mList: List<dataModelList.dataModel>) {
        run loop@{
            mList.forEach { item ->
                adapter?.addItem(item)
            }
        }
        adapter!!.notifyDataSetChanged()
    }
}
