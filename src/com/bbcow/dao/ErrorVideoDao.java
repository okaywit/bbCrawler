package com.bbcow.dao;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.dao.BasicDAO;

import com.bbcow.bean.ErrorVideo;

public class ErrorVideoDao extends BasicDAO<ErrorVideo, ObjectId>{
	
	public ErrorVideoDao(Datastore datastore) {
		super(datastore);
	}

}
