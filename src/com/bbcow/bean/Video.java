package com.bbcow.bean;

import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Field;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Index;
import org.mongodb.morphia.annotations.IndexOptions;
import org.mongodb.morphia.annotations.Indexes;
import org.mongodb.morphia.annotations.Transient;

@Entity(value="video",noClassnameStored=true)
@Indexes({
	@Index(fields={@Field(value="original_url")},options=@IndexOptions(unique=true)),
})
public class Video {
	@Id
	private ObjectId id;
	private String original_url;
	private String title;
	private String tag;
	private String keywords;
	private String video_url;
	private String host;
	private String img;
	private String room_id;
	private long view_count;
	private Date update_time;
	
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTag() {
		return tag;
	}
	public void setTag(String tag) {
		this.tag = tag;
	}
	public String getKeywords() {
		return keywords;
	}
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	public String getVideo_url() {
		return video_url;
	}
	public void setVideo_url(String video_url) {
		this.video_url = video_url;
	}
	public String getHost() {
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	public long getView_count() {
		return view_count;
	}
	public void setView_count(long view_count) {
		this.view_count = view_count;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getRoom_id() {
		return room_id;
	}
	public void setRoom_id(String room_id) {
		this.room_id = room_id;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	
}
