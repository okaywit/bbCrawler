package com.bbcow.video;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class HunanVideo extends AbstractVideoParser{
	private static final String HOST = "hunantv.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			String swfUrl = "http://i1.hunantv.com/ui/swf/share/player.swf?video_id=";
			String url = vm.getUrl();
			if(url.contains("www.hunantv.com")){
				int startIndex = url.lastIndexOf("/")+1;
				int endIndex = url.lastIndexOf(".html");
				String id = StringUtils.substring(url, startIndex, endIndex);
				v.setVideo_url(swfUrl+id);
			}
			v.setHost(HOST);
			v.setView_count(1);
			
			Document doc = Jsoup.connect(vm.getUrl()).get();
			Element keyword = doc.select("meta[name=keywords]").first();
			Element content = doc.select("title").first();
			Element category = doc.select("div.play-index-tiltag a").get(1);
			v.setTitle(content.text());
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag(category.text());
			
			dealVideoMsg(v);
			
		} catch (IOException e) {
			throw new BusException(v.getVideo_url(),e);
		}
		return v;
	}
	
	public static void main(String[] args) {
		//
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://www.hunantv.com/v/1/158128/c/1777053.html");
		HunanVideo yk = new HunanVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			e.printStackTrace();
		}
	}
}
