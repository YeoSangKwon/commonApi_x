/*
 * Create by SangKwon on 2019. 10. 29.
 */

package md.winitech.com.commonapi_x.room.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import md.winitech.com.commonapi_x.room.dao.UserDAO
import md.winitech.com.commonapi_x.room.data.UserEntity

@Database(entities = [UserEntity::class], version = 1)
abstract class AppDatabase :RoomDatabase(){
    abstract fun userDao() : UserDAO

    /**
     * DB의 자원은 고비용을 요구하기 때문에 싱글턴 패턴을 이용하여 하나의 instance를 이용하여 사용
     * */
    companion object{
        private val DB_NAME = "room-db"
        private var instance : AppDatabase? = null
        
        fun getInstance(context: Context) : AppDatabase {
            return instance ?: synchronized(this){
                instance ?: buildDatabase(context)
            }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, DB_NAME)
                    .addCallback(object :RoomDatabase.Callback(){
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                        }
                    }).build()
        }
    }
}