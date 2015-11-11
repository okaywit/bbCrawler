package com.bbcow.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.bson.codecs.ObjectIdGenerator;
import org.bson.types.ObjectId;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.bean.Video;
import com.bbcow.message.UrlMsg;
import com.bbcow.util.DocLoader;
import com.bbcow.video.AbstractVideoParser;
import com.bbcow.video.DouyuVideo;
import com.bbcow.video.HuyaVideo;
import com.bbcow.video.LongzhuVideo;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class SitemapTask {
	private static Logger logger = LoggerFactory.getLogger(SitemapTask.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'+00:00'") ;
	private static String sitemap_template_content;

	static {
		InputStream is = VideoTask.class
				.getResourceAsStream("/sitemap_template.xml");
		try {
			byte[] bs = ByteStreams.toByteArray(is);
			sitemap_template_content = new String(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public class Task implements Runnable {
		@Override
		public void run() {
			File f = new File(DocLoader.target_path+"video");
			File[] fs = f.listFiles();
			StringBuffer sb = new StringBuffer();
			for (File fi : fs) {
				sb.append("<url>\r")
					.append("<loc>http://www.bbcow.com/").append(fi.getName()).append("</loc>\r")
					.append("<priority>0.6</priority>\r")
					.append("<lastmod>").append(SitemapTask.sdf.format(fi.lastModified())).append("</lastmod>\r")
					.append("<changefreq>daily</changefreq>\r")
					.append("</url>\r");
			}

			String sitemap = sitemap_template_content.replace("#more", sb.toString()).replaceAll("#now", SitemapTask.sdf.format(System.currentTimeMillis()));

			BufferedWriter bw = null;

			try {
				bw = Files.newWriter(new File(DocLoader.target_path+"sitemap.xml"), Charset.forName("utf-8"));
				bw.write(sitemap);
			} catch (Exception e1) {
				logger.error(e1.toString());
			} finally {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

	public static void main(String[] args) {
		
		/* SitemapTask it = new SitemapTask("E:\\bbcow\\bbhtml_2"); 
		 ScheduledExecutorService es = Executors.newScheduledThreadPool(1); 
		 es.scheduleWithFixedDelay(it.new Task(),0, 10, TimeUnit.SECONDS);*/
		 

		
	}
}
