package cn.kaer.common.utils;

import android.util.Log;

import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ReflectUtils;

import java.io.File;
import java.util.List;

public class Logs {
    private Logs() {
    }

    private static String TAG = "===";
    private static boolean DEBUG = true;
    private static boolean isInit = false;


    public static void init() {
        if (isInit) return;
        isInit = true;
        LogUtils.getConfig()
                .setLogSwitch(DEBUG)
                // .setGlobalTag(TAG)
                .setLogHeadSwitch(false)
                .setBorderSwitch(false)//边框
                .setLog2FileSwitch(true)
                .setSaveDays(7);
        String currentLogFilePath = LogUtils.getCurrentLogFilePath();
        LogUtils.e(TAG, "log file path:" + currentLogFilePath);
    }

    public static LogUtils.Config getConfig() {
        return LogUtils.getConfig();
    }

    public static List<File> getLogFiles() {
        return LogUtils.getLogFiles();
    }

    public static void v(String tag, String msg) {
        init();
        LogUtils.vTag(TAG + tag, msg);
    }

    public static void d(String tag, String msg) {
        init();
        LogUtils.dTag(TAG + tag, msg);
    }

    public static void i(String tag, String msg) {
        init();
        LogUtils.iTag(TAG + tag, msg);
    }

    public static void w(String tag, String msg) {
        init();
        LogUtils.wTag(TAG + tag, msg);
    }

    public static void e(String tag, String msg) {
        init();
        LogUtils.eTag(TAG + tag, msg);
    }


    public static void d(String msg) {
        init();
        LogUtils.d(TAG + msg);
    }

    public static void i(String msg) {
        init();
        LogUtils.i(TAG + msg);
    }

    public static void w(String msg) {
        init();
        LogUtils.w(TAG + msg);
    }

    public static void e(String msg) {
        init();
        LogUtils.e(TAG + msg);

    }

    public static void json(String tag, Object o) {
        init();
        LogUtils.json(LogUtils.E, TAG + tag, o);
    }
}
