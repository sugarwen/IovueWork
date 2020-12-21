package com.iovuework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.iovuework.utils.Const
import kotlinx.android.synthetic.main.activity_disclaimer.*
import kotlinx.android.synthetic.main.activity_update_ota.ll_back


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class DisclaimerActivity : BaseActivity() {

    companion object {
        @JvmStatic
        fun start(context: Context, disclaimer: String) {
            var intent = Intent(context, DisclaimerActivity::class.java)
            intent.putExtra(Const.KEY_DISCLAIMER, disclaimer)
            context.startActivity(intent)


        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_disclaimer)
        initView()
        viewListeners()

    }

    fun initView() {
        if (intent != null && intent.hasExtra(Const.KEY_DISCLAIMER)) {
            var disclaimer = intent.getStringExtra(Const.KEY_DISCLAIMER)
            tv_disclaimer.text = disclaimer

        }

    }


    fun viewListeners() {
        ll_back?.setOnClickListener(View.OnClickListener {
            finish()
        })

    }


}