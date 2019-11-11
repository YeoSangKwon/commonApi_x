/*
 * Create by SangKwon on 2019. 11. 6.
 */

package md.winitech.com.commonapi_x.koin.component

import org.koin.dsl.module

//참조하는 파라메터는 클래스여야 함
class DataPresenter(private var repo: DataRepository) {
    var std = repo.std
    fun getData() = "${repo.getData()} from $this"
}

//get()을 통해서 koin에서 자동으로 추정하여 데이터를 가져옴
val appModule = module {
    single<DataRepository> { DataRepositoryImpl() }
    factory { DataPresenter(get()) }
}