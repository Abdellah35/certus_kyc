package com.softedge.solution.repository.impl;


import com.mongodb.*;
import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.errorcodes.ErrorCodeKYCEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
@Slf4j
public class MetadataDocumentDetailsRepositoryImpl {


    @Autowired
    @Qualifier("documents-db")
    private MongoClient mongoClient;


    public boolean loadMetadataDocument(Map<String,Object> map){
        DB db = mongoClient.getDB("documents_db");
        DBCollection collection = db.getCollection("documents_meta_mtb");
        WriteResult insert = collection.insert(new BasicDBObject(map));
        if(insert.wasAcknowledged()){
            log.info("Load meta data document inserted");
            return true;
        }
        else{
            log.error("Error during Load meta data document insertion");
            return false;
        }

    }

    public DBObject getMetadataDocumentById(Long docId) {
        DBObject document=null;
        try{
            DB db = mongoClient.getDB("documents_db");
            DBCollection collection = db.getCollection("documents_meta_mtb");
            BasicDBObject query = new BasicDBObject();

            query.put("docId", docId);
            DBCursor cur = collection.find(query);

            if (cur.hasNext()) {
                document = cur.next();
                document.removeField("_id");
                return document;
            }
            else {
                throw new KycDocumentGenericModuleException(ErrorCodeKYCEnum.DOCUMENT_NOT_FOUNT,
                        "Document with id "+docId+" not found");
            }
        }
        catch(Exception e){
            log.error("Document with id {} not found", docId);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
    }
}
