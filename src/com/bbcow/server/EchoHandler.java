package com.bbcow.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bbcow.message.MessageParser;
import com.bbcow.util.IndexTask;


@WebSocket
public class EchoHandler extends WebSocketHandler {
	private static Logger logger = LoggerFactory.getLogger(EchoHandler.class);
	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		logger.info(" offline " );
	}

	@OnWebSocketError
	public void onError(Throwable t) {
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		logger.info(session.getRemoteAddress()+" online " );
	}

	@OnWebSocketMessage
	public void onMessage(Session session,String message) {
		MessageParser.parseMessage(session,message);
		logger.info(session.getRemoteAddress()+" send message " + message);
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(EchoHandler.class);
	}
	
}
