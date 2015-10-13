package com.bbcow;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.server.EchoHandler;
import com.bbcow.util.DocLoader;
import com.bbcow.util.HtmlTask;
import com.bbcow.util.IndexTask;
import com.bbcow.util.SitemapTask;
import com.bbcow.util.IndexTask.Task;
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
		    
		    Thread t = new Thread(new HtmlTask());
			t.start();
		    
		    //首页初始化任务
		    IndexTask it = new IndexTask();
		    SitemapTask st = new SitemapTask(); 
			ScheduledExecutorService es = Executors.newScheduledThreadPool(1);
			es.scheduleWithFixedDelay(it.new Task(),0, 10, TimeUnit.MINUTES);
			es.scheduleWithFixedDelay(st.new Task(),0, 24, TimeUnit.HOURS);
		    
		    logger.info("server star");
		    server.join();
		}else{
			logger.error("server star error");
		}
	}
}
