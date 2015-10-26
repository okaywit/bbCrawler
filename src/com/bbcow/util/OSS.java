package com.bbcow.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectResult;
import com.google.common.io.Files;

public class OSS {

        public static void site(String url){
        	// 初始化OSSClient
            OSSClient client = new OSSClient("oss-cn-hangzhou.aliyuncs.com","tT9ivLVj2WdK3dlg", "KmfFzPeB5iCUwB2wYZQehbts9Tugcg");

            // 获取指定文件的输入流
            File file = new File("E:\\bbcow\\bbhtml_2\\video\\55f94f42cd86ad2638b4c018.html");
            InputStream content = null;
			try {
				content = new FileInputStream(file);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

            // 创建上传Object的Metadata
            ObjectMetadata meta = new ObjectMetadata();

            // 必须设置ContentLength
            meta.setContentLength(file.length());
            meta.setContentType("text/html");

            // 上传Object.
            PutObjectResult result = client.putObject("video-bbcow", "index.html", content, meta);

            // 打印ETag
            System.out.println(result.getETag());
        	
        	
        	
        }

       public static void main(String[] args) {
    	   OSS.site(null);
			
       }
}
