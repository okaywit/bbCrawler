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

public class HuyaVideo extends AbstractVideoParser{
	private static final String HOST = "huya.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Query<Video> query = vd.createQuery().field("original_url").equal(vm.getUrl());
			v = vd.findOne(query);
			if(v == null){
				Document doc = Jsoup.connect(vm.getUrl()).get();
				Element keyword = doc.select("meta[name=keywords]").first();
				//Element category = doc.select("div.host-detail span.host-channel a").get(1);
				Element title = doc.select("title").first();
				Element panel = doc.select("input[id=flash-link]").first();
				
				v.setKeywords(keyword.attr("content"));
				v.setOriginal_url(vm.getUrl());
				v.setTag("虎牙");
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
		return vd.find(query).asList();
	}
	public static List<Video> getVideo(){
		List<Video> douyus = new LinkedList<Video>();
		try {
			String url = "http://www.yy.com/index/t?tabId=101002&subTabId=&offset=0&limit=25&async=true";
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
						
				v.setImg(e.getAsJsonObject().get("thumb").getAsString()+"?imageview/0/w/280");
				v.setView_count(e.getAsJsonObject().get("users").getAsLong());
				v.setRoom_id(item.get("sid").getAsString()+"/"+item.get("ssid").getAsString());
				v.setUri("hy_"+item.get("liveUid").getAsString()+".html");
				v.setUpdate_time(new Date());
				
				douyus.add(v);
			}
			for(int i =0 ; i<5;i++){
				tops.put(HOST,new ArrayList<Video>(douyus.subList(0, 5)));
			}
			for(Video v : douyus){
				parseHtml(v);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("huya"+e);
		}
		return douyus;
	}
	public static void parseHtml(Video v){
		v.setKeywords(v.getKeywords());
		v.setOriginal_url(v.getOriginal_url());
		v.setTag("虎牙");
		v.setTitle(v.getTitle());
		v.setVideo_url("http://weblbs.yystatic.com/s/"+v.getRoom_id()+"/huyacoop.swf");
		v.setHost(HOST);
		
		dealVideoMsg(v);
	}
	
	public static void main(String[] args) {
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://www.huya.com/dnfer");
		HuyaVideo yk = new HuyaVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
