package cn.kaer.mvpcommonlib.http;



import io.reactivex.rxjava3.core.Observable;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Query;

/**
 * @author : yxl
 * Date: 2021/8/23
 */
public interface IHttpTest {
    @Headers({"Domain-Name: taobao"})
    @GET("/json/mobile_tel_segment.htm")
    Observable<ResponseBody> requestTest(@Query("tel") String tel);

    @Headers({"Domain-Name: ceshi"})
    @GET("/callback")
    Observable<ResponseBody> requestTest2(@Query("cmd") String cmd,@Query("callback") String callback,@Query("phone") String phone);

}
