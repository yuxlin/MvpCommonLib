package cn.kaer.mvpcommonlib.http;

import java.util.LinkedHashMap;
import java.util.Map;

import cn.kaer.common.api2.IRetrofitConfig;

/**
 * @author : yxl
 * Date: 2021/8/25
 */
public class TestHttpConfig extends IRetrofitConfig {
    @Override
    public String getBaseUrl() {
        return "https://baidu.com/";
    }

    @Override
    public Map<String, String> getDoMainBaseUrl() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ceshi","https://www.baifubao.com/");
        map.put("taobao","http://tcc.taobao.com/cc/");
        return map;
    }

    @Override
    public long getTimeOut() {
        return 5000;
    }

    @Override
    public boolean getDebug() {
        return true;
    }
}
