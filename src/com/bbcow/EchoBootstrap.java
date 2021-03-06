package com.bbcow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.server.EchoHandler;
import com.bbcow.task.IndexTask;
import com.bbcow.task.QiniuTask;
import com.bbcow.task.SitemapTask;
import com.bbcow.task.VideoTask;
import com.bbcow.task.IndexTask.Task;
import com.bbcow.util.DocLoader;
import com.bbcow.video.AbstractVideoParser;

public class EchoBootstrap {
	private static Logger logger = LoggerFactory.getLogger(EchoBootstrap.class); 
	
	public static void main(String[] args) throws Exception {
		if(args!=null) {
		    Server server = new Server(2014);
		    server.setHandler(new EchoHandler());
		    server.setStopTimeout(0);
		    server.start();
		    
		    DocLoader.load(args[0]);
			//AbstractVideoParser.initAllPage();
			
		    ScheduledExecutorService es = Executors.newScheduledThreadPool(6);
		    //首页初始化任务
			IndexTask it = new IndexTask();
			es.scheduleWithFixedDelay(it.new Task(),0, 5, TimeUnit.MINUTES);
			es.scheduleWithFixedDelay(it.new Task_2(),1, 6, TimeUnit.MINUTES);
			//单页视频
			es.scheduleWithFixedDelay(new VideoTask(),0, 2, TimeUnit.MINUTES);
			//网站地图
			SitemapTask st = new SitemapTask(); 
			es.scheduleWithFixedDelay(st.new Task(),0, 24, TimeUnit.HOURS);
		    //快照
			es.scheduleWithFixedDelay(new QiniuTask(),0, 1, TimeUnit.HOURS);
		    logger.info("server star");
		    server.join();
		}else{
			logger.error("server star error");
		}
	}
}
