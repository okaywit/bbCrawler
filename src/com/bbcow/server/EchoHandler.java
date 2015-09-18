package com.bbcow.server;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.server.WebSocketHandler;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

import com.bbcow.message.MessageParser;


@WebSocket
public class EchoHandler extends WebSocketHandler {
	@OnWebSocketClose
	public void onClose(int statusCode, String reason) {
		System.out.println("close");
	}

	@OnWebSocketError
	public void onError(Throwable t) {
	}

	@OnWebSocketConnect
	public void onConnect(Session session) {
		
		System.out.println("connect");
	}

	@OnWebSocketMessage
	public void onMessage(Session session,String message) {
		MessageParser.parseMessage(session,message);
	}

	@Override
	public void configure(WebSocketServletFactory factory) {
		factory.register(EchoHandler.class);
	}
	
}
