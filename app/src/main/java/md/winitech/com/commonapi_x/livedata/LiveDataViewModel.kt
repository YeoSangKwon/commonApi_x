/*
 * Create by SangKwon on 2019. 10. 29.
 */

package md.winitech.com.commonapi_x.livedata

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.LifecycleOwner
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import androidx.annotation.NonNull
import androidx.annotation.Nullable


/**
 * method 설명
 * @see getValue() : 현재값을 반환, Background thread에서 호출하면 마지막 set된값을 받는다고 보장할 수 없다.
 * @see hasActiveObservers : Active 상태의 observer가 있으면 true, 아니면 false
 * @see hasObservers : LiveData가 observer를 한개라도 가지고 있다면 true
 * @see observe : Lifecycle에 따라 동작하는 observer를 등록한다. main thread에서 호출하며, 이미 등록된 observer라면  observer 내용이 실행됨
 *  STARTED, RESUMED의 active 상태에서만 observer가 이벤트를 수신할 수 있고 DESTROYED상태로 이동하면 observer가 자동으로 제거됨.
    inActive 상태에서는 데이터를 수신받을수 없으나, 다시 Active상태로 진입시 자동으로 데이터가 수신됨.
    DESTORYED가 되기 전까지 LiveData와 owner, observer는 강한참조를 갖으나 DESTORYED되면 참조가 모두 제거됨.
    등록시 이미 owner가 DESTROY 상태라면 무시된다.
    같은 owner,observer 조합이 등록 요청되면 무시되며, 이미 등록된 observer가 다른 owner와 함께 등록 요청을 받는경우 IllegalArgumentException 발생.
 * @see observeForever : owner 없이 등록하며, 이는 owner가 항상 active 상태인것처럼 동작한다. 따라서 모든 이벤트를 수신할 수 있으며, 자동으로 해제되지 않기때문에 명시적으로 removeObserver()를 불러줘야 한다.
    만약 observer가 다른 owner와 함께 이미 등록된 상태라면 IllegalArgumentException을 발생 시킨다
 * @see removeObserver(@NonNull observer: Observer<String>) : 넘겨받은 observer를 list에서 해제한다.
 * @see removeObservers(@NonNull owner: LifecycleOwner) : 넘겨받은 owner와 함께 등록된 observer를 제거한다
 *
 * 아래는 LiveData<T> 상속시 구현가능
 * @onActive : active observer의 개수가 0 -> 1로 변경될때. 따라서 이때 데이터를 실시간으로 업데이트를 시작해야 한다.
 * @onInactive : active observer의 개수가 1 -> 0로 변경될때. observer가 전부 remove되거나 active 상태의 observer가 없을때
 * @postValue(T value) : background thread에서 호출하는 경우 main thread에 값을 set하라고 post 시킨다. setValue()와 동시에 불리면 postValue 값이 나중에 처리되어 최신값이 될 수 있다.
 * @setValue(T value) : main thread에서 호출하여 값을 set 시킨다. 등록된 active observer들에게 바로 전달된다
 * */

class LiveDataViewModel : ViewModel() {
    val mCurrentName: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun getCurrentName() : MutableLiveData<String> {
        return mCurrentName
    }

    fun postValue(value: String) {
        mCurrentName.postValue(value)
    }

    @Nullable
    fun getValue(): String? {
        return mCurrentName.value
    }

    fun setValue(value: String) {
        mCurrentName.value = value
    }

    @MainThread
    fun observe(@NonNull owner: LifecycleOwner, @NonNull observer: Observer<String>) {
        mCurrentName.observe(owner, observer)
    }
    @MainThread
    fun observeForever(@NonNull observer: Observer<String>) {
        mCurrentName.observeForever(observer)
    }

    @MainThread
    fun removeObserver(@NonNull observer: Observer<String>) {
        mCurrentName.removeObserver(observer)
    }

    @MainThread
    fun removeObservers(@NonNull owner: LifecycleOwner) {
        mCurrentName.removeObservers(owner)
    }

    fun hasObservers(): Boolean {
        return mCurrentName.hasObservers()
    }

    fun hasActiveObservers(): Boolean {
        return mCurrentName.hasActiveObservers()
    }

    override fun onCleared() {
        super.onCleared()
    }
}