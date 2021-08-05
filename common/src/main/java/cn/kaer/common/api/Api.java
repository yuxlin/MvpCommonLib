package cn.kaer.common.api;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.apache.http.conn.ssl.AllowAllHostnameVerifier;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import cn.kaer.common.BuildConfig;
import cn.kaer.common.utils.HttpsUtils;
import cn.kaer.common.utils.ServiceTime;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;


public class Api<T> {
    private static final String TAG = "Api";
    private Retrofit retrofit;
    private T mApiService;
    private IApiConfig mIApiConfig;

    public T getApiService() {
        return this.mApiService;
    }

    public Api(Class<T> service, @NonNull IApiConfig apiConfig) {
        this.mIApiConfig = apiConfig;
        HttpLogInterceptor httpLogInterceptor = new HttpLogInterceptor();
        httpLogInterceptor.setLevel(this.mIApiConfig.getDebug() ? HttpLogInterceptor.Level.BODY : HttpLogInterceptor.Level.NONE);
//        HttpLoggingInterceptor httpLoggingInterceptor = new1 HttpLoggingInterceptor();
//        httpLoggingInterceptor.setLevel(this.mIApiConfig.getDebug() ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
        Interceptor headInterceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain)
                    throws IOException {
                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();
                HttpUrl url = Api.this.mIApiConfig.getHttpUrl(originalHttpUrl);
                Request.Builder builder = original.newBuilder();
                String method = chain.request().method();
                if ((Api.this.mIApiConfig != null) && (Api.this.mIApiConfig.getHeadMap(method) != null)) {
                    Map<String, String> headMap = Api.this.mIApiConfig.getHeadMap(method);
                    if (headMap != null) {
                        for (Map.Entry<String, String> entry : headMap.entrySet()) {
                            builder.addHeader((String) entry.getKey(), (String) entry.getValue());
                        }
                    }
                }
                builder.addHeader("Date", ServiceTime.getCurrentServiceTime(Api.this.mIApiConfig.getContext()));

                builder.url(url);
                return chain.proceed(builder.build());
            }
        };
//        File cacheFile = new1 File(this.mIApiConfig.getContext().getCacheDir(), "cache");
//        Cache cache = new1 Cache(cacheFile, 104857600L);
//        HttpsUtils.SSLParams sslParams = null;
/*        try {
            sslParams = HttpsUtils.getSslSocketFactory(apiConfig.getContext().getAssets().open(BuildConfig.HTTPS_CER_NAME));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(apiConfig.getTimeOut() <= 0L ? 7676L : apiConfig.getTimeOut(), TimeUnit.MILLISECONDS)
                .connectTimeout(apiConfig.getTimeOut() <= 0L ? 7676L : apiConfig.getTimeOut(), TimeUnit.MILLISECONDS)
                .addInterceptor(headInterceptor)
                .addInterceptor(httpLogInterceptor)
                .addNetworkInterceptor(new HttpCacheInterceptor())
                // .sslSocketFactory(sslParams.sSLSocketFactory,sslParams.trustManager)
                //        .hostnameVerifier(new AllowAllHostnameVerifier())
//                .cache(cache)
//                .cookieJar(new1 CookiesManager())
                .build();
        this.retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .addConverterFactory(this.mIApiConfig.getFactory())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())

                .baseUrl(this.mIApiConfig.getBaseUrl())
                .build();
        this.mApiService = this.retrofit.create(service);
    }

    private class HttpCacheInterceptor
            implements Interceptor {
        private HttpCacheInterceptor() {
        }

        @Override
        public Response intercept(Chain chain)
                throws IOException {
            Request request = chain.request();
            if (!Api.this.isNetConnected(Api.this.mIApiConfig.getContext())) {
                request = request.newBuilder().cacheControl(CacheControl.FORCE_CACHE).build();
            }
            Response originalResponse = chain.proceed(request);
            ServiceTime.syncServiceTime(originalResponse, Api.this.mIApiConfig.getContext());
            String errorMsg = "";
            if ((originalResponse != null) && (originalResponse.code() != 200)) {
                ResponseBody responseBody = originalResponse.body();
                if (responseBody != null) {
                    BufferedSource source = responseBody.source();
                    if (source != null) {
                        source.request(9223372036854775807L);
                        Buffer buffer = source.buffer();
                        if (buffer != null) {
                            errorMsg = buffer.clone().readString(Charset.forName("UTF-8"));
                        }
                    }
                }
                if (Api.this.mIApiConfig != null) {
                    Api.this.mIApiConfig.checkCodeStatus(originalResponse.code(), errorMsg);
                }
            }
            if (Api.this.isNetConnected(Api.this.mIApiConfig.getContext())) {
                String cacheControl = request.cacheControl().toString();

                return originalResponse.newBuilder()
                        .header("Cache-Control", cacheControl)
                        .removeHeader("Pragma")
                        .build();
            }

            return originalResponse.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=2419200").removeHeader("Pragma").build();
        }
    }

    private boolean isNetConnected(Context context) {
        @SuppressLint("WrongConstant") ConnectivityManager manager = (ConnectivityManager) context.getSystemService("connectivity");
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if ((info != null) && (info.isConnected())) {
                return true;
            }
        }
        return false;
    }

    public class CookiesManager
            implements CookieJar {
        private final PersistentCookieStore cookieStore = new PersistentCookieStore(Api.this.mIApiConfig.getContext());

        public CookiesManager() {
        }

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if ((cookies != null) && (cookies.size() > 0)) {
                for (Cookie item : cookies) {
                    this.cookieStore.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = this.cookieStore.get(url);
            return cookies;
        }
    }
}
