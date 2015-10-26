package com.bbcow.video;

import java.io.IOException;
import java.util.List;

import org.bson.types.ObjectId;
import org.eclipse.jetty.websocket.api.Session;
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
import com.bbcow.util.BaiduPing;
import com.bbcow.util.BusException;
import com.bbcow.util.HtmlTask;

public abstract class AbstractVideoParser {
	protected String HOST;
	private static Logger logger = LoggerFactory.getLogger(AbstractVideoParser.class); 
	protected static VideoDao vd = new VideoDao(MongoDB.db());
	private static ErrorVideoDao evd = new ErrorVideoDao(MongoDB.db());
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
	public static void saveVideoMsg(Video video){
		Key<Video> key = vd.save(video);
		video.setId(new ObjectId(key.getId().toString()));
	}
	public static void dealVideoMsg(Video video){
		Key<Video> key = vd.save(video);
		video.setId(new ObjectId(key.getId().toString()));
		BaiduPing.site("http://www.bbcow.com/video/"+video.getId()+".html");
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
