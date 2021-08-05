package cn.kaer.common.utils;

import android.util.Log;


import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author : yxl
 * Date: 2021/7/14
 */
public class OkHttpUtils {

    private static OkHttpClient okHttpClient;

    private static synchronized void init() {
        if (okHttpClient != null) {
            return;
        }

        okHttpClient = new OkHttpClient.Builder()/*.addInterceptor(new RequestLoggerInterceptor())
                .addInterceptor(new ResponseLoggerInterceptor())*/
                .build();

    }


    public static void httpGet(String url, Callback callback) {
        init();
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static void httpPost(String urlString, FormBody formBody, Callback callback) {
        init();
        Request request = new Request.Builder().url(urlString).method("POST", formBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public static Response httpGetSync(String url) throws IOException {
        init();
        Request request = new Request.Builder().url(url).build();
        return okHttpClient.newCall(request).execute();
    }

    public static Response httpPostSync(String url, RequestBody formBody) throws IOException {
        init();
        Request request = new Request.Builder().url(url).method("POST", formBody).build();
        return okHttpClient.newCall(request).execute();
    }

    /**
     * 接口用于回调数据
     */
    public interface OnCallback<T> {
        void invoke(T t);
    }

    /**
     * 请求拦截器
     */
    static class RequestLoggerInterceptor implements Interceptor {

        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();

            Log.e(this.getClass().getSimpleName(), "url    =  : " + request.url());
            Log.e(this.getClass().getSimpleName(), "method =  : " + request.method());
            Log.e(this.getClass().getSimpleName(), "headers=  : " + request.headers());
            Log.e(this.getClass().getSimpleName(), "body   =  : " + request.body());

            return chain.proceed(request);
        }
    }

    /**
     * 响应拦截器
     */
    static class ResponseLoggerInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = chain.proceed(chain.request());

            Log.e(this.getClass().getSimpleName(), "code    =  : " + response.code());
            Log.e(this.getClass().getSimpleName(), "message =  : " + response.message());
            Log.e(this.getClass().getSimpleName(), "protocol=  : " + response.protocol());

            if (response.body() != null && response.body().contentType() != null) {
                MediaType mediaType = response.body().contentType();
                String string = response.body().string();
                Log.e(this.getClass().getSimpleName(), "mediaType=  :  " + mediaType.toString());
                Log.e(this.getClass().getSimpleName(), "string   =  : " + string);
                ResponseBody responseBody = ResponseBody.create(mediaType, string);
                return response.newBuilder().body(responseBody).build();
            } else {
                return response;
            }
        }
    }

}
