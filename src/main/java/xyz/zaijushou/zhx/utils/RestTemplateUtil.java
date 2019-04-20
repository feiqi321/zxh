package xyz.zaijushou.zhx.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import java.io.IOException;


/**
 * Created by caofanCPU on 2018/8/6.
 */
@Component
public class RestTemplateUtil {
    
    private static Logger logger = LoggerFactory.getLogger(RestTemplateUtil.class);

    
    /**
     * 通过启动类中配置的restTemplate, 可以通过IP:port或者服务名,找到被调用方
     */
    @Resource
    private RestTemplate restTemplate;


    /**
     * POST方式调用，传body对象
     *
     * @param url
     * @return
     */
    public void postBody(String url, String context)  {
        HttpEntity<String> httpEntity = loadHttpEntity(context);
        String reponseBody;
        try {
            reponseBody = restTemplate.postForObject(url, httpEntity, String.class);
            if (reponseBody == null) {
                logger.error("调用微服务接口失败：{}", url + "\n响应为null");
            }
        } catch (RestClientException e) {
            logger.error("调用微服务接口失败：{}", url + "\n" + e.getMessage());
        }

    }

    
    /**
     * 封装请求对象HttpEntity，主要是token、请求参数
     *
     * @param context
     * @return
     */
    public HttpEntity<String> loadHttpEntity(String context) {
        /*HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        //设置返回媒体数据类型
        headers.add("Accept", MediaType.APPLICATION_JSON.toString());*/
        //JSONObject jsonObject = JSONObject.parseObject(context);
        HttpHeaders headers = new HttpHeaders();

        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");

        headers.setContentType(type);

        headers.add("Accept", MediaType.APPLICATION_JSON.toString());
        HttpEntity httpEntity = new HttpEntity(context, headers);
        return httpEntity;
    }


    public String doPostTestTwo(String url,String jsonString) {
        String result = "";

        // 获得Http客户端(可以理解为:你得先有一个浏览器;注意:实际上HttpClient与浏览器是不一样的)
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 创建Post请求
        HttpPost httpPost = new HttpPost(url);

        // 我这里利用阿里的fastjson，将Object转换为json字符串;
        // (需要导入com.alibaba.fastjson.JSON包)

        StringEntity entity = new StringEntity(jsonString, "UTF-8");

        // post请求是将参数放在请求体里面传过去的;这里将entity放入post请求体中
        httpPost.setEntity(entity);

        httpPost.setHeader("Content-Type", "application/json;charset=utf8");

        // 响应模型
        CloseableHttpResponse response = null;
        try {
            // 由客户端执行(发送)Post请求
            response = httpClient.execute(httpPost);
            // 从响应模型中获取响应实体
            org.apache.http.HttpEntity responseEntity = response.getEntity();

            System.out.println("响应状态为:" + response.getStatusLine());
            if (responseEntity != null) {
                result = EntityUtils.toString(responseEntity);
                System.out.println("响应内容长度为:" + responseEntity.getContentLength());
                System.out.println("响应内容为:" + result);

            }

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                // 释放资源
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

}
