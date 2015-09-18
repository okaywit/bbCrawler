package com.bbcow.message;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jetty.websocket.api.Session;

import com.bbcow.video.AbstractVideoParser;
import com.bbcow.video.DouyuVideo;
import com.bbcow.video.HunanVideo;
import com.bbcow.video.HuyaVideo;
import com.bbcow.video.IqiyiVideo;
import com.bbcow.video.LetvVideo;
import com.bbcow.video.LongzhuVideo;
import com.bbcow.video.QqVideo;
import com.bbcow.video.SohuVideo;
import com.bbcow.video.TudouVideo;
import com.bbcow.video.YoukuVideo;
import com.google.gson.Gson;

public class MessageParser {
	
	public static void parseMessage(Session session,String message){
		Gson gson = new Gson();
		UrlMsg vm = gson.fromJson(message, UrlMsg.class);
		AbstractVideoParser avp = null;
		
		if(!StringUtils.contains(vm.getUrl(), "http")){
			vm.setUrl("http://"+vm.getUrl());
		}
		
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "youku.com")){
			avp = new YoukuVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "sohu.com")){
			avp = new SohuVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "huya.com")){
			avp = new HuyaVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "longzhu.com")){
			avp = new LongzhuVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "douyutv.com")){
			avp = new DouyuVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "qq.com")){
			avp = new QqVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "iqiyi.com")){
			avp = new IqiyiVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "tudou.com")){
			avp = new TudouVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "letv.com")){
			avp = new LetvVideo();
		}
		if(StringUtils.containsIgnoreCase(vm.getUrl(), "hunantv.com")){
			avp = new HunanVideo();
		}
		avp.toParse(session,vm);
	}
}
