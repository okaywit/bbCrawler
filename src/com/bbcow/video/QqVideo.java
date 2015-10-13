package com.bbcow.video;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class QqVideo extends AbstractVideoParser{
	private static final String HOST = "qq.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			String url = vm.getUrl();
			
			Element keyword = doc.select("meta[name=keywords]").first();
			if(StringUtils.endsWith(url, ".html")){
				Element content = doc.select("li[class=list_item list_item_half current] a").first();
				String id = "";
				if(content == null){
					id = StringUtils.substring(url, url.lastIndexOf("/")+1,url.lastIndexOf(".html"));
				}else{
					id = content.attr("id");
				}
				v.setVideo_url("http://static.video.qq.com/TPout.swf?vid="+id+"&auto=1");
			}else{
				int startIndex = url.indexOf("vid")+4;
				int endIndex = url.lastIndexOf("&") > startIndex ? url.lastIndexOf("&") : url.length();
				String id = StringUtils.substring(url, startIndex, endIndex);
				v.setVideo_url("http://static.video.qq.com/TPout.swf?vid="+id+"&auto=1");
				Element content = doc.getElementById(id);
				v.setTitle(content.attr("title"));
			}
			
			Element category = doc.select("meta[itemprop=title]").first();
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag(category.attr("content"));
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
		vm.setUrl("http://v.qq.com/cover/6/6rk0jemko5uecjh/h00157vw2oc.html");
		QqVideo yk = new QqVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
