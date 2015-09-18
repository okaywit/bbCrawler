package com.bbcow.bean;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Transient;

@Entity(value="errorvideo",noClassnameStored=true)
public class ErrorVideo {
	@Id
	private ObjectId id;
	private String original_url;
	private String host;
	
	public ErrorVideo(String original_url,String host){
		this.original_url = original_url;
		this.host = host;
	}
	
	public ObjectId getId() {
		return id;
	}
	public void setId(ObjectId id) {
		this.id = id;
	}
	public String getOriginal_url() {
		return original_url;
	}
	public void setOriginal_url(String original_url) {
		this.original_url = original_url;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	
}
