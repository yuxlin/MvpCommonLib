package cn.kaer.common.bases.callback;

/**
 * User: yxl
 * Date: 2021/3/12
 */
public interface OnConsumer<T> {
    void accept(T t);
}
