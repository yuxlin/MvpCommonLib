package cn.kaer.common.bases;

import android.content.Context;

/**
 * User: yxl
 * Date: 2020/5/4
 */
public interface BaseFunction<T> {

    T init(Context context);

    void unInit();

}
