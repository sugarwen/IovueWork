package com.iovuework

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.abup.iovue.data.remote.InstallConditionBean
import com.iovuework.dialogs.UpdateOTADialog
import com.iovuework.livedata.MainViewModel
import com.iovuework.utils.Const
import com.iovuework.utils.DataCallback
import com.iovuework.utils.ResUtils
import com.iovuework.utils.Trace
import com.wql.mykotlinstudy.adapters.CarConditionAdapter
import kotlinx.android.synthetic.main.activity_car_condition.*
import kotlinx.android.synthetic.main.activity_install_tips.*
import kotlinx.android.synthetic.main.activity_update_ota.ll_back


/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
class InstallCarConditionActivity : BaseActivity() {
    var list: MutableList<InstallConditionBean> = ArrayList()

    var mainViewModel: MainViewModel? = null
    var conditionAdapter: CarConditionAdapter? = null

    var conditionAllOk: Boolean = false

    //TODO:测试数据，用于测试条件变化
    var condition_unfill_size = 100;

    companion object {
        @JvmStatic
        fun start(context: Context) {
            var intent = Intent(context, InstallCarConditionActivity::class.java)
            context.startActivity(intent)


        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_condition)
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        initView()
        viewListeners()


    }

    override fun onResume() {
        super.onResume()
        installCondition()
    }

    fun initView() {

        mainViewModel!!.installconditionbean.observe(this, Observer {

            list.clear()
            val installCondition1 = it
            var size: Int? = installCondition1.size
            if (size == null) {
                size = 0
            }
            if (size == 0) {
                finish()
                return@Observer
            }


            //TODO:测试数据，用于测试条件变化
            var fillSize = installCondition1.size

            for (installConditionBean in installCondition1) {
                //条件名
                val name = installConditionBean.name
                //需要满足的条件值
                val effectiveValue = installConditionBean.effectiveValue
                var ok: Int = installConditionBean.isOK

                if (condition_unfill_size > 0) {
                    //此条件不满足
                    ok = 0
                    fillSize = 0
                } else {
                    //此条件满足
                    ok = 1
                }
                condition_unfill_size--
                var installConditionBean: InstallConditionBean =
                    InstallConditionBean(effectiveValue, ok, name)

                list.add(installConditionBean)
            }


            if (fillSize == 0) {
                conditionAllOk = false
            } else {
                conditionAllOk = true
            }

            Trace.d("conditionAllOk = $conditionAllOk ")
            if (conditionAllOk) {
                showDialog(Const.ACTION_DIALOG_INSTALL_CAR_CONDITION_SUCCESS)
            } else {
                showDialog(Const.ACTION_DIALOG_INSTALL_CAR_CONDITION_FAIL)
            }
            conditionAdapter?.notifyDataSetChanged()

        })


        conditionAdapter = CarConditionAdapter(list)
        var linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        mRecyclerView?.layoutManager = linearLayoutManager
        mRecyclerView?.adapter = conditionAdapter
    }

    fun installCondition() {
        mainViewModel?.loadInstallCondition()
    }

    fun showDialog(
        actiocString: String
    ) {
        UpdateOTADialog(this,
            actiocString,
            object : DataCallback<String> {
                override fun callback(t: String) {

                    if (t == Const.TYPE_CURR_ACTIVITY_FINISH) {
                        finish()
                    }
                }
            }).show()
    }


    fun viewListeners() {
        ll_back?.setOnClickListener(View.OnClickListener {
            InstallTipsActivity.start(this)
            finish()
        })

        btn_change_car_condition.setOnClickListener(View.OnClickListener {
            condition_unfill_size = ResUtils.getRangeNum(2, list.size)
            mainViewModel?.loadInstallCondition()
        })

        btn_car_condition_success.setOnClickListener(View.OnClickListener {

            condition_unfill_size = 0
            mainViewModel?.loadInstallCondition()
        })

    }


}