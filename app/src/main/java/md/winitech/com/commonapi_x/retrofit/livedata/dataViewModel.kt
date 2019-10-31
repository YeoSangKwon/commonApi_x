/*
 * Create by SangKwon on 2019. 10. 30.
 */

/*
 * Create by SangKwon on 2019. 10. 30.
 */

package md.winitech.com.commonapi_x.retrofit.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import md.winitech.com.commonapi_x.retrofit.model.dataModelList

class dataViewModel : ViewModel() {
    val dataModel: MutableLiveData<List<dataModelList.dataModel>> by lazy {
        MutableLiveData<List<dataModelList.dataModel>>()
    }

    fun getCurrentData() : MutableLiveData<List<dataModelList.dataModel>> {
        return dataModel
    }

    fun postValue(value: List<dataModelList.dataModel>) {
        dataModel.postValue(value)
    }

    fun setValue(value: List<dataModelList.dataModel>) {
        dataModel.value = value
    }

    @MainThread
    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<List<dataModelList.dataModel>>) {
        dataModel.observe(owner, observer)
    }

    @Nullable
    fun getValue(): MutableLiveData<List<dataModelList.dataModel>> {
        return dataModel
    }

    override fun onCleared() {
        super.onCleared()
    }
}