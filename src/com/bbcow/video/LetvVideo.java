package com.bbcow.video;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class LetvVideo extends AbstractVideoParser{
	private static final String HOST = "letv.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			String url = vm.getUrl();
			String swfUrl = "http://player.letvcdn.com/lc01_p/201509/14/16/04/16/newplayer/LetvPlayer.swf?id=";
			if(url.contains("live.letv.com")){
				swfUrl = "http://player.hz.letv.com/live.swf/open?channelId=";
				int startIndex = url.indexOf("channel")+8;
				int endIndex = url.lastIndexOf("&") > startIndex ? url.lastIndexOf("&") : url.length();
				String id = StringUtils.substring(url, startIndex, endIndex);
				v.setVideo_url(swfUrl+id);
			}else if(url.contains("www.letv.com")){
				swfUrl = "http://player.letvcdn.com/lc01_p/201509/14/16/04/16/newplayer/LetvPlayer.swf?id=";
				if(StringUtils.endsWith(url, ".html")){
					int startIndex = url.lastIndexOf("/")+1;
					int endIndex = url.lastIndexOf(".html");
					String id = StringUtils.substring(url, startIndex, endIndex);
					v.setVideo_url(swfUrl+id);
				}else{
					int startIndex = url.indexOf("vid")+4;
					int endIndex = url.lastIndexOf("&") > startIndex ? url.lastIndexOf("&") : url.length();
					String id = StringUtils.substring(url, startIndex, endIndex);
					v.setVideo_url(swfUrl+id);
				}
			}
			Element keyword = doc.select("meta[name=keywords]").first();
			Element content = doc.select("title").first();
			//Element category = doc.select("meta[itemprop=title]").first();
			v.setTitle(content.text());
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag("乐视");
			v.setHost(HOST);
			v.setView_count(1);
			
			dealVideoMsg(v);
			
		} catch (IOException e) {
			throw new BusException(e);
		}
		return v;
	}
	
	public static void main(String[] args) {
		//
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://live.letv.com/lunbo/play/index.shtml?channel=224");
		LetvVideo yk = new LetvVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
