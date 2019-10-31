/*
 * Create by SangKwon on 2019. 10. 30.
 */

/*
 * Create by SangKwon on 2019. 9. 27.
 */

package md.winitech.com.commonapi_x.retrofit.model

data class dataModelList<T>(var list:List<dataModel>){
    data class dataModel(var id: Int = 0, var name: String = "NULL", var resId: Int?)
}

