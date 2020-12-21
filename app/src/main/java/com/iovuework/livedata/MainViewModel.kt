package com.iovuework.livedata

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.abup.iovue.data.constant.Errors
import com.abup.iovue.data.remote.InstallConditionBean
import com.abup.iovue.data.remote.VersionInfoBean
import com.abup.iovue.interact.callback.CheckCallback
import com.iovuework.utils.DataCallback
import com.iovuework.utils.Trace


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {

    var baseRepository: BaseRepository? = null
    var versionInfo: MutableLiveData<VersionInfoBean> = MutableLiveData<VersionInfoBean>()
    var installconditionbean: MutableLiveData<MutableList<InstallConditionBean>> =
        MutableLiveData<MutableList<InstallConditionBean>>()

    init {
        baseRepository = BaseRepository(application)
    }


    fun loadCheckVersion(needCheckFailShow: Boolean) {
        baseRepository!!.startCheckVersion(object : CheckCallback {
            override fun onSuccess(versionInfoBean: VersionInfoBean?) {
                Trace.d("onSuccess")
                if (versionInfoBean != null) {
                    versionInfo.postValue(versionInfoBean)
                }
            }

            override fun onFailed(code: Int) {
                Trace.d("onFailed code = $code")

                if (Errors.isNetError(code)) {
                    //网络异常
                } else if (code == Errors.ADM_E_OTA_SERVER_UNREGISTER) {
                    //车辆未注册
                } else {
                    //没有新版本
                }

            }

        })

    }

    fun loadInstallCondition() {
        baseRepository!!.installCondition(object : DataCallback<MutableList<InstallConditionBean>> {
            override fun callback(list: MutableList<InstallConditionBean>) {
                installconditionbean.postValue(list)
            }

        })
    }

}