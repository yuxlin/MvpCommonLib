package cn.kaer.common.utils;

import java.lang.reflect.ParameterizedType;

/**
 * Created by wanghuixiang on 2018/9/19.
 */

public class TUtil {
    public TUtil() {
    }

    @SuppressWarnings("unchecked")
    public static <T> T getT(Object o, int i) {
        try {
            return ((Class<T>) ((ParameterizedType) (o.getClass()
                    .getGenericSuperclass())).getActualTypeArguments()[i])
                    .newInstance();
        } catch (InstantiationException e) {
            // e.printStackTrace();
        } catch (IllegalAccessException e) {
            //e.printStackTrace();
        } catch (ClassCastException e) {
            // e.printStackTrace();
        }
        return null;
    }
}
