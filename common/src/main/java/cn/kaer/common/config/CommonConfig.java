package cn.kaer.common.config;

import android.annotation.SuppressLint;
import android.content.Context;

import com.blankj.utilcode.util.LogUtils;


/**
 * User: yxl
 * Date: 2020/12/31
 */
public class CommonConfig {
    private final static String LOG_HEAD = "===yxl";

    @SuppressLint("SdCardPath")
    public static void initLogDefaultConfig(Context context) {
        LogUtils.getConfig().setLogSwitch(true)
                .setBorderSwitch(false)
                .setLogHeadSwitch(false)
                .setLog2FileSwitch(true)
                .setSaveDays(7)
                .setGlobalTag(LOG_HEAD);
        LogUtils.d("log save path" + LogUtils.getCurrentLogFilePath());


    }



}
