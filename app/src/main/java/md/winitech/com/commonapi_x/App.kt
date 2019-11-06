/*
 * Create by SangKwon on 2019. 11. 6.
 */

package md.winitech.com.commonapi_x

import android.app.Application
import md.winitech.com.commonapi_x.koin.component.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModule))
        }
    }
}