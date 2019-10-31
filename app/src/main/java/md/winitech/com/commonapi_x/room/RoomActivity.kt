/*
 * Create by SangKwon on 2019. 10. 29.
 */

package md.winitech.com.commonapi_x.room

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import kotlinx.android.synthetic.main.activity_room.*
import kotlinx.coroutines.*
import md.winitech.com.commonapi_x.R
import md.winitech.com.commonapi_x.databinding.ActivityRoomBinding
import md.winitech.com.commonapi_x.room.dao.UserDAO
import md.winitech.com.commonapi_x.room.data.UserEntity
import md.winitech.com.commonapi_x.room.database.AppDatabase

class RoomActivity : AppCompatActivity() {
    /**
     * DB 작업을 메인스레드에서 하지 않기위해 적용함
     * 코루틴
     * */
    private val mJob = Job()
    private val mScope = CoroutineScope(Dispatchers.Main + mJob)

    //val 변수에 대한 늦은 초기화
    private val userDao:UserDAO by lazy { AppDatabase.getInstance(this).userDao() }
    lateinit var binding: ActivityRoomBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_room)
        binding.activity = this
    }

    fun onClickButton(v: View){
        when(v.id){
            R.id.btn_insert -> {
                if(binding.edtFirstNM.toString().isEmpty() || binding.edtLastNM.toString().isEmpty()){
                    return@onClickButton
                }
                insertUser()
            }
            R.id.btn_select -> {
                selectUser()
            }
        }
    }

    private fun selectUser() {
        mScope.launch {
            withContext(Dispatchers.IO){
                var user:List<UserEntity> = userDao.getAll()
                run loop@{
                    user.forEachIndexed { index, userEntity ->
                        Log.e("RoomActivity",""+userEntity.id)
                        Log.e("RoomActivity",""+userEntity.firstName.toString())
                        Log.e("RoomActivity",""+userEntity.lastName.toString())
                        deleteUser(user[index])
                    }
                }
            }
        }
    }

    private fun insertUser() {
        mScope.launch {
            withContext(Dispatchers.IO){
                userDao.insertUser(UserEntity(firstName = edt_firstNM.text.toString(),lastName = edt_lastNM.text.toString()))
                edt_firstNM.setText("")
                edt_lastNM.setText("")
            }
        }
    }

    private fun deleteUser(userEntity: UserEntity){
        mScope.launch {
            withContext(Dispatchers.IO){
                userDao.deleteUser(userEntity)
            }
        }
    }
}
