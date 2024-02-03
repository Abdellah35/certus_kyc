package com.softedge.solution.repository.impl;

import com.mongodb.*;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Repository
@Slf4j
public class KycUserDocumentsRepositoryImpl {

    @Autowired
    @Qualifier("documents-db")
    private MongoClient mongoClient;


    public boolean loadUserDocument(Map<String,Object> map){
        DB db = mongoClient.getDB("documents_db");
        DBCollection collection = db.getCollection("user_documents");
        WriteResult insert = collection.insert(new BasicDBObject(map));
        if(insert.wasAcknowledged()){
            log.info("Load user data document inserted");
            return true;
        }
        else{
            log.error("Error during Load user data document insertion");
            return false;
        }

    }

    public String updateUserDocument(Map<String,Object> map){
        DB db = mongoClient.getDB("documents_db");
        DBCollection collection = db.getCollection("user_documents");

        Long userId = (Long)map.get("userId");
        Integer docId = (Integer)map.get("docId");

        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(new BasicDBObject("userId", userId));
        obj.add(new BasicDBObject("docId", docId));
        andQuery.put("$and", obj);


        DBObject updateObject = new BasicDBObject();
        updateObject.put("$set", new BasicDBObject("dob",map.get("dob"))
                                .append("gender", map.get("gender"))
                                .append("attachments", map.get("attachments")));

        collection.update(andQuery,updateObject);

        return "docId "+docId+" userId "+userId;


    }

    public DBObject getUserDocument(Long userId, Integer docId){

            DB db = mongoClient.getDB("documents_db");
            DBCollection collection = db.getCollection("user_documents");

            DBObject result = null;

            BasicDBObject andQuery = new BasicDBObject();
            List<BasicDBObject> obj = new ArrayList<>();
            obj.add(new BasicDBObject("userId", userId));
            obj.add(new BasicDBObject("docId", docId));
            andQuery.put("$and", obj);

            DBCursor cursor = collection.find(andQuery);
            if (cursor.hasNext()) {
                result = cursor.next();

                result.removeField("_id");
                log.info("{}",result);
                return result;
            }
            else{
               return result;
            }
    }

    public List<Long> getAllDocumentIdsByUserId(Long userId){
        List<Long> documentIDs = new ArrayList<>();
        DB db = mongoClient.getDB("documents_db");
        DBCollection collection = db.getCollection("user_documents");

        DBObject result = null;

        BasicDBObject query = new BasicDBObject();
        query.put("userId", userId);

        DBCursor cursor = collection.find(query);
        while (cursor.hasNext()) {
            result = cursor.next();
            Integer docId = (Integer) result.get("docId");
            Long docIdLong = Long.valueOf(docId);
            //Long docId = (long)result.get("docId");
            log.info("{}",docId);
            documentIDs.add(docIdLong);
        }
        return documentIDs;
    }


    public String updateUserDocument(Map<String,Object> map,Long userId, Integer docId){
        DB db = mongoClient.getDB("documents_db");
        DBCollection collection = db.getCollection("user_documents");

        DBObject result = null;

        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(new BasicDBObject("userId", userId));
        obj.add(new BasicDBObject("docId", docId));
        andQuery.put("$and", obj);

        DBCursor cursor = collection.find(andQuery);
        if (cursor.hasNext()) {
            result = cursor.next();

            collection.remove(result);
            collection.insert(new BasicDBObject(map));
            return "docId " + docId + " userId " + userId;
        }
        else{
            return null;
        }


    }


    public boolean deleteUserDocument(Long userId, Integer docId){
        DB db = mongoClient.getDB("documents_db");
        DBCollection collection = db.getCollection("user_documents");

        DBObject result = null;

        BasicDBObject andQuery = new BasicDBObject();
        List<BasicDBObject> obj = new ArrayList<>();
        obj.add(new BasicDBObject("userId", userId));
        obj.add(new BasicDBObject("docId", docId));
        andQuery.put("$and", obj);

        WriteResult remove = collection.remove(andQuery);
        if(remove.wasAcknowledged()){
            log.info("user document of userId "+userId+" docId"+docId+" is removed");
            return true;
        }
        else{
            log.error("Error during delete document");
            return false;
        }

    }
}
