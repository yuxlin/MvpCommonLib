package cn.kaer.common.utils;

import java.lang.reflect.Method;

/**
 * User: yxl
 * Date: 2021/1/8
 */
public class SysProp {
    private static Method sysPropGet;
    private static Method sysPropSet;

    private SysProp() {
    }

    static {
        try {
            Class<?> S = Class.forName("android.os.SystemProperties");
            Method M[] = S.getMethods();
            for (Method m : M) {
                String n = m.getName();
                if (n.equals("get")) {
                    sysPropGet = m;
                } else if (n.equals("set")) {
                    sysPropSet = m;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String get(String name, String default_value) {
        try {
            return (String) sysPropGet.invoke(null, name, default_value);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return default_value;
    }

    public static void set(String name, String value) {
        try {
            sysPropSet.invoke(null, name, value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
