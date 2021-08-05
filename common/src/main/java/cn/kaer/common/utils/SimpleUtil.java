package cn.kaer.common.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Locale;

import static android.content.Context.ACTIVITY_SERVICE;

public class SimpleUtil {
    private static String TAG = "SimpleUtil";

    public static void sync() {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(process.getOutputStream());
            os.writeBytes("sync\n");
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initStatusBarVisible(Context context, boolean statusBarVisible) {
//禁用虚拟按键、statusBar
        Intent invisIntent = new Intent();
//        不显示通知栏
        invisIntent.setAction("com.kaer.action.WHETHER_SHOW_STATUSBAR");
        invisIntent.putExtra("show_status_bar", true);
        invisIntent.setComponent(new ComponentName("com.android.settings", "com.kaer.ethernet.MasterClearReceiver"));
        context.sendBroadcast(invisIntent);
        Intent intent = new Intent("com.kaer.action.WHETHER_SHOW_NAVIGATION");
        //禁用虚拟按键
        if (!statusBarVisible) {
            intent.putExtra("show_navigation_bar", false);
        } else {
            intent.putExtra("show_navigation_bar", true);
        }
        intent.setComponent(new ComponentName("com.android.settings", "com.kaer.ethernet.MasterClearReceiver"));
        context.sendBroadcast(intent);

        Intent intentDisable = new Intent("com.kaer.action.OPEN_PANEL_ENABLED");
        //禁用下滑通知栏
        if (!statusBarVisible) {
            intentDisable.putExtra("open_panel_enabled", false);
        } else {
            intentDisable.putExtra("open_panel_enabled", true);
        }
        intentDisable.setComponent(new ComponentName("com.android.settings", "com.kaer.ethernet.MasterClearReceiver"));
        context.sendBroadcast(intentDisable);

    }
   /* public static void initStatusBarVisible(Context context, boolean statusBarVisible) {
//禁用虚拟按键、statusBar
        Intent invisIntent = new Intent();
//        不显示通知栏
        invisIntent.setAction("com.kaer.action.WHETHER_SHOW_STATUSBAR");
        invisIntent.putExtra("show_status_bar", true);
        context.sendBroadcast(invisIntent);
        Intent intent = new Intent("com.krer.action.WHETHER_SHOW_NAVIGATION");

        //禁用虚拟按键f
        if (!statusBarVisible) {
            intent.putExtra("show_navigation_bar", false);
        } else {
            intent.putExtra("show_navigation_bar", true);
        }
        *//*     intent.setComponent(new ComponentName("com.android.settings", "com.kaer.ethernet.MasterClearReceiver"));*//*
        context.sendBroadcast(intent);

        Intent intentDisable = new Intent("com.kaer.action.OPEN_PANEL_ENABLED");
        //禁用下滑通知栏
        if (!statusBarVisible) {
            intentDisable.putExtra("open_panel_enabled", false);
        } else {
            intentDisable.putExtra("open_panel_enabled", true);
        }
        context.sendBroadcast(intentDisable);

    }*/

    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null)
            Log.d(TAG, "---:" + list.size());
        for (ActivityManager.RunningTaskInfo taskInfo : list) {
            Log.d(TAG, "前台Activity：" + taskInfo.topActivity.getPackageName());
            if (taskInfo.topActivity.getPackageName().equals(className)) { // 说明它已经启动了
                Log.d(TAG, "当前前台：" + taskInfo.topActivity.getPackageName());
                return true;
            }
        }
        return false;
    }

    public static String getSHA1(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(
                    context.getPackageName(), PackageManager.GET_SIGNATURES);
            byte[] cert = info.signatures[0].toByteArray();
            MessageDigest md = MessageDigest.getInstance("SHA1");
            byte[] publicKey = md.digest(cert);
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < publicKey.length; i++) {
                String appendString = Integer.toHexString(0xFF & publicKey[i])
                        .toUpperCase(Locale.US);
                if (appendString.length() == 1)
                    hexString.append("0");
                hexString.append(appendString);
                hexString.append(":");
            }
            String result = hexString.toString();
            return result.substring(0, result.length() - 1);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setTranslucentStatus(Activity activity, boolean isDark, boolean isHideNavigation) {

/*        Intent intent = new Intent("com.kaer.action.WHETHER_SHOW_NAVIGATION");
        intent.putExtra("show_navigation_bar", !isHideNavigation);
        activity.sendBroadcast(intent);*/
        initStatusBarVisible(activity,!isHideNavigation);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
            Window window = activity.getWindow();
            View decorView = window.getDecorView();
            //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
            int HideNavigation = View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE;

            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | (isHideNavigation ? HideNavigation : 0);

            int dark = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    | (isHideNavigation ? HideNavigation : 0);


            //| View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            // | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION


            decorView.setSystemUiVisibility(isDark ? dark : option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
            //导航栏颜色也可以正常设置
            // window.setNavigationBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = activity.getWindow();
            WindowManager.LayoutParams attributes = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            attributes.flags |= flagTranslucentStatus;
            //int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
            //attributes.flags |= flagTranslucentNavigation;
            window.setAttributes(attributes);
        }
    }


}
