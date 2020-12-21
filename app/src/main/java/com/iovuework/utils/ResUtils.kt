package com.iovuework.utils

import android.content.Context
import android.os.Build
import java.text.DecimalFormat
import java.util.*

/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/21
 */
object ResUtils {


    /**
     * *******************************************************************
     * 获得应用版本Code 为是否升级的判定依据
     *
     * @param context
     * @param pkgName 包名
     * @return
     */
    fun pkgVersionCode(context: Context, pkgName: String?): Int {
        try {
            val info = context.packageManager.getPackageInfo(
                pkgName, 0
            )
            if (info != null) {
                var versionCode: Long = 0
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    versionCode = info.longVersionCode
                } else {
                    versionCode = info.versionCode.toLong()
                }
                return versionCode.toInt()
            }
        } catch (e: Exception) {
        }
        return 0
    }

    /**
     * 换算
     *
     * @param size
     * @return
     */
    @JvmStatic
    fun formatSize(size: Long): String? {
        val df = DecimalFormat("#.00")
        val fileSizeString: String
        fileSizeString = if (size <= 0) {
            "0B"
        } else if (size < 1024) {
            df.format(size.toDouble()) + "B"
        } else if (size < 1048576) {
            df.format(size.toDouble() / 1024) + "KB"
        } else if (size < 1073741824) {
            df.format(size.toDouble() / 1048576) + "MB"
        } else {
            df.format(size.toDouble() / 1073741824) + "GB"
        }
        return fileSizeString
    }


    /**
     * 生成一个0 到 count 之间的随机数
     *
     * @param endNum
     * @return
     */
    fun getNum(endNum: Int): Int {
        if (endNum > 0) {
            val random = Random()
            return random.nextInt(endNum)
        }
        return 0
    }

    /**
     * 返回[min, max]范围内的随机int
     *
     * @param min
     * @return
     */
    fun getRangeNum(min: Int, max: Int): Int {
        val random = Random().nextInt(max - min + 1) + min
        return random
    }


}