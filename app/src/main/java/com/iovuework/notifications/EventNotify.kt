package com.iovuework.notifications

import android.content.Intent
import com.abup.iovue.IEventAgent
import com.abup.iovue.engine.otaStatus.OtaStatus
import com.iovuework.App
import com.iovuework.InstallTipsActivity
import com.iovuework.MainActivity
import com.iovuework.UpdateOTAActivity
import com.iovuework.utils.Const
import com.iovuework.utils.Trace

/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/16
 */
public class EventNotify : IEventAgent {

    companion object {
        val mInstance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { EventNotify() }

    }


    override fun installActionResumeEventNotify() {
//        TODO("安装页面恢复，需要继续显示安装页面")
        ////使用场景：安装中重启，会收到此回调，请拉起安装页面继续显示
        Trace.d("安装页面恢复，需要继续显示安装页面 installActionResumeEventNotify()")
        var intent: Intent = Intent(App.context, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        App.context.startActivity(intent)
    }

    override fun downloadActionResumeEventNotify() {
//        TODO("Not yet implemented")
    }

    override fun appointUpgradeArrived() {
//        TODO("预约升级时间到达")
        Trace.d("预约升级时间到达 appointUpgradeArrived()")

    }

    override fun newVersionEventNotify(otaStatus: OtaStatus) {
//        TODO("收到新版本通知，请根据状态来提示")
       Trace.d("收到新版本通知，请根据状态来提示 newVersionEventNotify() otaStatus = ${otaStatus.name}")

        if (OtaStatus.CHECK_NEW_VERSION == otaStatus) {

            var intent = Intent(App.context, UpdateOTAActivity::class.java)
            intent.putExtra(Const.KEY_TYPE, "")
            NotifyUtils.sendNoti(
                App.context,
                "发现新版本",
                "请进入OTA更新",
                NotifyUtils.getClickPendingIntent(App.context, intent),
                11
            )
        }else if (OtaStatus.DOWNLOAD_SUCCESS==otaStatus){

            var intent = Intent(App.context, InstallTipsActivity::class.java)
            intent.putExtra(Const.KEY_TYPE, "")
            NotifyUtils.sendNoti(
                App.context,
                "软件已下载完成",
                "请即刻升级吧",
                NotifyUtils.getClickPendingIntent(App.context, intent),
                11
            )
        }

    }

}