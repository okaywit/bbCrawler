package com.bbcow.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaiduPing {
	private static Logger logger = LoggerFactory.getLogger(BaiduPing.class); 
	public static void site(String url) {
		CloseableHttpClient http = HttpClients.createDefault();
		HttpPost request = new HttpPost("http://data.zz.baidu.com/urls?site=www.bbcow.com&token=hqjEuHTIHexJiPFt");
		InputStream is = null;
		try {
			StringEntity formEntiry = new StringEntity(url);
			request.setEntity(formEntiry);
			// 执行请求
			HttpResponse res = http.execute(request);
			
			is = res.getEntity().getContent(); 
			byte[] b = new byte[100]; 
			is.read(b);
			//logger.info(new String(b)+ " - " +url);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
				http.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	public static void main(String[] args) {
		BaiduPing.site("http://www.bbcow.com/index.html\nhttp://www.bbcow.com/history.html");
	}
}
