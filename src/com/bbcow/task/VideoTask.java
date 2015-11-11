package com.bbcow.task;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.bean.Video;
import com.bbcow.util.BaiduPing;
import com.bbcow.util.Constants;
import com.bbcow.util.DocLoader;
import com.bbcow.video.AbstractVideoParser;
import com.bbcow.video.PandaVideo;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.Files;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class VideoTask implements Runnable{
	private static Logger logger = LoggerFactory.getLogger(VideoTask.class);
	private static final String HTML_TEMPLATE_PATH = DocLoader.target_path + DocLoader.getString("html.template.path");
	private static final String HTML_HISTORY_PATH = DocLoader.target_path + DocLoader.getString("html.history.path");
	private static final String HTML_BUILD_PREFIX =  DocLoader.target_path + DocLoader.getString("html.build.prefix");
	static int i = 0;
	private static String html_template_content ;
	
	static{
		//视频模板
		InputStream is = VideoTask.class.getResourceAsStream("/video_template.html");
		try {
			byte[] bs = ByteStreams.toByteArray(is);
			html_template_content = new String(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static BlockingQueue<Video> queue = new LinkedBlockingDeque<Video>();
	
	public static void addTask(Video video){
		VideoTask.queue.offer(video);
	}
	public static void createHistory(List<Video> videos){
		InputStream is = VideoTask.class.getResourceAsStream("/history_template.html");
		BufferedWriter hw = null;
		try {
			byte[] bs = ByteStreams.toByteArray(is);
			String h = new String(bs);
			for(Video video : videos){
				h = h.replace("#link", "#link<div class=\"col-sm-6 col-md-2\"><a href='/video/"+video.getId().toString()+".html'>"+video.getTitle()+"</a>("+video.getView_count()+")</div>");
			}
			hw = Files.newWriter(new File(HTML_HISTORY_PATH), Charset.forName("utf-8"));
			hw.write(h);
		} catch (Exception e) {
			logger.error(e.toString());
		} finally {
			try {
				hw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		
	}
	public static void createVideo(Video video){
		BufferedWriter bw = null;
		try {
			//视频模板
			bw = Files.newWriter(new File(HTML_BUILD_PREFIX + video.getUri()), Charset.forName("utf-8"));
			String template = html_template_content;
			//template = template.replace("#url", video.getVideo_url());
			if("zhanqi.tv".equals(video.getHost())){
				template = template.replace("#video", "<iframe class=\"embed-responsive-item\" src=\""+video.getVideo_url()+"\"></iframe>");
			}else{
				template = template.replace("#video", "<embed allownetworking=\"all\" allowscriptaccess=\"always\" src=\""+video.getVideo_url()+"\" quality=\"high\" bgcolor=\"#000\" wmode=\"window\" allowfullscreen=\"true\" allowFullScreenInteractive=\"true\" type=\"application/x-shockwave-flash\">");
			}
			//TOP5
			List<Video> vs = AbstractVideoParser.tops.get(video.getHost());
			for(int i=0;i<5;i++){
				template = template.replaceAll("#link"+i, "/video/"+vs.get(i).getUri());
				template = template.replace("#title"+i, vs.get(i).getTitle());
				template = template.replace("#img"+i, vs.get(i).getImg());
				template = template.replace("#count"+i, vs.get(i).getView_count()+"");
			}
			
			template = template.replaceAll("#title", video.getTitle());
			if(video.getKeywords()!=null)
				template = template.replaceAll("#keywords", video.getKeywords());
			if(video.getImg()!=null)
				template = template.replace("#img", video.getImg());
			template = template.replace("#uri", video.getUri());
			template = template.replaceAll("#link", video.getOriginal_url());
			template = template.replaceAll("#tag", video.getTag());
			template = template.replaceAll("#count", video.getView_count()+"");
			ZonedDateTime now = ZonedDateTime.now(ZoneOffset.UTC);
			template = template.replace("#date", now.toString());
			
			if(video.getView_count() > Constants.BAIDU_PING_MIN_COUNT){
				BaiduPing.site("http://www.bbcow.com/video/"+video.getUri());
			}
			
			bw.write(template);
			bw.close();
			
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			try {
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}


	@Override
	public void run() {
		while(true){
			try {
				createVideo(queue.take());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
