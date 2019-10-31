/*
 * Create by SangKwon on 2019. 10. 30.
 */


package md.winitech.com.commonapi_x.retrofit.inf

import md.winitech.com.commonapi_x.retrofit.model.dataModelList
import retrofit2.Call
import retrofit2.http.GET

interface retroKtinterface {
    //DB 데이터 조회
    @GET("dbconn")
    fun getData(): Call<List<dataModelList.dataModel>>
}