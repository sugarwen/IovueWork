package com.wql.mykotlinstudy.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.abup.iovue.data.remote.InstallConditionBean
import com.iovuework.App
import com.iovuework.R

/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/11/19
 */
class CarConditionAdapter(list: MutableList<InstallConditionBean>?) :
    RecyclerView.Adapter<CarConditionAdapter.MyViewHolder?>() {
    private val mData: MutableList<InstallConditionBean>?
    private var mListener: OnItemClickListener? = null
    private var lines = 3

    init {
        mData = list
    }

    fun setItemClickListener(listener: OnItemClickListener?) {
        mListener = listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.item_car_condition, parent, false)
        return MyViewHolder(view)
    }


    override fun onBindViewHolder(
        holder: MyViewHolder,
        position: Int
    ) {
        val appInfo = mData!![position]
        holder.tv_condition_name?.text = appInfo.name
        if (appInfo.isOK == 1) {
            holder.tv_condition_content?.text = ""
            holder.tv_condition_name?.setBackgroundColor(
                ContextCompat.getColor(
                    App.context,
                    R.color.blue
                )
            )
            holder.iv_icon?.setImageResource(R.drawable.ic_condition_success)
        } else {
            holder.tv_condition_content?.text = appInfo.effectiveValue
            holder.tv_condition_name?.setBackgroundColor(
                ContextCompat.getColor(
                    App.context,
                    R.color.red
                )
            )
            holder.iv_icon?.setImageResource(R.drawable.ic_condition_fail)
        }


    }


    override fun getItemCount(): Int {
        return mData?.size ?: 0
    }

    inner class MyViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var tv_condition_name: TextView
        var tv_condition_content: TextView
        var iv_icon: ImageView

        init {
            tv_condition_name = itemView.findViewById(R.id.tv_condition_name)
            tv_condition_content = itemView.findViewById(R.id.tv_condition_content)
            iv_icon = itemView.findViewById(R.id.iv_icon)
        }
    }

    interface OnItemClickListener {
        fun itemClick(position: Int, appInfo: InstallConditionBean?)
    }


}
