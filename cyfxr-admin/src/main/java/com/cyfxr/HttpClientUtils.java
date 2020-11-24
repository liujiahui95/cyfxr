package com.cyfxr;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;

public class HttpClientUtils {

    /**
     * 发送无参的GET请求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String sendGet(String url) throws IOException {
        //构建get请求
        HttpGet httpGet = new HttpGet(url);
        //根据CloseableHttpClient创建好我们的httpClient
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //执行http请求
        CloseableHttpResponse response = httpClient.execute(httpGet);
        //根据response，获取数据
        HttpEntity entity = response.getEntity();
        //将数据转换为String
        String content = EntityUtils.toString(entity, Charset.forName("utf-8"));
        return content;
    }

    /**
     * 以jsonString形式发送HttpPost的Json请求，String形式返回响应结果
     *
     * @param url
     * @param jsonString
     * @return
     */
    public static String sendPostJsonStr(String url, String jsonString) throws Exception {
        //判断参数是否为空
        if (jsonString == null || jsonString.isEmpty()) {
            //如果为空，则执行无参的请求
            return sendPost(url);
        }
        //初始化结果集
        String resp = "";
        //设置实体编码和格式
        StringEntity entityStr = new StringEntity(jsonString, ContentType.create("text/plain", "UTF-8"));
        //构建请求执行对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //构建post请求
        HttpPost httpPost = new HttpPost(url);
        //设置请求体
        httpPost.setEntity(entityStr);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        if (resp == null || resp.equals("")) {
            return "";
        }
        return resp;
    }

    /**
     * 以String形式发送HttpPost的Json请求，String形式返回响应结果
     *
     * @param url
     * @param jsonString
     * @return
     */
    public static String sendPostJson(String url, String jsonString) throws Exception {
        //判断参数是否为空
        if (jsonString == null || jsonString.isEmpty()) {
            //如果为空，则执行无参的请求
            return sendPost(url);
        }
        //初始化结果集
        String resp = "";
        //设置实体编码和格式
        StringEntity entityStr = new StringEntity(jsonString, ContentType.create("application/json", "UTF-8"));
        //构建请求执行对象
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //构建post请求
        HttpPost httpPost = new HttpPost(url);
        //设置请求体
        httpPost.setEntity(entityStr);
        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            resp = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
        } finally {
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                }
            }
        }
        if (resp == null || resp.equals("")) {
            return "";
        }
        return resp;
    }

    /**
     * 发送不带参数的HttpPost请求
     *
     * @param url
     * @return
     */
    public static String sendPost(String url) throws Exception {
        // 1.获得一个httpclient对象
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 2.生成一个post请求
        HttpPost httppost = new HttpPost(url);
        CloseableHttpResponse response = null;
        // 3.执行get请求并返回结果
        response = httpclient.execute(httppost);
        // 4.处理结果，这里将结果返回为字符串
        HttpEntity entity = response.getEntity();
        String result = null;
        result = EntityUtils.toString(entity);
        return result;
    }
}
