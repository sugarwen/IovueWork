package com.iovuework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.abup.iovue.OTAAgent
import com.abup.iovue.data.constant.Constant
import com.abup.iovue.data.constant.Errors
import com.abup.iovue.data.enums.DownloadResult
import com.abup.iovue.data.remote.ProgressBean
import com.abup.iovue.data.remote.VersionInfoBean
import com.abup.iovue.engine.otaStatus.OtaStatus
import com.abup.iovue.interact.callback.CheckCallback
import com.abup.iovue.interact.callback.DownloadCallback
import com.abup.iovue.interact.callback.InstallCallback
import com.iovuework.dialogs.UpdateOTADialog
import com.iovuework.utils.Const
import com.iovuework.utils.DataCallback
import com.iovuework.utils.ResUtils
import com.iovuework.utils.Trace
import kotlinx.android.synthetic.main.activity_update_ota.*


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class UpdateOTAActivity : BaseActivity() {
    var content = "更新提示" +
            "\n1.请点击【检测新版本】发现更新、更稳定、更优异的软件版本，以确保最佳的用车体验。" +
            "\n\n1.新的升级任务发布后，车辆将自动在后台完成软件下载。" +
            "\n\n2.下载完成后会在夜间自动安装软件更新，安装完成后将通知您。"

    companion object {
        @JvmStatic
        fun start(context: Context, releaseNote: String, type: String) {
            var intent = Intent(context, UpdateOTAActivity::class.java)
            intent.putExtra(Const.KEY_RELEASENOTE, releaseNote)
            intent.putExtra(Const.KEY_TYPE, type)
            context.startActivity(intent)


        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_ota)
        initView()
        viewListeners()

    }

    fun initView() {
        tv_releaseNote.text = content
        btn_download.text = "检测新版本"
        btnTextSet(true, getOtaStatus())
    }

    fun btnTextSet(sendCheck: Boolean, otaStatus: OtaStatus) {
        tv_small_version?.text = ""
        tv_time?.text = ""
        rl_progress?.visibility = View.GONE
        if (OtaStatus.CHECK_NEW_VERSION == otaStatus) {
            rl_progress?.visibility = View.VISIBLE
            btn_download?.text = "下载"
            if (sendCheck) {
                OTAAgent.getCheckMgr().startCheckVersion(checkCallback)
            }

        } else if (OtaStatus.IDLE == otaStatus) {
            tv_small_version?.text = "当前版本：V${ResUtils.pkgVersionCode(this, packageName)}"

            btn_download?.text = "检测新版本"
        } else if (OtaStatus.CHECKING == otaStatus) {
            tv_small_version?.text = "当前版本：V${ResUtils.pkgVersionCode(this, packageName)}"
            btn_download?.text = "检测新中"
        } else if (OtaStatus.DOWNLOADING == otaStatus) {
            rl_progress?.visibility = View.VISIBLE
            btn_download?.text = "下载中"
        } else if (OtaStatus.DOWNLOAD_PAUSE == otaStatus) {
            rl_progress?.visibility = View.VISIBLE
            btn_download?.text = "下载"
        } else if (OtaStatus.DOWNLOAD_SUCCESS == otaStatus) {
            rl_progress?.visibility = View.VISIBLE
            btn_install?.visibility = View.VISIBLE
            btn_reservation?.visibility = View.VISIBLE
            btn_download?.visibility = View.GONE
            progressBar?.progress = 100

        } else if (OtaStatus.UPGRADING == otaStatus) {
            rl_progress?.visibility = View.VISIBLE
            btn_download?.text = "安装中"
        } else if (OtaStatus.UPGRADE_PAUSE == otaStatus) {
            rl_progress?.visibility = View.VISIBLE

            btn_download?.text = "安装"
        } else if (OtaStatus.NULL == otaStatus) {
            tv_small_version?.text = "当前版本：V${ResUtils.pkgVersionCode(this, packageName)}"
            btn_download?.text = "检测新版本"
        }

        if (OtaStatus.NULL != otaStatus &&
            OtaStatus.IDLE != otaStatus &&
            OtaStatus.CHECKING != otaStatus &&
            sendCheck
        ) {
            val queryVersionInfo = OTAAgent.getQueryMgr().queryVersionInfo()
            if (queryVersionInfo != null) {

                tv_releaseNote?.text = queryVersionInfo!!.releaseNote
                tv_new_version?.text = "${queryVersionInfo!!.bigVersion}"
                var size: Long? = queryVersionInfo!!.ecuVersionInfos?.get(0)?.fileBytes
                if (size == null) {
                    size = 0
                }
                var strategyTimeConsuming = queryVersionInfo.strategyTimeConsuming;
                if (otaStatus == OtaStatus.DOWNLOADING) {
                    tv_time?.text = "正在下载 大约剩余时间 $strategyTimeConsuming"
                } else if (otaStatus == OtaStatus.DOWNLOAD_SUCCESS) {
                    tv_time?.text = "下载完成，预计更新耗时 $strategyTimeConsuming"

                }
                tv_size?.text = ResUtils.formatSize(size!!)

            }

        }
    }


    fun viewListeners() {
        ll_back.setOnClickListener(View.OnClickListener {
            finish()
        })
        btn_reservation.setOnClickListener(View.OnClickListener {

            //TODO: 设置预约时间

        })
        btn_install.setOnClickListener(View.OnClickListener {
            InstallTipsActivity.start(this)
        })

        btn_download.setOnClickListener(View.OnClickListener {
            val otaStatus = getOtaStatus()

            if (OtaStatus.CHECK_NEW_VERSION == otaStatus) {
                OTAAgent.getDownloadMgr().startDownload()
                OTAAgent.getDownloadMgr()
                    .registerDownloadListener(downloadCallback)
            } else if (OtaStatus.IDLE == otaStatus) {
                OTAAgent.getCheckMgr().startCheckVersion(checkCallback)
            } else if (OtaStatus.CHECKING == otaStatus) {
            } else if (OtaStatus.DOWNLOADING == otaStatus) {
            } else if (OtaStatus.DOWNLOAD_PAUSE == otaStatus) {
                OTAAgent.getDownloadMgr().startDownload()
                OTAAgent.getDownloadMgr()
                    .registerDownloadListener(downloadCallback)
            } else if (OtaStatus.DOWNLOAD_SUCCESS == otaStatus) {
                OTAAgent.getInstallMgr().startInstall()
                OTAAgent.getInstallMgr().registerInstallListener(installCallback)
            } else if (OtaStatus.UPGRADING == otaStatus) {
            } else if (OtaStatus.UPGRADE_PAUSE == otaStatus) {
                OTAAgent.getInstallMgr().startInstall()
                OTAAgent.getInstallMgr().registerInstallListener(installCallback)
            } else if (OtaStatus.NULL == otaStatus) {
                OTAAgent.getCheckMgr().startCheckVersion(checkCallback)
            }

            btnTextSet(false, getOtaStatus())
        })

    }

    fun getOtaStatus(): OtaStatus {
        val otaStatus = OTAAgent.getQueryMgr().queryOtaStatus()
        Trace.d("otaStatus = $otaStatus")
        return otaStatus
    }

    override fun onDestroy() {
        super.onDestroy()
        OTAAgent.getDownloadMgr().unRegisterDownloadListener(downloadCallback)
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
    }

    fun showDialog(
        actiocString: String
    ) {
        btnTextSet(false, getOtaStatus())
        UpdateOTADialog(
            this,
            actiocString,
            object : DataCallback<String> {
                override fun callback(t: String) {

                }
            }).show()
    }


    private var downloadCallback = object : DownloadCallback {
        override fun onEcuSuccess(ecuID: String?) {
            //ECU下载成功
            Trace.d("downloadCallback 下载成功 onEcuSuccess ecuID = $ecuID")


        }

        override fun onEcuFailed(ecuID: String?, i: Int) {
            //ECU下载失败
            Trace.d("downloadCallback 下载失败 onEcuFailed ecuID = $ecuID , i = $i")

        }

        override fun onFinished(downloadResult: DownloadResult?) {


            Trace.d("downloadCallback onFinished downloadResult = $downloadResult")
            when (downloadResult) {
                DownloadResult.cancel -> {
                }
                DownloadResult.pause -> {
                }
                DownloadResult.fail -> {
                    //下载失败
                    val code = downloadResult.code

                    if (code == Errors.ADM_E_SPACE_NOT_ENOUGH) {
                        //空间不足
                        showDialog(Const.ACTION_DIALOG_DOWNLOAD_FAIL_FILL_SD)

                    } else if (code == Errors.ADM_E_VERIFY_SHA256_FAIL) {
                        //升级包验签失败
                        showDialog(Const.ACTION_DIALOG_DOWNLOAD_VERIFICATION_FAILED)

                    } else if (code == Errors.ADM_E_OTA_SERVER_TASK_STOP) {
                        //后台取消了此次OTA任务
                        showDialog(Const.ACTION_DIALOG_DOWNLOAD_EXPIRED)

                    } else {
                        //下载失败
                        showDialog(Const.ACTION_DIALOG_DOWNLOAD_FAIL)

                    }
                }
                DownloadResult.success -> {
                }
            }
        }


        override fun onAllProgress(
            progress: Int,
            saveByte: Long,
            totalByte: Long,
            progressBean: ProgressBean?
        ) {

            //下载剩余时间，单位 秒
            val totalTime = progressBean?.totalTime

            Trace.d("downloadCallback onAllProgress totalTime = $totalTime progress = $progress,saveByte = $saveByte , totalByte = $totalByte")

            rl_progress?.setVerticalGravity(View.VISIBLE)
            progressBar?.progress = progress
            tv_size?.text = "${ResUtils.formatSize(totalByte)}"
            tv_time?.text = "正在下载，剩余时间${totalTime}"
            if (progress == 100) {

                btnTextSet(false, getOtaStatus())
                showDialog(Const.ACTION_DIALOG_DOWNLOAD_FINISH)

            }
        }

        override fun onEcuProgress(ecuID: String?, i: Int) {

            //ECU的下载进度
            Trace.d("downloadCallback onEcuProgress ecuID = $ecuID, i = $i ")

        }

    }

    private var installCallback = object : InstallCallback {
        override fun onEcuProgress(
            ecuID: String,
            progress: Int,
            isRollback: Boolean
        ) {
            //ECU的安装进度
            Trace.d(
                "installCallback ECU的安装进度 ecuID = $ecuID ,progress = $progress ,isRollback = $isRollback"
            )
        }

        override fun onEcuFailed(
            ecuID: String,
            code: Int,
            isRollback: Boolean
        ) {
            //ECU安装失败
            Trace.d("installCallback ECU安装失败 ecuID = $ecuID ,code = $code ,isRollback = $isRollback")

        }

        override fun onEcuSuccess(ecuID: String, isRollback: Boolean) {
            //ECU安装成功
            Trace.d("installCallback ECU安装成功 ecuID = $ecuID ,isRollback = $isRollback")

        }

        override fun onAllFinished(i: Int) {
            Trace.d("installCallback install finish:$i")
            when (i) {
                Errors.NO_ERROR -> {
                }
                Errors.ADM_E_INSTALL_ROLLBACK_OK -> {
                }
                Errors.ADM_E_VERIFY_SHA256_FAIL -> {
                }
                Errors.ADM_E_OTA_SERVER_TASK_STOP -> {
                }
                Errors.ADM_E_PACKAGE_VERIFY_SIGN_FAIL -> {
                }
                else -> {
                }
            }
        }

        override fun onPrepare(p0: Int, p1: Any?) {

            Trace.d("installCallback Install  onPrepare i = $p0 , o= $p1 ")

            //安装准备阶段
            when (p0) {
                Constant.INSTALL_NOTIFY_TYPE_INSTALL_PREPARE_START -> {
                    //安装校验阶段预计耗时（安装校验所需时间）,单位 S
                    var time: Long = p1 as Long
                    Trace.d("installCallback Install  onPrepare time = $time s")

                }
                Constant.INSTALL_NOTIFY_TYPE_INSTALL_PREPARE_VERIFY_VERSION -> {
                }
                Constant.INSTALL_NOTIFY_TYPE_INSTALL_PREPARE_VERIFY_PACKAGE -> {
                }
                Constant.INSTALL_NOTIFY_TYPE_INSTALL_PREPARE_DECODE_PACKAGE -> {
                }
                Constant.INSTALL_NOTIFY_TYPE_INSTALL_PREPARE_TRANSFORM_PACKAGE -> {
                }
                else -> {
                }
            }

        }
    }

    private var checkCallback = object : CheckCallback {
        override fun onSuccess(infoBean: VersionInfoBean?) {
            Trace.d("checkCallback onSuccess")
            btnTextSet(false, getOtaStatus())
            if (infoBean != null) {
//                //新版本说明
                val releaseNote = infoBean!!.releaseNote
//                //免责声明
                val disclaimer = infoBean!!.disclaimer


                rl_progress?.visibility = View.VISIBLE

                tv_releaseNote?.text = releaseNote
                tv_new_version?.text = "${infoBean!!.bigVersion}"
                var size: Long? = infoBean!!.ecuVersionInfos?.get(0)?.fileBytes
                if (size == null) {
                    size = 0
                }
                tv_size?.text = ResUtils.formatSize(size!!)
            }
        }

        override fun onFailed(code: Int) {
            Trace.d("checkCallback onFailed code = $code")

            if (Errors.isNetError(code)) {
                //网络异常
                showDialog(Const.ACTION_DIALOG_NETERROR)
            } else if (code == Errors.ADM_E_OTA_SERVER_UNREGISTER) {
                //车辆未注册
                showDialog(Const.ACTION_DIALOG_CAR_UNREGISTER)
            } else {
                //没有新版本
                showDialog(Const.ACTION_DIALOG_LATEST_VERSION)
            }

        }

    }


}