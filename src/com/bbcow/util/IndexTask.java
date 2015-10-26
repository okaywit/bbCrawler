package com.bbcow.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.EchoBootstrap;
import com.bbcow.bean.Video;
import com.bbcow.video.DouyuVideo;
import com.bbcow.video.HuyaVideo;
import com.bbcow.video.LongzhuVideo;
import com.bbcow.video.PandaVideo;
import com.bbcow.video.ZhanqiVideo;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;

public class IndexTask {
	private static Logger logger = LoggerFactory.getLogger(IndexTask.class);
	private static final String HTML_INDEX_PATH = DocLoader.target_path + DocLoader.getString("html.index.path");
	private static final String HTML_DOUYU_PATH = DocLoader.target_path + DocLoader.getString("html.douyu.path");
	private static final String HTML_LONGZHU_PATH = DocLoader.target_path + DocLoader.getString("html.longzhu.path");
	private static final String HTML_ZHANQI_PATH = DocLoader.target_path + DocLoader.getString("html.zhanqi.path");
	private static final String HTML_PANDA_PATH = DocLoader.target_path + DocLoader.getString("html.panda.path");
	private static final String HTML_HUYA_PATH = DocLoader.target_path + DocLoader.getString("html.huya.path");
	public class Task implements Runnable{
		@Override
		public void run() {
			//记录页
			InputStream is = this.getClass().getResourceAsStream("/index_template.html");
			BufferedWriter hw = null;
			
			List<Video> lvs = new LinkedList<Video>();
			lvs.addAll(DouyuVideo.getVideo());
			lvs.addAll(LongzhuVideo.getVideo());
			lvs.addAll(PandaVideo.getVideo());
			lvs.addAll(ZhanqiVideo.getVideo());
			lvs.addAll(HuyaVideo.getVideo());
			
			try {
				byte[] bs = ByteStreams.toByteArray(is);
				
				String template = new String(bs);
				template = template.replaceFirst("#row", getIndex(lvs));
				template = template.replaceFirst("#ad", getAd());
				
				hw = Files.newWriter(new File(HTML_INDEX_PATH), Charset.forName("utf-8"));
				hw.write(template);
			} catch (Exception e) {
				logger.error(e.toString());
			} finally {
				try {
					is.close();
					hw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		    //初始化历史页面
		    //AbstractVideoParser.initHistoryPage();
		}
		
	}
	public class Task_2 implements Runnable{
		@Override
		public void run() {
			//记录页
			InputStream is = this.getClass().getResourceAsStream("/web_template.html");
			String template_douyu = null,template_longzhu = null,template_zhanqi = null,template_panda = null,template_huya = null;
			try {
				byte[] bs = ByteStreams.toByteArray(is);
				template_douyu = template_longzhu = template_zhanqi = template_panda = template_huya = new String(bs);
			} catch (Exception e1) {
				logger.error(e1.toString());
			} finally{
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
			initEvetyWeb(HTML_DOUYU_PATH ,"斗鱼直播",DocLoader.getString("html.douyu.path"),"", template_douyu , DouyuVideo.getDbVideo());
			initEvetyWeb(HTML_LONGZHU_PATH ,"龙珠直播",DocLoader.getString("html.longzhu.path"),"", template_longzhu , LongzhuVideo.getDbVideo());
			initEvetyWeb(HTML_ZHANQI_PATH ,"战旗直播",DocLoader.getString("html.zhanqi.path"),"", template_zhanqi , ZhanqiVideo.getDbVideo());
			initEvetyWeb(HTML_PANDA_PATH ,"熊猫tv直播",DocLoader.getString("html.panda.path"),"", template_panda , PandaVideo.getDbVideo());
			initEvetyWeb(HTML_HUYA_PATH ,"虎牙直播",DocLoader.getString("html.huya.path"),"", template_huya , HuyaVideo.getDbVideo());
			
			//DocLoader.target_path+"douyu.html"
			
		}
		
	}
	public static void initEvetyWeb(String path,String name,String url,String img,String template,List<Video> videos){
		BufferedWriter hw = null;
		for(int i = 0; i<6 ; i++){
			try {
				template = template.replaceFirst("#row", getIndex(videos));
				template = template.replaceFirst("#ad", getAd());
				template = template.replaceAll("#webname", name);
				template = template.replaceAll("#weburl", url);
				//template = template.replaceAll("#webimg", img);
				hw = Files.newWriter(new File(path), Charset.forName("utf-8"));
				hw.write(template);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}  catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
			} finally {
				try {
					hw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static String getAd(){
		String ad_template = "<div class=\"hidden-xs\"><script type=\"text/javascript\">var cpro_id = \"u2306082\";</script><script src=\"http://cpro.baidustatic.com/cpro/ui/c.js\" type=\"text/javascript\"></script></div>";
		return ad_template;
	}
	public static String getIndex(List<Video> videos){
		Collections.sort(videos, new Comparator<Video>() {
			@Override
			public int compare(Video o1, Video o2) {
				return (int)(o2.getView_count() - o1.getView_count());
			}
		});
		int rows = videos.size() / 6;
		StringBuffer sb = new StringBuffer("");
		for(int i=0 ; i<rows ; i++){
			sb.append("<div class=\"row\">");
			for(int j=0 ; j<6 ; j++){
				Video v = videos.get(i * 6 + j);
				sb.append("<div class=\"col-sm-12 col-md-2\">");
				if(v.getId()!=null){
					sb.append("<a href=\"/video/"+ v.getId()+".html\"  target=\"_blank\" role=\"link\">");
				}else{
					sb.append("<a href=\""+ v.getOriginal_url()+"\"  target=\"_blank\" role=\"link\">");
				}
				sb.append("<div class=\"rounded\">")
					.append("<img src=\""+v.getImg()+"\" height='110px' alt=\""+v.getKeywords()+"\" class=\"col-md-12\">")
					.append("<label>"+v.getTitle()+"</label>")
					.append("</div></a></div>");
			}
			sb.append("</div>");
		}
		
		return sb.toString();
	}
	
	public String getRow(List<Video> videos){
		StringBuffer sb = new StringBuffer("<div class=\"row\">");
		for(int i = 0;i<12;i++){
			if(videos.size()>0){
				Video v = videos.get(i);
				sb.append("<div class=\"col-sm-12 col-md-2\">");
				if(v.getId()!=null){
					sb.append("<a href=\"/video/"+ v.getId()+".html\"  target=\"_blank\" role=\"link\">");
				}else{
					sb.append("<a href=\""+ v.getOriginal_url()+"\"  target=\"_blank\" role=\"link\">");
				}
				sb.append("<div class=\"thumbnail\">")
				.append("<img src=\""+v.getImg()+"\" alt=\""+v.getKeywords()+"\" class=\"img-rounded img-responsive\">")
				.append("<label>"+v.getTitle()+"</label>")
				.append("</div></a></div>");
				if(i==5){
					sb.append("</div><div class=\"row\">");
				}
			}else{
				sb.append("<div class=\"col-sm-12 col-md-3\"></div>");
			}
		}
		sb.append("</div>");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		/*IndexTask it = new IndexTask();
		ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
		es.scheduleWithFixedDelay(it.new Task(),0, 10, TimeUnit.SECONDS);*/
		
		String url = "http://api.m.panda.tv/ajax_live_lists?__version=1.0.0.1036&__plat=mobile&pageno=1&pagenum=20&status=2&order=person_num";
		try {
			Response rs  = Jsoup.connect(url).timeout(5000).ignoreContentType(true).header("xiaozhangdepandatv", "1").execute();
			System.out.println(rs.body());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
		//it.getHuya();
	}
}
