package cn.kaer.common.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.lang.reflect.Method;

import androidx.core.app.ActivityCompat;

public class MobileDataUtil {
    private final static String TAG = "mobileDataUtil";
    /**
     * 设置手机的移动数据
     */
    public static boolean setMobileData(Context context, boolean pBoolean) {

        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "无权限");
                return false;
            }
            @SuppressLint({"NewApi", "LocalSuppress"}) int subid = SubscriptionManager.from(context).getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId();
            TelephonyManager telephonyService = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            Method setDataEnabled = telephonyService.getClass().getDeclaredMethod("setDataEnabled", int.class, boolean.class);

            if (null != setDataEnabled) {
                setDataEnabled.invoke(telephonyService, subid, pBoolean);
                Log.e(TAG, "setDataEnabled suc");
                return true;
            } else {
                Log.e(TAG, "setnull");
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "setDataEnabled exception");
        }
        return false;
    }

    /**
     * 返回手机移动数据的状态
     *
     * @param pContext
     * @param arg      默认填null
     * @return true 连接 false 未连接
     */
    public static boolean getMobileDataState(Context pContext, Object[] arg) {

        boolean enabled = false;
        try {
            if (ActivityCompat.checkSelfPermission(pContext, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {

                return false;
            }
            int subid = SubscriptionManager.from(pContext).getActiveSubscriptionInfoForSimSlotIndex(0).getSubscriptionId();
            TelephonyManager telephonyService = (TelephonyManager) pContext.getSystemService(Context.TELEPHONY_SERVICE);
            Method getDataEnabled = telephonyService.getClass().getDeclaredMethod("getDataEnabled", int.class);
            if (null != getDataEnabled) {
                enabled = (Boolean) getDataEnabled.invoke(telephonyService, subid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return enabled;
    }

}
