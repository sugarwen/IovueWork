package com.iovuework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.CompoundButton
import com.abup.iovue.OTAAgent
import com.iovuework.utils.ResUtils
import com.iovuework.utils.Trace
import kotlinx.android.synthetic.main.activity_install_tips.*
import kotlinx.android.synthetic.main.activity_update_ota.ll_back


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class InstallTipsActivity : BaseActivity() {

    var disclaimerStr: String = ""

    companion object {
        @JvmStatic
        fun start(context: Context) {
            var intent = Intent(context, InstallTipsActivity::class.java)
            context.startActivity(intent)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_install_tips)
        initView()
        viewListeners()

    }

    fun initView() {
        val versionInfo = OTAAgent.getQueryMgr().queryVersionInfo()

        if (versionInfo != null) {
            tv_new_version?.text = versionInfo.bigVersion
            tv_releaseNote?.text = versionInfo.upgradeNote
            disclaimerStr = versionInfo.disclaimer
            var size: Long? = versionInfo!!.ecuVersionInfos?.get(0)?.fileBytes
            if (size == null) {
                size = 0
            }
            tv_size?.text = ResUtils.formatSize(size!!)
        }

    }


    fun viewListeners() {

        ll_back?.setOnClickListener(View.OnClickListener {
            finish()
        })

        tv_disclaimer_link.setOnClickListener(View.OnClickListener {
            DisclaimerActivity.start(this, disclaimerStr)
        })
        checkbox_choose.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {

            override fun onCheckedChanged(p0: CompoundButton?, p1: Boolean) {
                Trace.d("checkbox_choose p1 = $p1")
            }
        })

        btn_install_sure.setOnClickListener(View.OnClickListener {
            InstallCarConditionActivity.start(this)
            finish()
        })
        btn_install_cancel.setOnClickListener(View.OnClickListener {
            finish()
        })

    }


}