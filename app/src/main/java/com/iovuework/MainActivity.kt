package com.iovuework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : BaseActivity() {


    companion object {
        @JvmStatic
        fun start(context: Context) {
            var intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ll_update.setOnClickListener(View.OnClickListener {
            var content = "更新提示" +
                    "\n1.请点击【检测新版本】发现更新、更稳定、更优异的软件版本，以确保最佳的用车体验。" +
                    "\n\n1.新的升级任务发布后，车辆将自动在后台完成软件下载。" +
                    "\n\n2.下载完成后会在夜间自动安装软件更新，安装完成后将通知您。"

            UpdateOTAActivity.start(this, content, "")
        })

    }


}