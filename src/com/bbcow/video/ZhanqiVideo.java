package com.bbcow.video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.mongodb.morphia.query.Query;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ZhanqiVideo  extends AbstractVideoParser{
	private static final String HOST = "zhanqi.tv";
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Query<Video> query = vd.createQuery().field("original_url").equal(vm.getUrl());
			v = vd.findOne(query);
			if(v == null){
				Document doc = Jsoup.connect(vm.getUrl()).get();
				Element keyword = doc.select("meta[name=keywords]").first();
				Element title = doc.select("title").first();
				String cont = doc.html();
				int sind = cont.lastIndexOf("\"RoomId\":")+9;
				int eind = cont.lastIndexOf(",\"ComLayer");
				String vurl ="http://www.zhanqi.tv/live/embed?roomId="+cont.substring(sind,eind);
				
				v.setKeywords(keyword.attr("content"));
				v.setOriginal_url(vm.getUrl());
				v.setTag("战旗");
				v.setTitle(title.text());
				v.setVideo_url(vurl);
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
		return vd.find(query).asList();
	}
	public static List<Video> getVideo(){
		List<Video> douyus = new LinkedList<Video>();
		try {
			String url = "http://www.zhanqi.tv/api/static/live.hots/20-1.json";
			Response rs  = Jsoup.connect(url).ignoreContentType(true).execute();
			JsonObject object = new JsonParser().parse(rs.body()).getAsJsonObject();
			JsonArray items = object.getAsJsonObject("data").getAsJsonArray("rooms");
			for (JsonElement e : items) {
				JsonObject item = e.getAsJsonObject();
				Video v = new Video();
				v.setHost("zhanqi.tv");
				v.setKeywords(item.get("title").getAsString());
				v.setTitle(item.get("nickname").getAsString());
				v.setOriginal_url("http://www.zhanqi.tv"+item.get("url").getAsString());
				v.setImg(e.getAsJsonObject().get("spic").getAsString());
				v.setView_count(e.getAsJsonObject().get("online").getAsLong());
				v.setRoom_id(item.get("id").getAsString());
				v.setUri("zq_"+item.get("id").getAsString()+".html");
				v.setUpdate_time(new Date());
				
				douyus.add(v);
			}
			tops.put(HOST,new ArrayList<Video>(douyus.subList(0, 5)));
			for(Video v : douyus){
				parseHtml(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("zhanqi"+e);
		}
		return douyus;
	}
	public static void parseHtml(Video v){
		v.setKeywords(v.getKeywords());
		v.setOriginal_url(v.getOriginal_url());
		v.setTag("战旗");
		v.setTitle(v.getTitle());
		v.setVideo_url("http://www.zhanqi.tv/live/embed?roomId="+v.getRoom_id());
		v.setHost(HOST);
		
		dealVideoMsg(v);
	}
	
	public static void main(String[] args) {
		
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://www.zhanqi.tv/xinba");
		ZhanqiVideo yk = new ZhanqiVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
