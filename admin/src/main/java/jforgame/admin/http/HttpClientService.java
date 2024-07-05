package jforgame.admin.http;

import jforgame.commons.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class HttpClientService {

    @Autowired
    CloseableHttpClient httpClient;

    public String get(String url, Map<String, Object> params) throws IOException {
        return get(url, null, params);
    }

    public String get(String url, Map<String, String> headers, Map<String, Object> params) throws IOException {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);
            List<NameValuePair> urlParam = new ArrayList<>();
            if (!CollectionUtils.isEmpty(params)) {
                params.forEach((k, v) -> {
                    urlParam.add(new BasicNameValuePair(k, v.toString()));
                });
                uriBuilder.setParameters(urlParam);
            }
            // 构建 HttpGet 请求
            HttpGet httpGet = new HttpGet(uriBuilder.build());
            if (!CollectionUtils.isEmpty(headers)) {
                headers.forEach(httpGet::addHeader);
            }
            HttpResponse response = httpClient.execute(httpGet);
            int statusCode = response.getStatusLine().getStatusCode();
            HttpEntity entity = response.getEntity();
            return EntityUtils.toString(entity);

        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    public <T> T post(String url, Map<String, Object> params, Class<T> responseClazz) throws IOException {
        return post(url, null, params, responseClazz);
    }

    public <T> T post(String url, Map<String, String> headers, Map<String, Object> params, Class<T> responseClazz) throws IOException {
        //创建请求对象
        HttpPost    httpPost = new HttpPost(url);
        if (!CollectionUtils.isEmpty(params)) {
            String json = JsonUtil.object2String(params);
            StringEntity entity = new StringEntity(json);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
        }
        if (!CollectionUtils.isEmpty(headers)) {
            headers.forEach(httpPost::addHeader);
        }
        //发送请求，接受响应结果
        CloseableHttpResponse response = httpClient.execute(httpPost);
//        int statusCode = response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        return JsonUtil.string2Object(EntityUtils.toString(entity), responseClazz);
    }


}