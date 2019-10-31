/*
 * Create by SangKwon on 2019. 10. 29.
 */

package md.winitech.com.commonapi_x.room.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Entity(tableName = "user") : 테이블명 선언
 * @PrimaryKey(autoGenerate = true) : 기본키 설정 (자동생성으로 옵션을 줌)
 * */
@Entity(tableName = "UserEntity")
data class UserEntity(
        @PrimaryKey(autoGenerate = true) @ColumnInfo(index = true) var id : Int = 0,
        @ColumnInfo(name = "first_name") val firstName: String?,
        @ColumnInfo(name = "last_name") val lastName: String?
)
