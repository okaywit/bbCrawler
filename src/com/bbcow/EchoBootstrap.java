package com.bbcow;

import org.eclipse.jetty.server.Server;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.server.EchoHandler;
import com.bbcow.util.DocLoader;
import com.bbcow.util.HtmlTask;

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
		    
		    logger.info("server star");
		    server.join();
		}else{
			logger.error("server star error");
		}
	}
}
