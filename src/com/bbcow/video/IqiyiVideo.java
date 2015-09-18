package com.bbcow.video;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class IqiyiVideo extends AbstractVideoParser{
	private static final String HOST = "iqiyi.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException{
		Video v = new Video();
		try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			Element keyword = doc.select("meta[name=keywords]").first();
			Element category = doc.select("meta[name=irCategory]").first();
			Element title = doc.select("h1[class=mod-play-tit] a").first();
			
			
			Element panel = doc.getElementById("flashbox");
			//http://player.video.qiyi.com/8438fa1d0e14c97e9da6665603ed9b11/0/0/v_19rrocgi88.swf-albumId=397572900-tvId=397572900
			String vid = panel.attr("data-player-videoid");
			String hid = StringUtils.substring(vm.getUrl(), vm.getUrl().lastIndexOf("/"),vm.getUrl().lastIndexOf(".html"));
			String albumId = panel.attr("data-player-albumid");
			String tvId = panel.attr("data-player-tvid");
			
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag(category.attr("content"));
			v.setTitle(title.text());
			v.setVideo_url("http://player.video.qiyi.com/"+vid+"/0/0/"+hid+".swf-albumId="+albumId+"-tvId="+tvId);
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
		vm.setUrl("http://www.iqiyi.com/v_19rrocw6g8.html");
		IqiyiVideo yk = new IqiyiVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
