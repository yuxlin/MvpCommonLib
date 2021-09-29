package cn.kaer.common.api2.example;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author : yxl
 * Date: 2021/8/25
 */
public interface IExampleService {

    /**
     * Domain-Name 通过Domain-Name来实现多baseUrl 如果未设置Domain-Name则默认使用全局baseUrl
     * @param id
     * @return
     */
    @Headers({"Domain-Name: xxx"})
    @GET("/v2/book/")
    Observable<ResponseBody> xxxx(@Query("id") int id);


    @Headers({"Accept: application/json"})
    @POST("xx/xx")
    Observable<ResponseBody> postXX(@Query("xx") String xx);


}
