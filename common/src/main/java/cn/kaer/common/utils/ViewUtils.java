package cn.kaer.common.utils;

import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;

/**
 * User: yxl
 * Date: 2021/3/22
 */
public class ViewUtils {
    @SuppressLint("ClickableViewAccessibility")
    public static void setPressEffect(View view) {
        view.setOnTouchListener((view1, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                view1.setAlpha(0.7f);
            } else if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                view1.setAlpha(1f);
            }
            return false;
        });
    }
}
