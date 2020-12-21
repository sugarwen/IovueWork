package com.iovuework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.abup.iovue.OTAAgent
import com.abup.iovue.data.constant.Constant
import com.abup.iovue.data.constant.Errors
import com.abup.iovue.engine.otaStatus.OtaStatus
import com.abup.iovue.interact.callback.InstallCallback
import com.iovuework.dialogs.UpdateOTADialog
import com.iovuework.utils.Const
import com.iovuework.utils.DataCallback
import com.iovuework.utils.ResUtils
import com.iovuework.utils.Trace
import kotlinx.android.synthetic.main.activity_install.*
import kotlinx.android.synthetic.main.activity_install.progressBar
import kotlinx.android.synthetic.main.activity_install.rl_progress
import kotlinx.android.synthetic.main.activity_install.tv_new_version
import kotlinx.android.synthetic.main.activity_install.tv_size
import kotlinx.android.synthetic.main.activity_install.tv_time
import kotlinx.android.synthetic.main.activity_update_ota.*
import kotlinx.android.synthetic.main.activity_update_ota.ll_back


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class InstallActivity : BaseActivity() {

    var disclaimerStr: String = ""

    companion object {
        @JvmStatic
        fun start(context: Context) {
            var intent = Intent(context, InstallActivity::class.java)
            context.startActivity(intent)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install)
        initView()
        viewListeners()

    }

    fun initView() {
        btnTextSet()

        OTAAgent.getInstallMgr().startInstall()
        OTAAgent.getInstallMgr().registerInstallListener(installCallback)
    }


    fun viewListeners() {

        ll_back?.setOnClickListener(View.OnClickListener {
            finish()
        })



        btn_installing.setOnClickListener(View.OnClickListener {

        })


    }


    fun btnTextSet() {
        var otaStatus: OtaStatus = OTAAgent.getQueryMgr().queryOtaStatus()
        if (OtaStatus.UPGRADING == otaStatus) {
            btn_installing?.text = "安装中"
        } else if (OtaStatus.UPGRADE_PAUSE == otaStatus) {
            btn_installing?.text = "安装"
        }

        val queryVersionInfo = OTAAgent.getQueryMgr().queryVersionInfo()
        if (queryVersionInfo != null) {
            if (queryVersionInfo != null) {
                tv_new_version?.text = queryVersionInfo.bigVersion
                disclaimerStr = queryVersionInfo.disclaimer
                var size: Long? = queryVersionInfo!!.ecuVersionInfos?.get(0)?.fileBytes
                if (size == null) {
                    size = 0
                }
                tv_size?.text = ResUtils.formatSize(size!!)
            }

        }


    }


    override fun onDestroy() {
        super.onDestroy()

        OTAAgent.getInstallMgr().unRegisterInstallListener(installCallback)
    }

    private var installCallback = object : InstallCallback {
        override fun onEcuProgress(
            ecuID: String,
            progress: Int,
            isRollback: Boolean
        ) {
            tv_time?.text = "正在安装。。。"
            progressBar?.progress = progress

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
                    UpdateOTADialog(this@InstallActivity,
                        Const.ACTION_DIALOG_INSTALL_SUCCESS,
                        object : DataCallback<String> {
                            override fun callback(t: String) {
                                if (t == Const.TYPE_BACK_HOME) {
                                    MainActivity.start(this@InstallActivity)
                                    finish()
                                }

                            }
                        }).show()

                }
            }
        }

        override fun onPrepare(p0: Int, p1: Any?) {

            Trace.d("installCallback Install  onPrepare i = $p0 , o= $p1 ")
            btn_installing?.text = "安装中"
            tv_time?.text = "正在准备安装环境。。。"
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


}