package cn.kaer.common.api2;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;

import okhttp3.HttpUrl;
import retrofit2.Converter;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : yxl
 * Date: 2021/8/23
 **/

public abstract class IRetrofitConfig {
    /**
     * 全局baseUrl
     *
     * @return
     */
    public abstract String getBaseUrl();

    /**
     * 存放 Domain(BaseUrl) 的映射关系
     * 实现多 baseUrl支持
     *
     * @return
     */
    public abstract Map<String, String> getDoMainBaseUrl();

    /**
     * 请求超时时长
     *
     * @return Millisecond millisecond
     */
    public long getTimeOut() {
        return 7000L;
    }

    /**
     * 官方推荐使用GsonConverter
     *
     * @return
     */
    public Converter.Factory getFactory() {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ").serializeNulls().create();
        return GsonConverterFactory.create(gson);
    }

    /**
     * 是否开启拦截器打印详细日志
     *
     * @return
     */
    public abstract boolean getDebug();
}
