package cn.kaer.common.api;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.HttpUrl;


public class PersistentCookieStore {
    private static final String LOG_TAG = "PersistentCookieStore";
    private static final String COOKIE_PREFS = "Cookies_Prefs";
    private final Map<String, ConcurrentHashMap<String, Cookie>> cookies;
    private final SharedPreferences cookiePrefs;

    public PersistentCookieStore(Context context)
    {
        this.cookiePrefs = context.getSharedPreferences("Cookies_Prefs", 0);
        this.cookies = new HashMap();


        Map<String, ?> prefsMap = this.cookiePrefs.getAll();
        for (Map.Entry<String, ?> entry : prefsMap.entrySet())
        {
            String[] cookieNames = TextUtils.split((String)entry.getValue(), ",");
            for (String name : cookieNames)
            {
                String encodedCookie = this.cookiePrefs.getString(name, null);
                if (encodedCookie != null)
                {
                    Cookie decodedCookie = decodeCookie(encodedCookie);
                    if (decodedCookie != null)
                    {
                        if (!this.cookies.containsKey(entry.getKey())) {
                            this.cookies.put(entry.getKey(), new ConcurrentHashMap());
                        }
                        ((ConcurrentHashMap)this.cookies.get(entry.getKey())).put(name, decodedCookie);
                    }
                }
            }
        }
    }

    protected String getCookieToken(Cookie cookie)
    {
        return cookie.name() + "@" + cookie.domain();
    }

    public void add(HttpUrl url, Cookie cookie)
    {
        String name = getCookieToken(cookie);
        if (!cookie.persistent())
        {
            if (!this.cookies.containsKey(url.host())) {
                this.cookies.put(url.host(), new ConcurrentHashMap());
            }
            ((ConcurrentHashMap)this.cookies.get(url.host())).put(name, cookie);
        }
        else if (this.cookies.containsKey(url.host()))
        {
            ((ConcurrentHashMap)this.cookies.get(url.host())).remove(name);
        }
        SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.putString(url.host(), TextUtils.join(",", ((ConcurrentHashMap)this.cookies.get(url.host())).keySet()));
        prefsWriter.putString(name, encodeCookie(new SerializableOkHttpCookies(cookie)));
        prefsWriter.apply();
    }

    public List<Cookie> get(HttpUrl url)
    {
        ArrayList<Cookie> ret = new ArrayList();
        if (this.cookies.containsKey(url.host())) {
            ret.addAll(((ConcurrentHashMap)this.cookies.get(url.host())).values());
        }
        return ret;
    }

    public boolean removeAll()
    {
        SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
        prefsWriter.clear();
        prefsWriter.apply();
        this.cookies.clear();
        return true;
    }

    public boolean remove(HttpUrl url, Cookie cookie)
    {
        String name = getCookieToken(cookie);
        if ((this.cookies.containsKey(url.host())) && (((ConcurrentHashMap)this.cookies.get(url.host())).containsKey(name)))
        {
            ((ConcurrentHashMap)this.cookies.get(url.host())).remove(name);

            SharedPreferences.Editor prefsWriter = this.cookiePrefs.edit();
            if (this.cookiePrefs.contains(name)) {
                prefsWriter.remove(name);
            }
            prefsWriter.putString(url.host(), TextUtils.join(",", ((ConcurrentHashMap)this.cookies.get(url.host())).keySet()));
            prefsWriter.apply();

            return true;
        }
        return false;
    }

    public List<Cookie> getCookies()
    {
        ArrayList<Cookie> ret = new ArrayList();
        for (String key : this.cookies.keySet()) {
            ret.addAll(((ConcurrentHashMap)this.cookies.get(key)).values());
        }
        return ret;
    }

    protected String encodeCookie(SerializableOkHttpCookies cookie)
    {
        if (cookie == null) {
            return null;
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        try
        {
            ObjectOutputStream outputStream = new ObjectOutputStream(os);
            outputStream.writeObject(cookie);
        }
        catch (IOException e)
        {
            Log.d("PersistentCookieStore", "IOException in encodeCookie", e);
            return null;
        }
        return byteArrayToHexString(os.toByteArray());
    }

    protected Cookie decodeCookie(String cookieString)
    {
        byte[] bytes = hexStringToByteArray(cookieString);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        Cookie cookie = null;
        try
        {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            cookie = ((SerializableOkHttpCookies)objectInputStream.readObject()).getCookies();
        }
        catch (IOException e)
        {
            Log.d("PersistentCookieStore", "IOException in decodeCookie", e);
        }
        catch (ClassNotFoundException e)
        {
            Log.d("PersistentCookieStore", "ClassNotFoundException in decodeCookie", e);
        }
        return cookie;
    }

    protected String byteArrayToHexString(byte[] bytes)
    {
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte element : bytes)
        {
            int v = element & 0xFF;
            if (v < 16) {
                sb.append('0');
            }
            sb.append(Integer.toHexString(v));
        }
        return sb.toString().toUpperCase(Locale.US);
    }

    protected byte[] hexStringToByteArray(String hexString)
    {
        int len = hexString.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[(i / 2)] = ((byte)((Character.digit(hexString.charAt(i), 16) << 4) + Character.digit(hexString.charAt(i + 1), 16)));
        }
        return data;
    }
}
