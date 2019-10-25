package http;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.cookie.BasicClientCookie;
import util.PropertiesUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by WangXW.
 * Date: 2019/10/24
 */
public class RapHttpClient {
    private CloseableHttpClient httpClient;
    private RequestConfig requestConfig;

    public RapHttpClient() {
        httpClient = HttpClientBuilder.create().setDefaultCookieStore(getCooikeStore()).build();
        requestConfig = RequestConfig.custom()
                .setCookieSpec(CookieSpecs.STANDARD)
                // 设置连接超时时间(单位毫秒)
                .setConnectTimeout(5000)
                // 设置请求超时时间(单位毫秒)
                .setConnectionRequestTimeout(5000)
                // socket读写超时时间(单位毫秒)
                .setSocketTimeout(5000)
                // 设置是否允许重定向(默认为true)
                .setRedirectsEnabled(true).build();
    }


    private BasicCookieStore getCooikeStore() {
        BasicCookieStore cookieStore = new BasicCookieStore();
        BasicClientCookie cookie = new BasicClientCookie("koa.sid", PropertiesUtils.get("koa.sid"));
        cookie.setDomain(".rap2api.taobao.org");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        cookie = new BasicClientCookie("koa.sid.sig", PropertiesUtils.get("koa.sid.sig"));
        cookie.setDomain(".rap2api.taobao.org");
        cookie.setPath("/");
        cookieStore.addCookie(cookie);
        return cookieStore;
    }

    public String getInterfaceData(String id) throws Exception {
        HttpGet httpGet = new HttpGet(PropertiesUtils.get("url") + id);
        httpGet.setConfig(requestConfig);
        BufferedReader bufferReader = null;
        StringBuilder responseBuilder = new StringBuilder();
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity httpEntity = response.getEntity();
            bufferReader = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "UTF-8"), 8 * 1024);
            String line = null;
            while ((line = bufferReader.readLine()) != null) {
                responseBuilder.append(line);
            }
            return responseBuilder.toString();
        } catch (Exception e) {
            throw e;
        } finally {
            bufferReader.close();
        }

    }
}
