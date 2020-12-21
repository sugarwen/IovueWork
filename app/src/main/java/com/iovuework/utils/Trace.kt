package com.iovuework.utils

import android.util.Log

/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
object Trace {
    private val TAG = "my_log"

    @JvmStatic
    fun d(message: String) {
        Log.d(TAG, message)

    }

    @JvmStatic
    fun e(message: String) {
        Log.e(TAG, message)

    }
}