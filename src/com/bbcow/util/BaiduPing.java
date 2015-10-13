package com.bbcow.util;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class BaiduPing {

        public static void site(String url){
                // 定义HttpClient
                HttpClient client = new DefaultHttpClient();
                // 实例化HTTP方法
                HttpPost request = new HttpPost("http://data.zz.baidu.com/urls?site=www.bbcow.com&token=hqjEuHTIHexJiPFt");
                // 创建UrlEncodedFormEntity对象
                StringEntity formEntiry;
				try {
					formEntiry = new StringEntity(url);
	                request.setEntity(formEntiry);
	                // 执行请求
	                client.execute(request);
					
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}

        }

       public static void main(String[] args) {
    	   BaiduPing.site("http://www.bbcow.com/index.html");
       }
}
