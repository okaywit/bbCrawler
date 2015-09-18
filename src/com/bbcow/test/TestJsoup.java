package com.bbcow.test;

import java.io.IOException;




import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestJsoup {
	
	public static void main(String[] args) {
		try {
			Document doc = Jsoup.connect("http://tv.sohu.com/20150901/n420181871.shtml").get();
			/*Element panel = doc.getElementById("panel_share");
			Element video = panel.getElementById("link2");
			
			System.out.println(video.val());*/
			System.out.println(doc);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
