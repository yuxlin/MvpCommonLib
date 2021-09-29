package cn.kaer.common.api2;

import android.util.Log;

import com.blankj.utilcode.util.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.reactivestreams.Subscription;

import java.io.File;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import cn.kaer.common.api.HttpLogInterceptor;
import cn.kaer.common.api2.interceptor.HttpCacheInterceptor;
import me.jessyan.retrofiturlmanager.RetrofitUrlManager;
import okhttp3.Cache;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author : yxl
 * Date: 2021/8/23
 * <p>
 * https://github.com/JessYanCoding/RetrofitUrlManager
 * 使用 RetrofitUrlManager retrofit多baseUrl支持
 * <p>
 * Step 2
 * // 可在 App 运行时,随时切换 BaseUrl (指定了 Domain-Name header 的接口)
 * RetrofitUrlManager.getInstance().putDomain("douban", "https://api.douban.com");
 * If you want to change the global BaseUrl
 * // 全局 BaseUrl 的优先级低于 Domain-Name header 中单独配置的,其他未配置的接口将受全局 BaseUrl 的影响
 * RetrofitUrlManager.getInstance().setGlobalDomain("your BaseUrl");
 */
public class RetrofitManager {
    private String TAG = getClass().getSimpleName();
    private Retrofit mRetrofit;
    private IRetrofitConfig mIRetrofitConfig;

    private RetrofitManager() {
    }

    private volatile static RetrofitManager instance;

    public synchronized static RetrofitManager get() {
        if (instance == null) {
            instance = new RetrofitManager();
        }
        return instance;
    }

    public RetrofitManager init(IRetrofitConfig retrofitConfig) {
        mIRetrofitConfig = retrofitConfig;
        mRetrofit = getRetrofitBuilder(retrofitConfig).build();

        /*添加doMain baseUrl*/
        Map<String, String> doMainBaseUrlMap = retrofitConfig.getDoMainBaseUrl();
        if (doMainBaseUrlMap != null) {
            for (Map.Entry<String, String> entry : doMainBaseUrlMap.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Log.e(TAG, "add domain baseUrl:" + key + "--" + value);
                putBaseUrlDoMain(key, value);
            }
        }


        return this;
    }

    /**
     * 存放 Domain(BaseUrl) 的映射关系
     * 可在 App 运行时,随时切换 BaseUrl (指定了 Domain-Name header 的接口)
     *
     * @param domainName Domain-Name header
     * @param domainUrl  baseUrl
     */
    public void putBaseUrlDoMain(String domainName, String domainUrl) {
        RetrofitUrlManager.getInstance().putDomain(domainName, domainUrl);
    }

    /**
     * 设置全局 BaseUrl的优先级低于 Domain-Name header 中单独配置的,其他未配置的接口将受全局 BaseUrl 的影响
     *
     * @param baseUrl
     */
    public void setGlobalBaseUrl(String baseUrl) {
        RetrofitUrlManager.getInstance().setGlobalDomain(baseUrl);
    }

    public <T> T createApiService(Class<T> service) {
        if (mRetrofit == null) {
            throw new NullPointerException("retrofit not initialized");
        }
        return mRetrofit.create(service);
    }



    /*================================================================*/

    private static Retrofit.Builder getRetrofitBuilder(IRetrofitConfig retrofitConfig) {

        /**
         * 通过RetrofitUrlManager构建Okhttp
         */
        OkHttpClient okHttpClient = RetrofitUrlManager.getInstance().with(getOkHttpClientBuilder(retrofitConfig)).build();
        return new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(retrofitConfig.getFactory())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .baseUrl(retrofitConfig.getBaseUrl());
    }

    private static OkHttpClient.Builder getOkHttpClientBuilder(IRetrofitConfig apiConfig) {
        File cacheFile = new File(Utils.getApp().getCacheDir(), "cache");
        Cache cache = new Cache(cacheFile, 1024 * 1024 * 100);
        HttpLogInterceptor loggingInterceptor = new HttpLogInterceptor();
        loggingInterceptor.setLevel(apiConfig.getDebug() ? HttpLogInterceptor.Level.BODY : HttpLogInterceptor.Level.NONE);
        return new OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .readTimeout(apiConfig.getTimeOut() <= 0L ? 7676L : apiConfig.getTimeOut(), TimeUnit.MILLISECONDS)
                .connectTimeout(apiConfig.getTimeOut() <= 0L ? 7676L : apiConfig.getTimeOut(), TimeUnit.MILLISECONDS)
                //.addNetworkInterceptor(new HttpCacheInterceptor())
                /*    .sslSocketFactory(SslContextFactory.getSSLSocketFactoryForTwoWay())  // https认证 如果要使用https且为自定义证书 可以去掉这两行注释，并自行配制证书。
                    .hostnameVerifier(new SafeHostnameVerifier())*/
                .cache(cache);
    }

}
