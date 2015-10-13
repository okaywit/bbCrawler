package com.bbcow.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.bbcow.bean.Video;
import com.google.common.io.ByteStreams;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

public class HtmlTask implements Runnable{
	private static final String HTML_TEMPLATE_PATH = DocLoader.target_path + DocLoader.getString("html.template.path");
	private static final String HTML_HISTORY_PATH = DocLoader.target_path + DocLoader.getString("html.history.path");
	private static final String HTML_BUILD_PREFIX =  DocLoader.target_path + DocLoader.getString("html.build.prefix");
	
	private static String html_template_content ;
	
	static{
		//视频模板
		InputStream is = HtmlTask.class.getResourceAsStream("/video_template.html");
		try {
			byte[] bs = ByteStreams.toByteArray(is);
			html_template_content = new String(bs);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static BlockingQueue<Video> queue = new LinkedBlockingDeque<Video>();
	
	public static void addTask(Video video){
		HtmlTask.queue.offer(video);
	}
	public static void createHistory(List<Video> videos){
		InputStream is = HtmlTask.class.getResourceAsStream("/history_template.html");
		BufferedWriter hw = null;
		try {
			byte[] bs = ByteStreams.toByteArray(is);
			String h = new String(bs);
			for(Video video : videos){
				h = h.replace("#link", "#link<div class=\"col-sm-6 col-md-2\"><a href='/video/"+video.getId().toString()+".html'>"+video.getTitle()+"</a>("+video.getView_count()+")</div>");
			}
			hw = Files.newWriter(new File(HTML_HISTORY_PATH), Charset.forName("utf-8"));
			hw.write(h);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				hw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
			
		
	}
	private void createVideo(){
		BufferedWriter bw = null;
		
		try {
			Iterator<Video> videos = queue.iterator();
			while(videos.hasNext()){
				Video video = videos.next();
				//视频模板
				bw = Files.newWriter(new File(HTML_BUILD_PREFIX + video.getId() + ".html"), Charset.forName("utf-8"));
				String template = html_template_content;
				//template = template.replace("#url", video.getVideo_url());
				if("zhanqi.tv".equals(video.getHost())){
					template = template.replace("#video", "<iframe class=\"embed-responsive-item\" src=\""+video.getVideo_url()+"\"></iframe>");
				}else{
					template = template.replace("#video", "<embed allownetworking=\"all\" allowscriptaccess=\"always\" src=\""+video.getVideo_url()+"\" quality=\"high\" bgcolor=\"#000\" wmode=\"window\" allowfullscreen=\"true\" allowFullScreenInteractive=\"true\" type=\"application/x-shockwave-flash\">");
				}
				template = template.replaceAll("#title", video.getTitle());
				template = template.replace("#keywords", video.getKeywords());
				template = template.replace("#link", video.getOriginal_url());
				bw.write(template);
				bw.close();
			}
		} catch (IOException e1) {
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
			if(!queue.isEmpty() && queue.size()>10){
				createVideo();
			}
		}
		
	}

}
