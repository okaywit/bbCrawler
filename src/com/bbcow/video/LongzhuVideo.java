package com.bbcow.video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
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

public class LongzhuVideo extends AbstractVideoParser{
	private static final String HOST = "longzhu.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Query<Video> query = vd.createQuery().field("original_url").equal(vm.getUrl());
			v = vd.findOne(query);
			if(v == null){
				Document doc = Jsoup.connect(vm.getUrl()).get();
				
				String content = doc.toString();
				String vid = content.substring(content.indexOf("BoardCast_Address")+20, content.indexOf("LinkBBSId")-3);
				
				Element keyword = doc.select("meta[name=keywords]").first();
				Element category = doc.select("span.room-status a").first();
				Element title = doc.select("a[class=room-name]").first();
				if(title==null)
					title = doc.select("span[class=room-name]").first();
				
				v.setKeywords(keyword.attr("content"));
				v.setOriginal_url(vm.getUrl());
				v.setTag(category.text());
				v.setTitle(title.text());
				v.setVideo_url("http://imgcache.qq.com/minivideo_v1/vd/res/TencentPlayerLive.swf?max_age=86400&auto=1&v="+vid);
				v.setHost(HOST);
				v.setView_count(1);
				
				dealVideoMsg(v);
			}
			
		} catch (Exception e) {
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
			String url = "http://api.plu.cn/tga/streams?start-index=0&max-results=20&filter=4";
			Response rs  = Jsoup.connect(url).ignoreContentType(true).execute();
			JsonObject object = new JsonParser().parse(rs.body()).getAsJsonObject();
			JsonArray items = object.getAsJsonObject("data").getAsJsonArray("items");
			
			for (JsonElement e : items) {
				JsonObject item = e.getAsJsonObject().getAsJsonObject("channel");
				Video v = new Video();
				v.setHost("longzhu.com");
				v.setKeywords(item.get("status").getAsString());
				v.setTitle(item.get("name").getAsString());
				v.setOriginal_url(item.get("url").getAsString());
				v.setImg(e.getAsJsonObject().get("preview").getAsString());
				v.setView_count(e.getAsJsonObject().get("viewers").getAsLong());
				if(StringUtils.isBlank(item.get("vid").getAsString()) || "0".equals(item.get("vid").getAsString())){
					v.setRoom_id("roomId="+item.get("id").getAsString());
				}else{
					v.setRoom_id("v="+item.get("vid").getAsString());
				}
				v.setUri("lz_"+item.get("id").getAsString()+".html");
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
			System.out.println("longzhu"+e);
		}
		return douyus;
	}
	
	public static void parseHtml(Video v){
		v.setKeywords(v.getKeywords());
		v.setOriginal_url(v.getOriginal_url());
		v.setTag("龙珠");
		v.setTitle(v.getTitle());
		if(StringUtils.contains(v.getRoom_id(), "roomId")){
			v.setVideo_url("http://r.plures.net/proton/flash/streaming-ifp2rgic.swf?hasNextBtn=0&hasMovieBtn=0&autoPlay=1&"+v.getRoom_id());
		}else{
			v.setVideo_url("http://imgcache.qq.com/minivideo_v1/vd/res/TencentPlayerLive.swf?max_age=86400&auto=1&"+v.getRoom_id());
		}
		
		v.setHost(HOST);
		
		dealVideoMsg(v);
	}
	
	public static void main(String[] args) throws IOException {
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://star.longzhu.com/102022?from=challcontent");
		LongzhuVideo yk = new LongzhuVideo();
		Video v;
		try {
			v = yk.parseHtml(vm);
			System.out.println(v.getKeywords());
			System.out.println(v.getVideo_url());
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
