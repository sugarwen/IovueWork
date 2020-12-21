package com.iovuework

import android.app.Application
import com.abup.iovue.OTAConfig
import com.iovuework.notifications.EventNotify
import kotlin.properties.Delegates

/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class App : Application() {
    companion object {

        var context: App by Delegates.notNull()

    }


    override fun onCreate() {
        super.onCreate()
        context = this
        var logLastName = "/log"
        var fileLastName = "/package"

        var builder: OTAConfig.Builder = OTAConfig.build(this)
            .setLogFolder("$cacheDir$logLastName")
            .setDownloadFolder("$filesDir$fileLastName")
        OTAConfig.eventAgent = (EventNotify.mInstance)

        OTAConfig.commit(builder)

    }


}