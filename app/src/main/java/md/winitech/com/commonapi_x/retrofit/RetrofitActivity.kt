/*
 * Create by SangKwon on 2019. 10. 30.
 */

package md.winitech.com.commonapi_x.retrofit

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_retrofit.*
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

class RetrofitActivity : AppCompatActivity(), recyclerAdapter.ViewClickListener{
    private val url = "http://192.168.15.56:3001/crawling/"

    override fun onItemClicked(position: Int) {

    }

    override fun onItemLongClicked(position: Int) {

    }


    val apiClient: apiClientKt = apiClientKt(URLS = this.url)
    lateinit var retrofit: Retrofit
    lateinit var interfaceKt: retroKtinterface

    private var adapter: recyclerAdapter? = null
    private var mListdata: Call<List<dataModelList.dataModel>>? = null

    lateinit var model:dataViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_retrofit)

        retrofit = apiClient.getClient()
        interfaceKt = retrofit.create(retroKtinterface::class.java)

        initRecycler()

        model = ViewModelProviders.of(this).get(dataViewModel::class.java)
        val dataObserver: Observer<List<dataModelList.dataModel>> = Observer {
            dataSet(it!!)
        }
        model.dataModel.observe(this, dataObserver)


        btn_search.setOnClickListener {
            mListdata = interfaceKt.getData()
            mListdata!!.enqueue(object : Callback<List<dataModelList.dataModel>> {
                override fun onFailure(call: Call<List<dataModelList.dataModel>>, t: Throwable) {
                }

                override fun onResponse(call: Call<List<dataModelList.dataModel>>, response: Response<List<dataModelList.dataModel>>) {
                    var mList: List<dataModelList.dataModel>? = response.body()
                    model.setValue(mList!!)
                }
            })
        }
    }

    private fun initRecycler() {
        val recyclerView = findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.my_recycler_view)
        val linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(this)
        recyclerView.layoutManager = linearLayoutManager
        adapter = recyclerAdapter()
        adapter!!.setOnClickListener(this)
        recyclerView.adapter = adapter
    }

    private fun dataSet(mList: List<dataModelList.dataModel>) {
        run loop@{
            var index = 0;
            mList.forEach { item ->
                adapter?.addItem(item)
            }
        }
        adapter!!.notifyDataSetChanged()
    }
}
