/*
 * Create by SangKwon on 2019. 10. 30.
 */

package md.winitech.com.commonapi_x.retrofit.okhttp3

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class apiClientKt(URLS:String) {

    var URLS = ""
    var retrofit:Retrofit

    init {
        this.URLS = URLS

        var clientKt : OkHttpClient = OkHttpClient.Builder().build()
        var gson = GsonBuilder().setLenient().create()
        this.retrofit = Retrofit.Builder().baseUrl(this.URLS).addConverterFactory(GsonConverterFactory.create(gson)).client(clientKt).build()
    }

    fun getClient():Retrofit{

        return this.retrofit
    }
}