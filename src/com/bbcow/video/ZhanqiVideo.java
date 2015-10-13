package com.bbcow.video;

import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class ZhanqiVideo extends AbstractVideoParser{
	private static final String HOST = "zhanqi.tv";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		/*try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			Element keyword = doc.select("meta[name=keywords]").first();
			Element category = doc.select("div.host-detail span.host-channel a").get(1);
			Element title = doc.select("title").first();
			Element panel = doc.select("input[id=flash-link]").first();
			
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
		}*/
		return v;
	}
	public void parseHtml(Video v){
		v.setKeywords(v.getKeywords());
		v.setOriginal_url(v.getOriginal_url());
		v.setTag("战旗");
		v.setTitle(v.getTitle());
		v.setVideo_url("http://www.zhanqi.tv/live/embed?roomId="+v.getRoom_id());
		v.setHost(HOST);
		
		dealVideoMsg(v);
	}
	
	public static void main(String[] args) {
		UrlMsg vm = new UrlMsg();
		vm.setUrl("http://www.huya.com/dnfer");
		ZhanqiVideo yk = new ZhanqiVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
