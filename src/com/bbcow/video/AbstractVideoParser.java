package com.bbcow.video;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.bson.types.ObjectId;
import org.eclipse.jetty.websocket.api.Session;
import org.mongodb.morphia.Key;

import com.bbcow.bean.ErrorVideo;
import com.bbcow.bean.Video;
import com.bbcow.dao.ErrorVideoDao;
import com.bbcow.dao.VideoDao;
import com.bbcow.db.MongoDB;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;
import com.bbcow.util.HtmlTask;

public abstract class AbstractVideoParser {
	private static VideoDao vd = new VideoDao(MongoDB.db());
	private static ErrorVideoDao evd = new ErrorVideoDao(MongoDB.db());
	public abstract Video parseHtml(UrlMsg vm) throws BusException;
	
	public void toParse(Session session,UrlMsg vm){
		try {
			Video v = parseHtml(vm);
			session.getRemote().sendString(v.getVideo_url());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BusException e) {
			ErrorVideo ev = new ErrorVideo(vm.getUrl(),vm.getHost());
			evd.save(ev);
			
			try {
				if(StringUtils.isNotBlank(e.getMessage())){
					session.getRemote().sendString(e.getMessage());
				}else{
					session.getRemote().sendString("undefined");
				}
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			
		}
	}
	
	public void dealVideoMsg(Video video){
		Key<Video> key = vd.save(video);
		video.setId(new ObjectId(key.getId().toString()));
		
		HtmlTask.addTask(video);
	}
	
}
