package com.bbcow.video;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.eclipse.jetty.websocket.api.Session;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.bean.ErrorVideo;
import com.bbcow.bean.Video;
import com.bbcow.dao.ErrorVideoDao;
import com.bbcow.dao.VideoDao;
import com.bbcow.db.MongoDB;
import com.bbcow.message.UrlMsg;
import com.bbcow.test.TestImage;
import com.bbcow.util.BaiduPing;
import com.bbcow.util.BusException;
import com.bbcow.util.DocLoader;
import com.bbcow.util.HtmlTask;
import com.bbcow.util.MD5;
import com.google.common.io.Files;

public abstract class AbstractVideoParser {
	protected String HOST;
	private static Logger logger = LoggerFactory.getLogger(AbstractVideoParser.class); 
	protected static VideoDao vd = new VideoDao(MongoDB.db());
	private static ErrorVideoDao evd = new ErrorVideoDao(MongoDB.db());
	protected static List<String> imgs = new ArrayList<String>();
	protected static Map<String,String> imgMap = new HashMap<String, String>();
	public abstract Video parseHtml(UrlMsg vm) throws BusException;
	
	public void toParse(Session session,UrlMsg vm){
		try {
			Video v = parseHtml(vm);
			session.getRemote().sendString(v.getVideo_url());
			logger.info("parse url " + vm.getUrl());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BusException e) {
			logger.error(e.toString());
			ErrorVideo ev = new ErrorVideo(vm.getUrl(),vm.getHost());
			evd.save(ev);
			try {
				session.getRemote().sendString("undefined");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	/***
	 * 照片存储
	 * @param video
	 */
	protected static void dealImg(Video video) {
		String digest = MD5.encode(video.getOriginal_img());
		boolean save_flag = true;
		for(String img : imgs){
			if(img.equals(digest)){
				save_flag = false;
				break;
			}
		}
		if(save_flag){
			Connection con = Jsoup.connect(video.getOriginal_img());
			try {
				Response res = con.ignoreContentType(true).execute();
				byte[] bs = res.bodyAsBytes();
				Files.write(bs, new File(DocLoader.target_path+"img/"+video.getId()+".jpg"));
				imgs.add(digest);
				
			} catch (Exception e) {
				e.printStackTrace();
				logger.error("video : " + e);
			}
		}
				
	}

	public static void saveVideoMsg(Video video){
		Key<Video> key = vd.save(video);
		video.setId(new ObjectId(key.getId().toString()));
	}
	public static void dealVideoMsg(Video video){
		video.setOriginal_img(video.getImg());
		Key<Video> key = vd.save(video);
		video.setId(new ObjectId(key.getId().toString()));
		
		dealImg(video);
		
		BaiduPing.site("/"+video.getId()+".html");
		HtmlTask.addTask(video);
	}
	public static void initHistoryPage(){
		List<Video> vs = vd.find().asList();
		HtmlTask.createHistory(vs);
	}
	public static void initAllPage(){
		List<Video> vs = vd.find().asList();
		System.out.println(vs.size());
		//HtmlTask.createHistory(vs);
		for(Video v : vs){
			HtmlTask.addTask(v);
		}
	}
}
