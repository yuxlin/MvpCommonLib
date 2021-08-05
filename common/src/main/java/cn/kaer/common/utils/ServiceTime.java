package cn.kaer.common.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import okhttp3.Response;



public class ServiceTime {
    private static String TAG = "ServiceTime";
    private static String PACKAGE_NAME = "com.frame.mvp.lib.utils";
    private static String TIME_DIFFERENT = "time_different";

    public static void syncServiceTime(Response response, Context context)
    {
        List<String> strList = response.headers("Date");
        if ((!strList.isEmpty()) && (strList.size() > 0) &&
                (!TextUtils.isEmpty((CharSequence)strList.get(0))) &&
                (!((String)strList.get(0)).equals("NONE")))
        {
       /*     MVPLog.i(TAG, "response.header(\"Date\"):==" + (String)strList.get(0));*/
            setTimeDifferent(computingServerAndClientTimeDifference((String)strList.get(0)), context);
        }
        List<String> cookie = response.headers("Set-Cookie");
        if ((!cookie.isEmpty()) && (cookie.size() > 0) &&
                (!TextUtils.isEmpty((CharSequence)cookie.get(0))) &&
                (!((String)cookie.get(0)).equals("NONE")))
        {
          /*  for (int i = 0; i < cookie.size(); i++) {
                MVPLog.i(TAG, "response.header(\"Set-Cookie\"):==" + (String)cookie.get(i));
            }*/
            setSetCookie((String)cookie.get(0), context);
        }
    }

    public static String getCurrentServiceTime(Context context)
    {
        Date d = new Date(System.currentTimeMillis() - getTimeDifferent(context).longValue());
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        dateFormat.setTimeZone(new SimpleTimeZone(0, "GMT"));
        return dateFormat.format(d);
    }

    private static Long getTimeDifferent(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PACKAGE_NAME, 0);

        Long distance = Long.valueOf(settings.getLong(TIME_DIFFERENT, 0L));
        return distance;
    }

    private static void setTimeDifferent(Long time_different, Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PACKAGE_NAME, 0);

        settings.edit().putLong(TIME_DIFFERENT, time_different.longValue()).commit();
    }

    private static Long computingServerAndClientTimeDifference(String gmt)
    {
        DateFormat utcFormat = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH);

        utcFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        try
        {
            Date date = utcFormat.parse(gmt);
            Long currentTime = Long.valueOf(System.currentTimeMillis() -
                    System.currentTimeMillis() % 1000L);
            return Long.valueOf(currentTime.longValue() - date.getTime());
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
        return Long.valueOf(0L);
    }

    private static String SET_COOKIE = "set_cookie";

    public static String getSetCookie(Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PACKAGE_NAME, 0);

        String distance = settings.getString(SET_COOKIE, "");
        return distance;
    }

    public static void setSetCookie(String set_cookie, Context context)
    {
        SharedPreferences settings = context.getSharedPreferences(PACKAGE_NAME, 0);

        settings.edit().putString(SET_COOKIE, set_cookie).commit();
    }
}
