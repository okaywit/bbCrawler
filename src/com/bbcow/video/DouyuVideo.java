package com.bbcow.video;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class DouyuVideo extends AbstractVideoParser{
	private static final String HOST = "douyutv.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			Element keyword = doc.select("meta[name=keywords]").first();
			Element category = doc.select("ul.r_else li a").first();
			Element title = doc.select("h1").first();
			Element panel = doc.select("div.cc input.inn").get(1);
			
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag(category.text());
			v.setTitle(title.text());
			v.setVideo_url(panel.val());
			v.setHost(HOST);
			v.setView_count(1);
			
			dealVideoMsg(v);
			
		} catch (IOException e) {
			throw new BusException(e);
		}
		return v;
	}
	
	public static void main(String[] args) {
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://www.douyutv.com/caomei");
		DouyuVideo yk = new DouyuVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
