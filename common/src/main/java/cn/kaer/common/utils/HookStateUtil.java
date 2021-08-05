package cn.kaer.common.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

/**
 * User: yxl
 * Date: 2020/12/15
 */
public class HookStateUtil {
    private static final String TAG = "HookStateUtil";
    private static final Uri mUri = Uri.parse("content://com.android.dialer.calllog.DialerStateProvider/");
    private static final String KEY_LOUDSPEAKER_STATE = "loudspeaker_state";

    public HookStateUtil() {
    }

    public static String queryHookState(Context mContext) {
        ContentResolver resolver = mContext.getContentResolver();
        Cursor c = resolver.query(mUri, (String[])null, (String)null, (String[])null, (String)null);
        String ret = "";
        if (c != null && c.moveToFirst()) {
            ret = c.getString(c.getColumnIndex("loudspeaker_state"));
            c.close();
        }

        return ret;
    }

    public static boolean isHookOff(Context mContext) {
        String rst = queryHookState(mContext);
        if ("inner".equals(rst)) {
            return true;
        } else {
            return "outside".equals(rst) ? false : false;
        }
    }
}
