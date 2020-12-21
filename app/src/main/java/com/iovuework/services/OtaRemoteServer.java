package com.iovuework.services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;

import com.abup.iovue.data.constant.Errors;
import com.abup.iovue.engine.DataProvider;
import com.abupdate.common.Trace;
import com.abupdate.lib_ota_adapter.AECU;
import com.abupdate.lib_ota_adapter.IAECUCallback;
import com.abupdate.lib_ota_adapter.ICarInfoCallback;
import com.abupdate.lib_ota_adapter.ICarStateCallback;
import com.abupdate.lib_ota_adapter.IInstallCallback;
import com.abupdate.lib_ota_adapter.IOtaRemoteService;
import com.abupdate.lib_ota_adapter.OtaCode;

/**
 * @author fighter_lee
 * @date 2020/7/13
 */
public class OtaRemoteServer extends Service {
    private static final String TAG = "OtaRemoteServer";

    @Override
    public IBinder onBind(Intent intent) {
        return stub;
    }

    IOtaRemoteService.Stub stub = new IOtaRemoteService.Stub() {

        @Override
        public void getEcuInfo(String s, String s1, IAECUCallback iaecuCallback) throws RemoteException {
            String ecuID = DataProvider.boxEcuID(s, s1);
            Trace.d(TAG, "getEcuInfo() start:" + ecuID);
            switch (ecuID) {
                case "0X7B4_0X7B4":
                    AECU aecu = new AECU();
                    aecu.DID = "0X7B4";
                    aecu.SwID = "0X7B4";
                    aecu.SVer = "V1";
                    aecu.Hsn = "0x2D";
                    aecu.HVer = "V1";
                    aecu.partNumber = ("791150x4400");
                    aecu.productDate = ("2018/08/10");
                    aecu.supplierCode = ("huayang");
                    iaecuCallback.onFinished(OtaCode.SUCCESS, aecu);
                    break;
                case "0X7B5_0X7B5":
                    AECU aecu1 = new AECU();
                    aecu1.DID = ("0X7B5");
                    aecu1.SwID = ("0X7B5");
                    aecu1.SVer = ("A7600C_A39_190327_V1.00");
                    aecu1.Hsn = ("0x2D");
                    aecu1.HVer = ("V1");
                    aecu1.partNumber = ("791150x4400");
                    aecu1.productDate = ("2018/08/10");
                    aecu1.supplierCode = ("huayang");
                    iaecuCallback.onFinished(OtaCode.SUCCESS, aecu1);
                    break;
                case "0X7B6_0X7B6":
                    AECU aecu2 = new AECU();
                    aecu2.DID = ("0X7B6");
                    aecu2.SwID = ("0X7B6");
                    aecu2.SVer = ("R8955W1744_IOT3V1212");
                    aecu2.Hsn = ("0x2D");
                    aecu2.HVer = ("V1");
                    aecu2.partNumber = ("791150x4400");
                    aecu2.productDate = ("2018/08/10");
                    aecu2.supplierCode = ("huayang");
                    iaecuCallback.onFinished(OtaCode.SUCCESS, aecu2);
                    break;
                default:
                    break;
            }
        }

        @Override
        public void getCarInfo(ICarInfoCallback iCarInfoCallback) throws RemoteException {

        }

        @Override
        public void getCarState(ICarStateCallback iCarStateCallback) throws RemoteException {

        }

        @Override
        public void installPackage(String s, String s1, String s2, String s3, IInstallCallback iInstallCallback) throws RemoteException {
            for (int i = 0; i <= 100; i += 5) {
                SystemClock.sleep(500);
                iInstallCallback.onInstallProgress(s, s1, i);
                if (i >= 100) {
                    SystemClock.sleep(1000);
                    iInstallCallback.onInstallFinished(s, s1, Errors.NO_ERROR);
                }
            }
        }
    };
}
