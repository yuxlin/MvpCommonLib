package cn.kaer.common.widget.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.blankj.utilcode.util.ConvertUtils;

/**
 * @author : yxl
 * Date: 2021/6/26
 */
public class AlertDialogUtils {
    public interface OnDialogResult<T> {
        void onResult(T t);
    }

    /**
     * @param context
     * @param title
     * @param message
     * @param onClickListeners
     * @return
     */
    public static AlertDialog.Builder createMessageDialog(Context context, String title, String message, DialogInterface.OnClickListener... onClickListeners) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message);
        if (onClickListeners != null && onClickListeners.length == 1) {
            builder.setPositiveButton("确定", onClickListeners[0]);
        } else if (onClickListeners != null && onClickListeners.length == 2) {
            builder.setNegativeButton("取消", onClickListeners[0]);
            builder.setPositiveButton("确定", onClickListeners[1]);
        }
        return builder;
    }

    public static AlertDialog createChooseDialog(Context context, String title, CharSequence[] items, DialogInterface.OnClickListener listener) {
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setItems(items, listener)
                .create();
        return alertDialog;
    }

    public static AlertDialog createEditTextDialog(Context context, String title, String hint, OnDialogResult<String> onDialogResultListen) {

        LinearLayout linearLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        EditText editText = new EditText(context);
        layoutParams.setMargins(ConvertUtils.dp2px(16), 0, ConvertUtils.dp2px(16), 0);
        linearLayout.addView(editText, layoutParams);
        AlertDialog alertDialog = new AlertDialog.Builder(context)
                .setTitle(title)
                .setView(linearLayout)
                .setNegativeButton("取消", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                }).setPositiveButton("确定", (dialogInterface, i) -> {
                    dialogInterface.dismiss();
                    if (onDialogResultListen != null) {
                        onDialogResultListen.onResult(editText.getText().toString());
                    }
                })
                .create();
        return alertDialog;
    }


}
