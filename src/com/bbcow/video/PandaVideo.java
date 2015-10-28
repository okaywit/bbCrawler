package com.bbcow.video;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
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

public class PandaVideo extends AbstractVideoParser{
	private static final String HOST = "panda.tv";
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Query<Video> query = vd.createQuery().field("original_url").equal(vm.getUrl());
			v = vd.findOne(query);
			if(v == null){
				Document doc = Jsoup.connect(vm.getUrl()).header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.93 Safari/537.36").get();
				String html = doc.toString();
				String oroom = html.substring(html.lastIndexOf("{\"user\""), html.lastIndexOf("}}};")+3);
				
				JsonObject jsonObject = new JsonParser().parse(oroom).getAsJsonObject();
				JsonObject room = jsonObject.getAsJsonObject("room");
				JsonObject hostinfo = room.getAsJsonObject("hostinfo");
				JsonObject roomInfo = room.getAsJsonObject("roominfo");
				JsonObject videoinfo = room.getAsJsonObject("videoinfo");
				
				v.setKeywords(hostinfo.get("name").getAsString());
				v.setOriginal_url(vm.getUrl());
				v.setTag("熊猫TV");
				v.setTitle(roomInfo.get("name").getAsString());
				v.setVideo_url("http://s4.pdim.gs/static/19fa49e912574695.swf?videoId="+videoinfo.get("room_key").getAsString()+"&plflag="+videoinfo.get("plflag").getAsString());
				v.setHost(HOST);
				v.setView_count(roomInfo.get("person_num").getAsLong());
				
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
		String url = "http://api.m.panda.tv/ajax_live_lists?__version=1.0.0.1041&__plat=mobile&pageno=1&pagenum=10&status=2&order=person_num";
		String url2 = "http://api.m.panda.tv/ajax_live_lists?__version=1.0.0.1041&__plat=mobile&pageno=1&pagenum=10&status=2&order=person_num";
		try {
			Response rs  = Jsoup.connect(url).ignoreContentType(true).header("xiaozhangdepandatv", "1").execute();
			JsonObject jsonObject = new JsonParser().parse(rs.body()).getAsJsonObject();
			JsonObject dataObject = jsonObject.get("data").getAsJsonObject();
			JsonArray dataArray = dataObject.get("items").getAsJsonArray();
		/*	
			Response rs2  = Jsoup.connect(url2).ignoreContentType(true).header("xiaozhangdepandatv", "1").execute();
			JsonObject jsonObject2 = new JsonParser().parse(rs2.body()).getAsJsonObject();
			JsonObject dataObject2 = jsonObject2.get("data").getAsJsonObject();
			dataArray.addAll(dataObject2.get("items").getAsJsonArray());*/
			for (JsonElement dataElement : dataArray) {
				JsonObject room = dataElement.getAsJsonObject();
				
				Video v = new Video();
				v.setHost(HOST);
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
				
				/*if(room.get("level").getAsInt()<10){
					v.setRoom_id(room.get("room_key").getAsString()+"&plflag=1_2");
				}else{
					v.setRoom_id(room.get("room_key").getAsString()+"&plflag=2_3");
				}*/
				
				parseHtml(v);
				
				douyus.add(v);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return douyus;
	}
	public static void parseHtml(Video v){
		v.setKeywords(v.getKeywords());
		v.setOriginal_url(v.getOriginal_url());
		v.setTag("熊猫TV");
		v.setTitle(v.getTitle());
		v.setVideo_url("http://s4.pdim.gs/static/19fa49e912574695.swf?videoId="+v.getRoom_id());
		v.setHost(HOST);
		
		dealVideoMsg(v);
	}
	
	public static void main(String[] args) {
		
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://www.panda.tv/room/10029");
		PandaVideo yk = new PandaVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			e.printStackTrace();
		}
	}
}
