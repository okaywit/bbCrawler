package com.bbcow.db;

import java.util.ArrayList;
import java.util.List;

import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import com.bbcow.bean.Video;
import com.bbcow.util.DocLoader;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

public class MongoDB {
	private static final int CONNECT_TIMEOUT = DocLoader.getInteger("mongo.bbcow.connectTimeout");
	private static final String USER_NAME = DocLoader.getString("mongo.bbcow.user");
	private static final char[] USER_PASSWORD = DocLoader.getString("mongo.bbcow.password").toCharArray();
	private static final String DATABASE_NAME = DocLoader.getString("mongo.bbcow.database");
	private static final String HOST = DocLoader.getString("mongo.bbcow.host");
	private static final int PORT = DocLoader.getInteger("mongo.bbcow.port");
	
	static class MongoFactory{
		final static Datastore datastore = new Morphia().createDatastore(client(), DATABASE_NAME);
	}
	
	private static MongoClient client(){
		MongoClientOptions clientOptions = MongoClientOptions.builder().connectTimeout(CONNECT_TIMEOUT).build();
		MongoCredential credential = MongoCredential.createCredential(USER_NAME, DATABASE_NAME, USER_PASSWORD);
		List<MongoCredential> list = new ArrayList<MongoCredential>();
		list.add(credential);
		MongoClient mongoClient = new MongoClient(new ServerAddress(HOST, PORT), list, clientOptions);
		return mongoClient;
	}
	
	public static Datastore db(){
		return MongoFactory.datastore;
	}
	
	
	public static void main(String[] args) {
		Video v = new Video();
		v.setKeywords("34");
		MongoDB.db().save(v);
	}
}
