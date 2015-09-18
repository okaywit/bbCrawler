package com.bbcow.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import com.bbcow.bean.Video;
import com.google.common.io.CharSource;
import com.google.common.io.Files;

public class HtmlTask implements Runnable{
	private static final String HTML_TEMPLATE_PATH = DocLoader.target_path + DocLoader.getString("html.template.path");
	private static final String HTML_HISTORY_PATH = DocLoader.target_path + DocLoader.getString("html.history.path");
	private static final String HTML_BUILD_PREFIX =  DocLoader.target_path + DocLoader.getString("html.build.prefix");
	
	private static String html_template_content ;
	
	static{
		//视频模板
		CharSource bs = Files.asCharSource(new File(HTML_TEMPLATE_PATH), Charset.forName("utf-8"));
		
		try {
			html_template_content = bs.read();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private static BlockingQueue<Video> queue = new LinkedBlockingDeque<Video>();
	
	public static void addTask(Video video){
		HtmlTask.queue.offer(video);
	}
	
	private void createVideo(Video video){
		BufferedWriter bw = null;
		BufferedWriter hw = null;
		try {
			//视频模板
			bw = Files.newWriter(new File(HTML_BUILD_PREFIX + video.getId() + ".html"), Charset.forName("utf-8"));
			String template = html_template_content;
			template = template.replace("#url", video.getVideo_url());
			template = template.replaceAll("#title", video.getTitle());
			template = template.replace("#keywords", video.getKeywords());
			bw.write(template);
			
			//记录页
			CharSource hs =Files.asCharSource(new File(HTML_HISTORY_PATH), Charset.forName("utf-8"));
			String h = hs.read();
			h = h.replace("#link", "#link<li><a href='/video/"+video.getId().toString()+".html'>"+video.getTitle()+"</a></li>");
			
			hw = Files.newWriter(new File(HTML_HISTORY_PATH), Charset.forName("utf-8"));
			hw.write(h);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				bw.close();
				hw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	@Override
	public void run() {
		while(true){
			try {
				Video v = queue.take();
				
				createVideo(v);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
	}

}
