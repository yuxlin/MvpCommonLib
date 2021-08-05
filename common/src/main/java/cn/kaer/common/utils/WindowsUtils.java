package cn.kaer.common.utils;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import com.blankj.utilcode.util.BarUtils;

/**
 * User: yxl
 * Date: 2021/1/26
 */
public class WindowsUtils {
    public static void setTranslucentStatus(Activity activity, boolean isDark, boolean isHideNavigation) {

        BarUtils.transparentStatusBar(activity); //沉浸式状态栏

        BarUtils.setStatusBarLightMode(activity, isDark);

        BarUtils.setNavBarVisibility(activity, !isHideNavigation);

        setKaerStatus(activity, true, !isHideNavigation, !isHideNavigation);
    }

    /**
     * @param context
     * @param isShowStatusBar 显示状态栏
     * @param isShowNavigation  显示底部虚拟按钮
     * @param enable_panel  启用状态栏下拉
     */
    public static void setKaerStatus(Context context, boolean isShowStatusBar, boolean isShowNavigation, boolean enable_panel,String customNavigation) {
        /*通知栏*/
        Intent invisIntent = new Intent();
        invisIntent.setAction("com.kaer.action.WHETHER_SHOW_STATUSBAR");
        invisIntent.putExtra("show_status_bar", isShowStatusBar);
        context.sendBroadcast(invisIntent);

        /*虚拟按键*/

        Intent intent = new Intent("com.kaer.action.WHETHER_SHOW_NAVIGATION");
            intent.putExtra("show_navigation_bar", isShowNavigation);
        context.sendBroadcast(intent);

        /*自定义底部按钮 b_h_r */
        Intent intent_customNav = new Intent("com.kaer.action.SHOW_NAVIGATION_BUTTON");
        intent_customNav.putExtra("show_navigation_button", customNavigation);
        context.sendBroadcast(intent_customNav);

        /*下拉导航栏*/
            Intent intentDisable = new Intent("com.kaer.action.OPEN_PANEL_ENABLED");
        intentDisable.putExtra("open_panel_enabled", enable_panel);
        context.sendBroadcast(intentDisable);
    }

    public static void setKaerStatus(Context context, boolean isShowStatusBar, boolean isShowNavigation, boolean enable_panel) {
        setKaerStatus(context,isShowStatusBar,isShowNavigation,enable_panel,"b_h_r");
    }
}
