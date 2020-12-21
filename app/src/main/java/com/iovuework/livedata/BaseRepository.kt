package com.iovuework.livedata

import android.content.Context
import com.abup.iovue.OTAAgent
import com.abup.iovue.data.remote.InstallConditionBean
import com.abup.iovue.engine.otaStatus.OtaStatus
import com.abup.iovue.interact.callback.CheckCallback
import com.abup.iovue.interact.callback.DownloadCallback
import com.iovuework.utils.DataCallback
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.functions.Consumer
import io.reactivex.schedulers.Schedulers


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class BaseRepository {

    constructor(context: Context) {

    }


    fun startCheckVersion(checkCallback: CheckCallback) {
        if (OtaStatus.NULL != OTAAgent.getQueryMgr().queryOtaStatus())
            OTAAgent.getCheckMgr().startCheckVersion(checkCallback)
    }


    fun installCondition(callback: DataCallback<MutableList<InstallConditionBean>>) {
        Observable.create(object : ObservableOnSubscribe<MutableList<InstallConditionBean>?> {
            @Throws(Exception::class)
            override fun subscribe(emitter: ObservableEmitter<MutableList<InstallConditionBean>?>) {
                val installCondition = OTAAgent.getQueryMgr().queryCarState()
                var list: MutableList<InstallConditionBean> = ArrayList()
                if (installCondition != null) {
                    list.addAll(installCondition.installCondition)

                }
                emitter.onNext(list)
            }
        }).subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Consumer<MutableList<InstallConditionBean>?> {
                @Throws(Exception::class)
                override fun accept(homeListInfo: MutableList<InstallConditionBean>?) {
                    var list: MutableList<InstallConditionBean> = ArrayList()
                    if (homeListInfo != null) {
                        callback.callback(homeListInfo)
                    }else{
                        callback.callback(list)
                    }
                }
            })


    }
}