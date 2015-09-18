package com.bbcow.video;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class LongzhuVideo extends AbstractVideoParser{
	private static final String HOST = "longzhu.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			
			String content = doc.toString();
			String vid = content.substring(content.indexOf("BoardCast_Address")+20, content.indexOf("LinkBBSId")-3);
			
			Element keyword = doc.select("meta[name=keywords]").first();
			Element category = doc.select("span.room-status a").first();
			Element title = doc.select("a[class=room-name]").first();
			if(title==null)
				title = doc.select("span[class=room-name]").first();
			
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag(category.text());
			v.setTitle(title.text());
			v.setVideo_url("http://imgcache.qq.com/minivideo_v1/vd/res/TencentPlayerLive.swf?max_age=86400&auto=1&v="+vid);
			v.setHost(HOST);
			v.setView_count(1);
			
			dealVideoMsg(v);
			
		} catch (IOException e) {
			throw new BusException(e);
		}
		return v;
	}
	
	public static void main(String[] args) throws IOException {
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://star.longzhu.com/102022?from=challcontent");
		LongzhuVideo yk = new LongzhuVideo();
		Video v;
		try {
			v = yk.parseHtml(vm);
			System.out.println(v.getKeywords());
			System.out.println(v.getVideo_url());
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
