package com.iovuework.dialogs

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AlertDialog
import com.iovuework.InstallActivity
import com.iovuework.InstallTipsActivity
import com.iovuework.R
import com.iovuework.utils.Const
import com.iovuework.utils.DataCallback
import com.iovuework.utils.UpdateType
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.dialog_update_ota.*
import java.util.concurrent.TimeUnit


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class UpdateOTADialog(
    context: Context,
    actiocString: String,
    callBack: DataCallback<String>?
) :
    AlertDialog(context, R.style.dialog) {

    var actiocString: String = ""
    var btnType: Int = UpdateType.btnType_sure

    var callBack: DataCallback<String>? = null

    init {
        this.actiocString = actiocString
        this.callBack = callBack
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_update_ota)
        val window = window
        if (window != null) {
            val layoutParams = window.attributes
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT
            window.attributes = layoutParams
        }
        initView()
    }


    fun initView() {

        if (context == null) {
            dismiss()
        }

        var title = ""
        var content = ""
        var icType = UpdateType.icType_warn
        btnType = UpdateType.btnType_sure
        when (actiocString) {
            (Const.ACTION_DIALOG_NETERROR) -> {
                title = "网络异常"
                content = "请检查网络连接后再试。"
                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_CAR_UNREGISTER) -> {
                title = "车辆未备案"
                content = "请咨询售后解决。"
                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_LATEST_VERSION) -> {
                title = "已是最新版本。"
                content = ""
                icType = UpdateType.icType_tips
                btnType = UpdateType.btnType_sure_countdown
            }
            (Const.ACTION_DIALOG_UPDATE_FINISH) -> {
                title = "恭喜！软件更新已完成，"
                content = "请尽情体验吧。"
                icType = UpdateType.icType_finish
            }
            (Const.ACTION_DIALOG_INSTALL_FAIL) -> {
                title = "安装失败"
                content = "请前往 4S 店重新安装或联系售后处理"
                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_DOWNLOAD_FAIL) -> {
                title = "下载失败"
                content = "请稍后重试"
                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_DOWNLOAD_EXPIRED) -> {
                title = "下载失败"
                content = "任务已失效。请等待服务器后续更新推送"
                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_DOWNLOAD_FAIL_FILL_SD) -> {
                title = "下载失败"
                content = "储存空间不足。请联系售后解决"
                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_DOWNLOAD_VERIFICATION_FAILED) -> {
                title = "下载失败"
                content = "软件包校验失败。是否重新下载？"
                icType = UpdateType.icType_question
            }
            (Const.ACTION_DIALOG_DOWNLOAD_FINISH) -> {
                title = "软件下载已完成"
                content = "是否立即安装？"
                icType = UpdateType.icType_finish
                btnType = UpdateType.btnType_install_scheduleinstallation
            }
            (Const.ACTION_DIALOG_INSTALL_CAR_CONDITION_SUCCESS) -> {
                title = "本次升级预计耗时 45 分钟，升级"
                content = "期间车辆不可用。是否确认升级？"
                icType = UpdateType.icType_question
                btnType = UpdateType.btnType_install_countdown_cancel
            }
            (Const.ACTION_DIALOG_INSTALL_CAR_CONDITION_FAIL) -> {
                title = "安装未完成"
                content = "升级条件不满足，请调整后再试。"
                icType = UpdateType.icType_warn
                btnType = UpdateType.btnType_sure
            }
            (Const.ACTION_DIALOG_INSTALL_ENVIRONMENT_TASK_EXPIRED) -> {
                title = "准备安装环境失败"
                content = "任务已失效，请等待服务器后续推送"
                icType = UpdateType.icType_warn
                btnType = UpdateType.btnType_sure
            }
            (Const.ACTION_DIALOG_INSTALL_PKG_UNTRUSTED) -> {
                title = "准备安装环境失败"
                content = "升级包不可信，请重新下载"
                icType = UpdateType.icType_warn
                btnType = UpdateType.btnType_sure
            }
            (Const.ACTION_DIALOG_INSTALL_ENVIRONMENT) -> {
                title = "准备安装环境失败"
                content = "请退出后再试"
                icType = UpdateType.icType_warn
                btnType = UpdateType.btnType_sure
            }
            (Const.ACTION_DIALOG_INSTALL_SUCCESS) -> {
                title = "安装成功"
                content = "请尽情体验新系统吧！"
                icType = UpdateType.icType_finish
                btnType = UpdateType.btnType_sure
            }


        }



        if (icType == UpdateType.icType_warn) {
            iv_icon.setImageResource(R.drawable.ic_warn)
        } else if (icType == UpdateType.icType_tips) {
            iv_icon.setImageResource(R.drawable.ic_tips)
        } else if (icType == UpdateType.icType_question) {
            iv_icon.setImageResource(R.drawable.ic_question)
        } else if (icType == UpdateType.icType_finish) {
            iv_icon.setImageResource(R.drawable.ic_finish)

        }


        if (btnType == UpdateType.btnType_sure) {
            tv_sure.text = "确定"
            iv_line.visibility = View.GONE
            tv_cancel.visibility = View.GONE
        } else if (btnType == UpdateType.btnType_sure_countdown) {
            tv_sure.text = "确定（5）"
            iv_line.visibility = View.GONE
            tv_cancel.visibility = View.GONE
            dialogShowTimeInterval(5)
        } else if (btnType == UpdateType.btnType_sure_cancel) {

        } else if (btnType == UpdateType.btnType_install_scheduleinstallation) {
            tv_sure.text = "立即安装"
            tv_cancel.text = "预约安装"
        } else if (btnType == UpdateType.btnType_install_countdown_cancel) {
            tv_sure.text = "立即安装（9）"
            tv_cancel.text = "取消"
            dialogShowTimeInterval(9)
        }

        tv_title.text = title
        tv_content.text = content

        tv_sure.setOnClickListener(View.OnClickListener {
            clickSure()
            dismiss()
        })

        tv_cancel.setOnClickListener(View.OnClickListener {

            dismiss()
        })

        setOnDismissListener(DialogInterface.OnDismissListener {
            clickCancel()
            closeDialogCount()

        })

    }

    fun clickSure() {

        //TODO:处理确定按钮事件
        when (actiocString) {
            (Const.ACTION_DIALOG_NETERROR),
            (Const.ACTION_DIALOG_CAR_UNREGISTER),
            (Const.ACTION_DIALOG_LATEST_VERSION),
            (Const.ACTION_DIALOG_DOWNLOAD_FAIL_FILL_SD),
            (Const.ACTION_DIALOG_DOWNLOAD_VERIFICATION_FAILED) -> {
                //dialog直接消失

            }
            (Const.ACTION_DIALOG_UPDATE_FINISH) -> {
//                title = "恭喜！软件更新已完成，"
//                content = "请尽情体验吧。"
//                icType = UpdateType.icType_finish
            }
            (Const.ACTION_DIALOG_INSTALL_FAIL) -> {
//                title = "安装失败"
//                content = "请前往 4S 店重新安装或联系售后处理"
//                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_DOWNLOAD_FAIL) -> {
//                title = "下载失败"
//                content = "请稍后重试"
//                icType = UpdateType.icType_warn
            }
            (Const.ACTION_DIALOG_DOWNLOAD_EXPIRED),
            (Const.ACTION_DIALOG_INSTALL_SUCCESS) -> {
                //TODO: 点击确定退出并回到首页
                callBack?.callback(Const.TYPE_BACK_HOME)
            }
            (Const.ACTION_DIALOG_DOWNLOAD_FINISH) -> {
                InstallTipsActivity.start(context)
            }
            (Const.ACTION_DIALOG_INSTALL_CAR_CONDITION_SUCCESS) -> {
                InstallActivity.start(context)
                //TODO:回调设置activity消失
                callBack?.callback(Const.TYPE_CURR_ACTIVITY_FINISH)
            }
            (Const.ACTION_DIALOG_INSTALL_CAR_CONDITION_FAIL) -> {
                //TODO:回调设置activity消失
                callBack?.callback(Const.TYPE_CURR_ACTIVITY_FINISH)
            }


        }

    }

    fun clickCancel() {
        //TODO:处理取消按钮事件

    }

    private var countdownDisposable: Disposable? = null

    fun closeDialogCount() {
        if (countdownDisposable != null) {
            countdownDisposable!!.dispose()
            countdownDisposable = null
        }
    }

    /**
     * 音乐打开的时间计时器
     */
    fun dialogShowTimeInterval(count: Int) {
        closeDialogCount()
        //intervalRange四个参数分别为：从0开始、到60结束、延时0开始，单位时间（NANOSECONDS,MICROSECONDS,MILLISECONDS,SECONDS,MINUTES,HOURS,DAYS）。
        countdownDisposable = Flowable.intervalRange(0, count.toLong(), 0, 1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(object : Consumer<Long> {
                @Throws(Exception::class)
                override fun accept(aLong: Long) {
//                    callbackData.callback(count.toLong() - aLong)
                    if (btnType == UpdateType.btnType_sure_countdown) {
                        tv_sure?.text = "确定（ ${count.toLong() - aLong} ）"
                    } else if (btnType == UpdateType.btnType_install_countdown_cancel) {
                        tv_sure?.text = "立即安装（ ${count.toLong() - aLong} ）"
                    }
                }
            })
            .doOnComplete(object : Action {
                @Throws(Exception::class)
                override fun run() {
                    //倒计时完毕事件处理
                    closeDialogCount()
                    dismiss()

                }
            })
            .subscribe()
    }


}
