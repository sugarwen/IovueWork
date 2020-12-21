package com.iovuework.utils

/**
 *@author : wql
 *@e_mail : wenqiaoling@adups.com
 *@description :
 *@date : 2020/12/18
 */
object Const {

    /////////////////////////dialog back/////////////

    /**
     * 关闭当前activity
     */
    @JvmField
    val TYPE_CURR_ACTIVITY_FINISH = "curr_activity_finish";
    @JvmField
    val TYPE_BACK_HOME = "back_Home";

    ///////////////////////////action/////////////////////////
    @JvmField
    val ACTION_CHECK_NEW_VERSION = "CHECK_NEW_VERSION"

    /**
     * 网络异常
     */
    @JvmField
    val ACTION_DIALOG_NETERROR = "dialog_neterror"

    /**
     * 车辆未备案
     */
    @JvmField
    val ACTION_DIALOG_CAR_UNREGISTER = "dialog_car_unregister"

    /**
     * 已是最新版本
     */
    @JvmField
    val ACTION_DIALOG_LATEST_VERSION = "dialog_latest_version"

    /**
     *
     */
    @JvmField
    val ACTION_DIALOG_UPDATE_FINISH = "dialog_update_finish"

    @JvmField
    val ACTION_DIALOG_INSTALL_FAIL = "dialog_install_fail"

    /**
     *升级包，汽车条件满足，可升级
     */
    @JvmField
    val ACTION_DIALOG_INSTALL_CAR_CONDITION_SUCCESS = "dialog_install_car_condition_success"

    /**
     *升级包，汽车条件不满足
     */
    @JvmField
    val ACTION_DIALOG_INSTALL_CAR_CONDITION_FAIL = "dialog_install_car_condition_fail"

    /**
     *升级包，准备安装环境失败，任务已失效，请等待服务器后续推送
     */
    @JvmField
    val ACTION_DIALOG_INSTALL_ENVIRONMENT_TASK_EXPIRED = "dialog_install_environment_task_expired"

    /**
     *升级包，准备安装环境失败，升级包不可信，请重新下载
     */
    @JvmField
    val ACTION_DIALOG_INSTALL_PKG_UNTRUSTED = "dialog_install_pkg_untrusted"

    /**
     *升级包，准备安装环境失败，请退出后再试
     */
    @JvmField
    val ACTION_DIALOG_INSTALL_ENVIRONMENT = "dialog_install_environment"

    /**
     *升级包，安装成功
     */
    @JvmField
    val ACTION_DIALOG_INSTALL_SUCCESS = "dialog_install_success"

    /**
     * 升级包下载 失败
     */
    @JvmField
    val ACTION_DIALOG_DOWNLOAD_FAIL = "dialog_download_fail"

    /**
     * 升级包下载 储存空间不足，导致失败
     */
    @JvmField
    val ACTION_DIALOG_DOWNLOAD_FAIL_FILL_SD = "dialog_download_fail_fill_sd"

    /**
     * 升级包下载 升级包签名验证失败
     */
    @JvmField
    val ACTION_DIALOG_DOWNLOAD_VERIFICATION_FAILED = "dialog_download_verification_failed"


    /**
     * 升级包下载  完成
     */
    @JvmField
    val ACTION_DIALOG_DOWNLOAD_FINISH = "dialog_download_finish"

    /**
     * 升级包下载  失败 任务已失效
     */
    @JvmField
    val ACTION_DIALOG_DOWNLOAD_EXPIRED = "dialog_download_expired"


    /**
     * 获取到新版本信息
     */
    @JvmField
    val ACTION_NEW_VERSION = "NEW_VERSION"

    /**
     * 获取到新版本信息
     */
    @JvmField
    val ACTION_DOWNLOAD_PROGRESS = "download_progress"


    //////////////////////key//////////////////////////////////////
    @JvmField
    val KEY_RELEASENOTE = "releaseNote"

    @JvmField
    val KEY_TYPE = "type"

    @JvmField
    val KEY_DISCLAIMER = "disclaimer"
}