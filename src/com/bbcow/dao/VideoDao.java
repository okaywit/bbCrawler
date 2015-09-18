package com.bbcow.dao;

import java.util.List;

import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Key;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.bbcow.bean.Video;

public class VideoDao extends BasicDAO<Video, ObjectId>{
	
	public VideoDao(Datastore datastore) {
		super(datastore);
	}

	@Override
	public Key<Video> save(Video entity) {
		Query<Video> query = createQuery().field("original_url").equal(entity.getOriginal_url());
		List<Key<Video>> vs = query.asKeyList();
		if(vs == null || vs.size()==0){
			return super.save(entity);
		}else{
			UpdateOperations<Video> ops = createUpdateOperations();
			ops.inc("view_count");
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
