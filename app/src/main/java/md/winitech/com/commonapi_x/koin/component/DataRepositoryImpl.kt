/*
 * Create by SangKwon on 2019. 11. 6.
 */

package md.winitech.com.commonapi_x.koin.component

class DataRepositoryImpl : DataRepository {
    var std = "TEST"
    override fun getData() = "DATA"
}