package com.iovuework.utils

/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/21
 */
open interface DataCallback<T> {
    fun callback(t: T)
}
