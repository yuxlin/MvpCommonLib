package cn.kaer.common.api;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;


public abstract class IApiConfig {
    public abstract Context getContext();

    public abstract String getBaseUrl();

    public abstract long getTimeOut();

    public abstract Map<String, String> getHeadMap(String paramString);

    public HttpUrl getHttpUrl(HttpUrl httpUrl)
    {
        HttpUrl url = httpUrl.newBuilder().build();
        return url;
    }

    public abstract void checkCodeStatus(int paramInt, String paramString);

    public Converter.Factory getFactory()
    {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        return GsonConverterFactory.create(gson);
    }

    public abstract boolean getDebug();
}
