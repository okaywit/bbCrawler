package com.bbcow.video;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class YoukuVideo extends AbstractVideoParser{
	private static final String HOST = "youku.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			Element keyword = doc.select("meta[name=keywords]").first();
			Element category = doc.select("meta[name=irCategory]").first();
			Element title = doc.select("h1[class=title]").first();
			Element panel = doc.getElementById("panel_share");
			Element video = panel.getElementById("link2");
			
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag(category.attr("content"));
			v.setTitle(title.text());
			v.setVideo_url(video.val());
			v.setHost(HOST);
			v.setView_count(1);
			
			dealVideoMsg(v);
			
		} catch (IOException e) {
			throw new BusException(e);
		}
		return v;
	}
	
	public static void main(String[] args) {
		YoukuVideo yk = new YoukuVideo();
		try {
			yk.parseHtml(null);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
