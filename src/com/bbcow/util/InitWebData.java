package com.bbcow.util;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;

import com.bbcow.bean.Video;
import com.bbcow.video.DouyuVideo;
import com.bbcow.video.HuyaVideo;
import com.bbcow.video.LongzhuVideo;
import com.bbcow.video.PandaVideo;
import com.bbcow.video.ZhanqiVideo;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class InitWebData {
	public static void initPanda(){
		int i = 0;
		while(true){
			++i;
			String url = "http://api.m.panda.tv/ajax_live_lists?__version=1.0.0.1036&__plat=mobile&pageno="+i+"&pagenum=10&status=2&order=person_num";
			try {
				Response rs  = Jsoup.connect(url).ignoreContentType(true).header("xiaozhangdepandatv", "1").execute();
				JsonObject jsonObject = new JsonParser().parse(rs.body()).getAsJsonObject();
				JsonObject dataObject = jsonObject.get("data").getAsJsonObject();
				JsonArray dataArray = dataObject.get("items").getAsJsonArray();
				for (JsonElement dataElement : dataArray) {
					JsonObject room = dataElement.getAsJsonObject();
					
					Video v = new Video();
					v.setHost("panda.tv");
					v.setKeywords(room.getAsJsonObject("userinfo").get("nickName").getAsString());
					v.setTitle(room.get("name").getAsString());
					v.setOriginal_url("http://www.panda.tv/room/"+room.get("id").getAsString());
					v.setImg(room.getAsJsonObject("pictures").get("img").getAsString());
					v.setView_count(room.get("person_num").getAsLong());
					
					String cu = "http://api.m.panda.tv/ajax_room?roomid="+room.get("id").getAsString()+"&type=json&__version=1.0.0.1039&__plat=mobile";
					Response cs  = Jsoup.connect(cu).ignoreContentType(true).header("xiaozhangdepandatv", "1").execute();
					JsonObject jsonObject3 = new JsonParser().parse(cs.body()).getAsJsonObject();
					JsonObject cd = jsonObject3.get("data").getAsJsonObject();
					JsonObject info = cd.get("info").getAsJsonObject();
					JsonObject videoinfo = info.get("videoinfo").getAsJsonObject();
					v.setRoom_id(room.get("room_key").getAsString()+"&plflag="+videoinfo.get("plflag").getAsString());
					
					PandaVideo.parseHtml(v);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	public static void initLongzhu(){
		int i = 0;
		while(true){
			i = i + 20;
			try {
				String url = "http://api.plu.cn/tga/streams?start-index="+i+"&max-results=20&filter=4";
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
					
					LongzhuVideo.parseHtml(v);
				}
			} catch (Exception e) {
				break;
			}
		}
	}
	public static void initZhanqi(){
		int i = 0;
		while(true){
			++i;
			try {
				String url = "http://www.zhanqi.tv/api/static/live.hots/20-"+i+".json";
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
					
					ZhanqiVideo.parseHtml(v);
				}
			} catch (Exception e) {
				break;
			}
		}
	}
	public static void initHuya(){
		int i = 0;
		while(true){
			i = i + 25;
			try {
				String url = "http://www.yy.com/index/t?tabId=101002&subTabId=&offset="+i+"&limit=25&async=true";
				Response rs  = Jsoup.connect(url).ignoreContentType(true).execute();
				JsonObject object = new JsonParser().parse(rs.body()).getAsJsonObject();
				JsonArray items = object.getAsJsonObject("data").getAsJsonArray("lives");
				for (JsonElement e : items) {
					JsonObject item = e.getAsJsonObject();
					Video v = new Video();
					v.setHost("huya.com");
					v.setKeywords(item.get("liveName").getAsString());
					v.setTitle(item.get("liveName").getAsString());
					v.setOriginal_url("http://huya.com/index.php?m=Live&do=onliveOther&channel="+item.get("sid").getAsString()+"&subChannel="+item.get("ssid").getAsString()+"&src=bbcow.com");
							
					v.setImg(e.getAsJsonObject().get("thumb").getAsString());
					v.setView_count(e.getAsJsonObject().get("users").getAsLong());
					v.setRoom_id(item.get("sid").getAsString()+"/"+item.get("ssid").getAsString());
					
					HuyaVideo.parseHtml(v);
				}
			} catch (Exception e) {
				break;
			}
			
		}
	}
	public static void initDouyu(){
		int i = 0;
		while(true){
			i = i + 20;
			try {
				String url = "http://www.douyutv.com/api/v1/live?aid=android&client_sys=android&limit=20&offset="+i+"&time="+System.currentTimeMillis()+"";
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
					
					DouyuVideo.parseHtml(v);
				}
			} catch (Exception e) {
				break;
			}
		}
	}

	public static void main(String[] args) {
		DocLoader.load("E:/bbcow/bbhtml_2/");
//		InitWebData.initDouyu();
//		InitWebData.initLongzhu();
//		InitWebData.initZhanqi();
//		InitWebData.initHuya();
		InitWebData.initPanda();
	}

}
