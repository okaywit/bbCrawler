package com.bbcow.dao;

import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.bbcow.bean.Video;

public class VideoDao extends BasicDAO<Video, ObjectId>{
	private static int i = 0;
	public VideoDao(Datastore datastore) {
		super(datastore);
	}

	@Override
	public Key<Video> save(Video entity) {
		Query<Video> query = createQuery().field("original_url").equal(entity.getOriginal_url());
		List<Key<Video>> vs = query.asKeyList();
		if(vs == null || vs.size()==0){
			entity.setUpdate_time(new Date());
			return super.save(entity);
		}else{
			UpdateOperations<Video> ops = createUpdateOperations();
			if(entity.getView_count()>0){
				ops.set("view_count", entity.getView_count());
			}else{
				ops.inc("view_count");
			}
			if(entity.getImg()!=null)
				ops.set("img", entity.getImg());
			if(entity.getVideo_url()!=null)
				ops.set("video_url", entity.getVideo_url());
			if(entity.getTitle()!=null)
				ops.set("title", entity.getTitle());
			if(entity.getKeywords()!=null)
				ops.set("keywords", entity.getKeywords());
			ops.set("update_time", new Date());
			super.update(query, ops);
			return vs.get(0);
		}
		
	}

	public List<Video> getTop(){
		return createQuery().order("view_count").batchSize(10).asList();
	}
	
	public static void main(String[] args) {
		/*VideoDao vd = new VideoDao(MongoDB.db());
		System.out.println(vd.getTop());*/
		
	}
}
