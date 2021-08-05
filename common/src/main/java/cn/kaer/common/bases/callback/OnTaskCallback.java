package cn.kaer.common.bases.callback;

/**
 * @author : yxl
 * Date: 2021/7/16
 */
public interface OnTaskCallback<T> {
    void onResult(T t);

    void onFailed(String err);
}
