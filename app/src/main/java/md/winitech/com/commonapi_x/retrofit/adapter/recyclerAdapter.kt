/*
 * Create by SangKwon on 2019. 10. 30.
 */

package md.winitech.com.commonapi_x.retrofit.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import md.winitech.com.commonapi_x.R
import md.winitech.com.commonapi_x.retrofit.model.dataModelList
import md.winitech.com.commonapi_x.retrofit.model.ktModel
import java.util.ArrayList

class recyclerAdapter  : RecyclerView.Adapter<recyclerAdapter.ViewHolder>() {
    private val listData = ArrayList<dataModelList.dataModel>()

    /**
     * 클릭이벤트를 다른곳에서 사용하기 위해 인터페이스 생성
     */
    private var mListener: ViewClickListener? = null

    interface ViewClickListener {
        // 아이템 전체 부분 클릭
        fun onItemClicked(position: Int)

        // 아이템 롱클릭
        fun onItemLongClicked(position: Int)
    }

    /**
     * 인터페이스가 구현되있는곳의 리스너와 연결
     */
    fun setOnClickListener(listener: ViewClickListener) {
        mListener = listener
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val view = LayoutInflater.from(p0.getContext()).inflate(R.layout.item, p0, false)
        return ViewHolder(view)
    }


    override fun getItemCount(): Int {
        return listData.size
    }


    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.onBind(listData[p1])

        if (null != mListener) {
            p0.itemView.setOnClickListener({
                mListener!!.onItemClicked(p1)
            })
        }
    }

    fun addItem(data: dataModelList.dataModel) {
        listData.add(data)
    }

    fun removeItem(data: Collection<dataModelList.dataModel>) {
        listData.removeAll(data)
    }

    /**
     * 리사이클러뷰에 등록하는 아이템 클래스
     */
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var textView1: TextView
        var textView2: TextView
        var imageView: ImageView

        init {
            textView1 = itemView.findViewById(R.id.textView1)
            textView2 = itemView.findViewById(R.id.textView2)
            imageView = itemView.findViewById(R.id.imageView)
        }

        fun onBind(data: dataModelList.dataModel) {
            textView1.text = data.id.toString()
            textView2.text = data.name
            data.resId?.let { imageView.setImageResource(it) }
        }

    }
}