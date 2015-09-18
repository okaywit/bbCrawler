package com.bbcow.video;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.BusException;

public class TudouVideo extends AbstractVideoParser{
	private static final String HOST = "tudou.com";
	@Override
	public Video parseHtml(UrlMsg vm) throws BusException {
		Video v = new Video();
		try {
			Document doc = Jsoup.connect(vm.getUrl()).get();
			Element keyword = doc.select("meta[name=keywords]").first();
			Element category = doc.select("meta[name=irCategory]").first();
			Element title = doc.select("meta[name=irAlbumName]").first();
			
			//http://www.tudou.com/programs/view/html5embed.action?code=9gJ9RCrCSqQ
			String content = doc.toString();
			int sidx = content.indexOf("icode")+8;
			int eidx = content.indexOf(",vcode");
			String avid = StringUtils.substring(content,sidx,eidx).trim();
			String vid = avid.substring(0, avid.length()-1);
			
			v.setKeywords(keyword.attr("content"));
			v.setOriginal_url(vm.getUrl());
			v.setTag(category.attr("content"));
			v.setTitle(title.attr("content"));
			v.setVideo_url("http://www.tudou.com/programs/view/html5embed.action?code="+vid);
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
		vm.setUrl("http://www.tudou.com/albumplay/CuoLsJ93g0s.html");
		TudouVideo yk = new TudouVideo();
		try {
			yk.parseHtml(vm);
		} catch (BusException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
