package com.bbcow.video;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.query.Query;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class DouyuVideo extends AbstractVideoParser{
	private static final String HOST = "douyutv.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Query<Video> query = vd.createQuery().field("original_url").equal(vm.getUrl());
			v = vd.findOne(query);
			if(v == null){
				Document doc = Jsoup.connect(vm.getUrl()).get();
				Element keyword = doc.select("h1").first();
				Element category = doc.select("ul.r_else li i").first();
				Element title = doc.select("i[class=zb_name]").first();
				Element panel = doc.select("div.cc input.inn").get(1);
				
				v.setKeywords(keyword.attr("content"));
				v.setOriginal_url(vm.getUrl());
				v.setTag(category.text());
				v.setTitle(title.text());
				v.setVideo_url(panel.val());
				v.setHost(HOST);
				v.setView_count(1);
				
				dealVideoMsg(v);
			}	
		} catch (IOException e) {
			throw new BusException(e);
		}
		return v;
	}
	public static List<Video> getDbVideo(){
		Query<Video> query = vd.createQuery().field("host").equal(HOST).order("-update_time").limit(100);
		List<Video> list = vd.find(query).asList();
		return list;
	}
	public static List<Video> getVideo(){
		List<Video> douyus = new LinkedList<Video>();
		try {
			String url = "http://www.douyutv.com/api/v1/live?aid=android&client_sys=android&limit=20&offset=0&time="+System.currentTimeMillis()+"";
			Response rs  = Jsoup.connect(url).ignoreContentType(true).execute();
			JsonObject object = new JsonParser().parse(rs.body()).getAsJsonObject();
			JsonArray items = object.getAsJsonArray("data");
			
			for (JsonElement e : items) {
				JsonObject item = e.getAsJsonObject();
				Video v = new Video();
				v.setHost("douyutv.com");
				v.setKeywords(item.get("room_name").getAsString());
				v.setTitle(item.get("nickname").getAsString());
				v.setOriginal_url("http://www.douyutv.com"+item.get("url").getAsString());
				v.setImg(item.get("room_src").getAsString());
				v.setView_count(item.get("online").getAsLong());
				v.setRoom_id(item.get("room_id").getAsString());
				
				parseHtml(v);
				
				douyus.add(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("douyu"+e);
		}
		return douyus;
	}
	public static void parseHtml(Video v){
		v.setTag("斗鱼");
		v.setVideo_url("http://staticlive.douyutv.com/common/share/play.swf?room_id="+v.getRoom_id());
		v.setHost(HOST);
		
		dealVideoMsg(v);
	}
	
	public static void main(String[] args) {
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://www.douyutv.com/caomei");
		DouyuVideo yk = new DouyuVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
