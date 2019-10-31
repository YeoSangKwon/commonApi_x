/*
 * Create by SangKwon on 2019. 10. 30.
 */


package md.winitech.com.commonapi_x.retrofit.model


data class ktModel(var list:List<data>){
    data class data(var id : Int, var name : String, var age :Int)
}

