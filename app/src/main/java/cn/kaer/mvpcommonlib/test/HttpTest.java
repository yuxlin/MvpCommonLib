package cn.kaer.mvpcommonlib.test;

import android.content.Context;
import android.database.Observable;
import android.util.Log;

import java.io.IOException;

import cn.kaer.common.api.Api;
import cn.kaer.common.api.IApiConfig;
import cn.kaer.common.api2.RetrofitManager;
import cn.kaer.common.rx.RxSchedulers;
import cn.kaer.common.utils.Logs;
import cn.kaer.common.utils.OkHttpUtils;
import cn.kaer.mvpcommonlib.http.IHttpTest;
import cn.kaer.mvpcommonlib.http.TestHttpConfig;
import io.reactivex.rxjava3.functions.Consumer;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * @author : yxl
 * Date: 2021/8/23
 */
public class HttpTest {
    private String TAG = getClass().getSimpleName();
    private Context mContext;

    public void init(Context context) {
        mContext = context;
        RetrofitManager retrofitManager = RetrofitManager.get().init(new TestHttpConfig());
        IHttpTest apiService = retrofitManager.createApiService(IHttpTest.class);
        apiService.requestTest("15588346331")
                .compose(RxSchedulers.io_main())
                .subscribe(responseBody -> {
                    Logs.e(TAG, "result:" + responseBody.string());
                });

     /*   apiService.requestTest2("1059", "phone", "15588346331")
                .compose(RxSchedulers.io_main())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Throwable {
                        Logs.e(TAG, "result:" + responseBody.string());
                    }
                });*/


        /*     IHttpTest apiService = retrofitManager.createApiService(IHttpTest.class);
         *//*     apiService.requestTest("15588346331")
                .compose(RxSchedulers.io_main())
                .subscribe(responseBody -> {
                    Logs.e(TAG, "result:"+responseBody.string());
                });*//*
        retrofitManager.putBaseUrlDoMain("ceshi","https://www.baifubao.com/");
        apiService.requestTest2("1059","phone","15588346331")
        .compose(RxSchedulers.io_main())
        .subscribe(new Consumer<ResponseBody>() {
            @Override
            public void accept(ResponseBody responseBody) throws Throwable {
                Logs.e(TAG, "result:"+responseBody.string());
            }
        });*/


    }
}
