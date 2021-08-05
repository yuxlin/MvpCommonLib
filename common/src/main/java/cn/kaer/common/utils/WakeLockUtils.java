package cn.kaer.common.utils;

import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;
import android.os.PowerManager;

import static android.content.Context.KEYGUARD_SERVICE;
import static android.os.PowerManager.FULL_WAKE_LOCK;
/**
 * User: yxl
 * Date: 2021/1/26
 */
public class WakeLockUtils {

    private static PowerManager mPm;
    private static PowerManager.WakeLock mWakeLock;

    /**
     * 键盘解锁
     *
     * @param context
     */
    public static void disableKeyguard(Context context) {
        // 屏幕解锁
        KeyguardManager keyguardManager = (KeyguardManager) context.getSystemService(KEYGUARD_SERVICE);
        if (keyguardManager.isKeyguardLocked()) {
            KeyguardManager.KeyguardLock
                    keyguardLock = keyguardManager.newKeyguardLock("dialviewUnlockTag");
            keyguardLock.disableKeyguard();
        }
    }

    /**
     * 调用了此方法须和releaseLock()；配合使用
     *
     * @param context
     * @param timeout 背光超时时间 推荐超时时间十分钟单位毫秒
     */
    public static void keepScreenOn(Context context, long timeout) {
        mWakeLock = getPowerManager(context).newWakeLock(FULL_WAKE_LOCK, context.getPackageName());
        mWakeLock.acquire(timeout);
    }

    public static void keepScreenOn(Context context) {
        keepScreenOn(context, Long.MAX_VALUE);
    }

    //关闭屏幕常亮
    public static void releaseLock() {
        if (mWakeLock != null && mWakeLock.isHeld())
            mWakeLock.release();
    }

    //点亮屏幕一次
    @SuppressLint("WakelockTimeout")
    public static void wakeupScreen(Context context) {
        try {
            PowerManager.WakeLock wakeLock = getPowerManager(context).newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK, context.getPackageName());
            wakeLock.acquire();
            wakeLock.release();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 判断手机屏幕是否锁定
     *
     * @param c
     * @return
     */
    public static boolean isScreenOn(Context c) {
        return getPowerManager(c).isScreenOn();
    }

    public static PowerManager getPowerManager(Context context) {
        if (mPm == null) {
            mPm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        }
        return mPm;
    }
}
