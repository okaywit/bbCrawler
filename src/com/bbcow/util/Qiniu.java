package com.bbcow.util;

import java.io.File;

import com.google.common.io.ByteSource;
import com.google.common.io.Files;
import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class Qiniu {
	private static Auth auth = Auth.create("M4J26-pF7eC3bXW5k2rzqJS8ovbkG5fid8ETKxFd", "JyLum342FkH78cjYwpOklSbhRcS0tH_Opm2Iqudf");
	
	public static void upload(byte[] bs,String id){
		String token = auth.uploadToken("bbcow", id+".jpg");
		UploadManager uploadManager = new UploadManager();
		try {
			Response res = uploadManager.put(bs, id+".jpg", token);
			System.out.println(res.bodyString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public static void main(String[] args) {
		
		ByteSource bs = Files.asByteSource(new File("E:\\bbcow\\bbhtml_2\\img\\56144f0356b7e87d356bcf2e.jpg"));
		
		Auth auth = Auth.create("M4J26-pF7eC3bXW5k2rzqJS8ovbkG5fid8ETKxFd", "JyLum342FkH78cjYwpOklSbhRcS0tH_Opm2Iqudf");
		String token = auth.uploadToken("bbcow", "logo.png");
		UploadManager uploadManager = new UploadManager();
		
		try {
			Response res = uploadManager.put(bs.read(), "logo.png", token);
			System.out.println(res.bodyString());
		} catch (Exception e) {
			System.out.println(e);
		}
	}
}
