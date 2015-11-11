package com.bbcow.task;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.bson.types.ObjectId;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiniu.http.Response;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;

public class QiniuTask implements Runnable{
	private static Logger logger = LoggerFactory.getLogger(QiniuTask.class); 
	public static Map<ObjectId,String> imgMap = new ConcurrentHashMap<ObjectId,String>();
	private static Auth auth = Auth.create("M4J26-pF7eC3bXW5k2rzqJS8ovbkG5fid8ETKxFd", "JyLum342FkH78cjYwpOklSbhRcS0tH_Opm2Iqudf");

	@Override
	public void run() {
		Iterator<Entry<ObjectId,String>> its = imgMap.entrySet().iterator();
		while(its.hasNext()){
			Entry<ObjectId,String> img = its.next();
			Connection con = Jsoup.connect(img.getValue());
			try {
				org.jsoup.Connection.Response res = con.ignoreContentType(true).timeout(5000).execute();
				byte[] bs = res.bodyAsBytes();
				upload(bs, img.getKey().toString());
				//Files.write(bs, new File(DocLoader.target_path+"img/"+img.getKey().toString()+".jpg"));
			} catch (Exception e) {
				logger.error("video : " + e);
				its.remove();
			}
		}
		
	}
	
	
	
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
		imgMap.put(ObjectId.get(), "");
		imgMap.put(ObjectId.get(), "");
		imgMap.put(ObjectId.get(), "");
		Iterator<Entry<ObjectId,String>> its = imgMap.entrySet().iterator();
		
		while(its.hasNext()){
			Entry<ObjectId,String> img = its.next();
			System.out.println(img.getKey());
			its.remove();
		}
		System.out.println(imgMap.size());
		/*for(Entry<ObjectId,String> img : imgMap.entrySet()){
			System.out.println(img.getKey());
			imgMap.remove(img.getKey());
			Connection con = Jsoup.connect(img.getValue());
			try {
				org.jsoup.Connection.Response res = con.ignoreContentType(true).timeout(5000).execute();
				byte[] bs = res.bodyAsBytes();
				upload(bs, img.getKey().toString());
				//Files.write(bs, new File(DocLoader.target_path+"img/"+img.getKey().toString()+".jpg"));
			} catch (Exception e) {
				logger.error("video : " + e);
			}
		}*/
		/*ByteSource bs = Files.asByteSource(new File("E:\\bbcow\\bbhtml_2\\img\\56144f0356b7e87d356bcf2e.jpg"));
		
		Auth auth = Auth.create("M4J26-pF7eC3bXW5k2rzqJS8ovbkG5fid8ETKxFd", "JyLum342FkH78cjYwpOklSbhRcS0tH_Opm2Iqudf");
		String token = auth.uploadToken("bbcow", "logo.png");
		UploadManager uploadManager = new UploadManager();
		
		try {
			Response res = uploadManager.put(bs.read(), "logo.png", token);
		} catch (Exception e) {
			System.out.println(e);
		}*/
	}

}
