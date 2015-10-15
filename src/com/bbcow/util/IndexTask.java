package com.bbcow.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.ObjectId;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.video.AbstractVideoParser;
import com.bbcow.video.DouyuVideo;
import com.bbcow.video.HuyaVideo;
import com.bbcow.video.LongzhuVideo;
import com.bbcow.video.ZhanqiVideo;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class IndexTask {
	private static final String HTML_INDEX_PATH = DocLoader.target_path + DocLoader.getString("html.index.path");
	public class Task implements Runnable{
		@Override
		public void run() {
			List<Video> douyus = getDouyu();
			List<Video> longzhus = getLongzhu();
			List<Video> zhanqis = getZhanqi();
			List<Video> huyas = getHuya();
			
			//记录页
			InputStream is = this.getClass().getResourceAsStream("/index_template.html");
			BufferedWriter hw = null;
			try {
				byte[] bs = ByteStreams.toByteArray(is);
				
				String template = new String(bs);
				Video v = null;
				StringBuffer sb = new StringBuffer("<div class=\"row\">");
				for(int i = 0;i<12;i++){
					if(douyus.size()>0){
						v = douyus.get(i);
						sb.append("<div class=\"col-sm-6 col-md-2\">")
						.append("<img src=\""+v.getImg()+"\" alt=\""+v.getKeywords()+"\" class=\"img-rounded img-responsive\">");
						if(v.getId()!=null){
							sb.append("<a href=\"/video/"+ v.getId()+".html\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}else{
							sb.append("<a href=\""+ v.getOriginal_url()+"\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}
						sb.append("</div>");
						if(i==5){
							sb.append("</div><div class=\"row\">");
						}
					}else{
						sb.append("<div class=\"col-sm-6 col-md-3\"></div>");
					}
				}
				sb.append("</div><div class=\"row ad\">");
				sb.append("<div class=\"hidden-xs\"><script type=\"text/javascript\">var cpro_id = \"u2306082\";</script><script src=\"http://cpro.baidustatic.com/cpro/ui/c.js\" type=\"text/javascript\"></script></div></div><div class=\"row\">");
				for(int i = 0;i<12;i++){
					if(longzhus.size()>0){
						v = longzhus.get(i);
						sb.append("<div class=\"col-sm-6 col-md-2\">")
						.append("<img src=\""+v.getImg()+"\" alt=\""+v.getKeywords()+"\" class=\"img-rounded img-responsive\">");
						if(v.getId()!=null){
							sb.append("<a href=\"/video/"+ v.getId()+".html\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}else{
							sb.append("<a href=\""+ v.getOriginal_url()+"\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}
						sb.append("</div>");
						if(i==5){
							sb.append("</div><div class=\"row\">");
						}
					}else{
						sb.append("<div class=\"col-sm-6 col-md-3\"></div>");
					}
				}
				sb.append("</div><div class=\"row ad\">");
				sb.append("<div class=\"hidden-xs\"><script type=\"text/javascript\">var cpro_id = \"u2306082\";</script><script src=\"http://cpro.baidustatic.com/cpro/ui/c.js\" type=\"text/javascript\"></script></div></div><div class=\"row\">");
				for(int i = 0;i<12;i++){
					if(huyas.size()>0){
						v = huyas.get(i);
						sb.append("<div class=\"col-sm-6 col-md-2\">")
						.append("<img src=\""+v.getImg()+"\" alt=\""+v.getKeywords()+"\" class=\"img-rounded img-responsive\">");
						if(v.getId()!=null){
							sb.append("<a href=\"/video/"+ v.getId()+".html\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}else{
							sb.append("<a href=\""+ v.getOriginal_url()+"\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}
						sb.append("</div>");
						if(i==5){
							sb.append("</div><div class=\"row\">");
						}
					}else{
						sb.append("<div class=\"col-sm-6 col-md-3\"></div>");
					}
				}
				sb.append("</div><div class=\"row ad\">");
				sb.append("<div class=\"hidden-xs\"><script type=\"text/javascript\">var cpro_id = \"u2306082\";</script><script src=\"http://cpro.baidustatic.com/cpro/ui/c.js\" type=\"text/javascript\"></script></div></div><div class=\"row\">");
				for(int i = 0;i<12;i++){
					if(zhanqis.size()>0){
						/*v = zhanqis.get(i);
						sb.append("<div class=\"col-sm-6 col-md-2\">")
						.append("<img src=\""+v.getImg()+"\" alt=\""+v.getKeywords()+"\" class=\"img-rounded img-responsive\">")
						.append("<a href=\""+ v.getOriginal_url()+"\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>")
						.append("</div>");
						if(i==5){
							sb.append("</div><div class=\"row\">");
						}*/
						v = zhanqis.get(i);
						sb.append("<div class=\"col-sm-6 col-md-2\">")
						.append("<img src=\""+v.getImg()+"\" alt=\""+v.getKeywords()+"\" class=\"img-rounded img-responsive\">");
						if(v.getId()!=null){
							sb.append("<a href=\"/video/"+ v.getId()+".html\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}else{
							sb.append("<a href=\""+ v.getOriginal_url()+"\"  target=\"_blank\" role=\"link\">").append("<h3>"+v.getTitle()+"</h3></a>");
						}
						sb.append("</div>");
						if(i==5){
							sb.append("</div><div class=\"row\">");
						}
					}else{
						sb.append("<div class=\"col-sm-6 col-md-3\"></div>");
					}
				}
				sb.append("</div>");
				
				template = template.replace("#row", sb.toString()+"#row");
				hw = Files.newWriter(new File(HTML_INDEX_PATH), Charset.forName("utf-8"));
				hw.write(template);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					is.close();
					hw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		    //初始化历史页面
		    AbstractVideoParser.initAllPage();
			
		}
		
	}
	public List<Video> getDouyu(){
		List<Video> douyus = new LinkedList<Video>();
		DouyuVideo yk = new DouyuVideo();
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
				
				yk.parseHtml(v);
				
				douyus.add(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("---"+e);
		}
		return douyus;
	}
	
	public List<Video> getLongzhu(){
		List<Video> douyus = new LinkedList<Video>();
		LongzhuVideo yk = new LongzhuVideo();
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
				
				v.setRoom_id(item.get("vid").getAsString());
				
				yk.parseHtml(v);
				
				douyus.add(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("---"+e);
		}
		return douyus;
	}
	
	public List<Video> getHuya(){
		List<Video> douyus = new LinkedList<Video>();
		HuyaVideo yk = new HuyaVideo();
		try {
			String url = "http://api.m.huya.com/gamelabel/labellive?limit=20&platform=android&labelid=5&page=1&gameid=1&version=3.0.13";
			Response rs  = Jsoup.connect(url).ignoreContentType(true).execute();
			JsonObject object = new JsonParser().parse(rs.body()).getAsJsonObject();
			JsonArray items = object.getAsJsonObject("data").getAsJsonArray("lives");
			for (JsonElement e : items) {
				JsonObject item = e.getAsJsonObject();
				Video v = new Video();
				v.setHost("huya.com");
				v.setKeywords(item.get("sLiveDesc").getAsString());
				v.setTitle(item.get("sNick").getAsString());
				v.setOriginal_url("http://www.huya.com"+item.get("lYYId").getAsString());
				v.setImg(e.getAsJsonObject().get("sVideoCaptureUrl").getAsString());
				v.setView_count(e.getAsJsonObject().get("iAttendeeCount").getAsLong());
				v.setRoom_id(item.get("lChannelId").getAsString()+"/"+item.get("lSubchannel").getAsString());
				
				yk.parseHtml(v);
				
				douyus.add(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("---"+e);
		}
		return douyus;
	}
	
	
	public List<Video> getZhanqi(){
		List<Video> douyus = new LinkedList<Video>();
		ZhanqiVideo yk = new ZhanqiVideo();
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
				
				yk.parseHtml(v);
				
				douyus.add(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("---"+e);
		}
		return douyus;
	}
	
	public static void main(String[] args) {
		IndexTask it = new IndexTask();
		ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
		es.scheduleWithFixedDelay(it.new Task(),0, 10, TimeUnit.SECONDS);
		
		//it.getHuya();
	}
}
