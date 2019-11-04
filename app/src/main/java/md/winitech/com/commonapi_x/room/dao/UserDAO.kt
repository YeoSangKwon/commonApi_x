/*
 * Create by SangKwon on 2019. 10. 29.
 */

package md.winitech.com.commonapi_x.room.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import md.winitech.com.commonapi_x.room.data.UserEntity

@Dao
interface UserDAO {

    /**
     * @Insert(onConflict = OnConflictStrategy.REPLACE) : 중복 키값이 들어올 경우 처리
     * vararg : 가변인자 (UserEntity 타입의 변수가 몇개가 들어 와도 상관없음) -> ex insertUser(UserEntity(), UserEntity(), UserEntity())
     * */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(vararg users: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateUser(vararg user:UserEntity)

    @Delete
    fun deleteUser(vararg users: UserEntity)


    /**
     * Simple Query는 List 형태의 리턴값을 받으며 변화를 감지 할 수 없습니다.
     * 하지만 Observable Query 는 LiveData 형태의 리턴값을 받으며 데이터가 변화할 경우 변화를 감지하여 데이터를 재갱신합니다
     * */
    //Simple Query
    @Query("SELECT * FROM UserEntity")
    fun getAll() : List<UserEntity>

    @Query("SELECT * FROM UserEntity WHERE first_name = :first_name")
    fun getName(first_name:String) : List<UserEntity>

    //Observable Query
    @Query("SELECT * FROM UserEntity")
    fun getAllObservable(): LiveData<List<UserEntity>>

    @Query("SELECT * FROM UserEntity WHERE first_name = :first_name")
    fun getNameObservable(first_name:String) : LiveData<List<UserEntity>>

}








