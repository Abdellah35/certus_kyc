package com.softedge.solution.repository.impl;


import com.softedge.solution.contractmodels.KycProcessDocumentDetailsCM;
import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.repomodels.TempKycDocumentDetails;
import com.softedge.solution.repository.rowmappers.KycProcessDocumentDetailsCMRowMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Repository
@Transactional
public class TempKycDocumentDetailsRepositoryImpl {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;





    @Autowired
    public void setDataSource(@Qualifier("kyc-db") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Long requestTempKycDocumentSave(TempKycDocumentDetails tempKycDocumentDetails) throws KycDocumentGenericModuleException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String insertKycRequestDocumentSql = "INSERT INTO kyc_db.temp_kyc_documents (company_id, requestor_userid, requestee_userid, document_id, created_at, created_by) VALUES(:companyId, :requestorUserId, :requesteeUserId, :documentId, :createdAt, :createdBy)";
            logger.info("Executing the query for inserting the kyc temp documents details -> {}", insertKycRequestDocumentSql);
            SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(tempKycDocumentDetails);
            namedParameterJdbcTemplate.update(insertKycRequestDocumentSql, fileParameters, keyHolder);
            return keyHolder.getKey().longValue();
        }catch (Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, TempKycDocumentDetailsRepositoryImpl.class);
        }
    }

    public List<KycProcessDocumentDetailsCM> getTempKycDocumentDetailsById(List<Long> id)throws KycDocumentGenericModuleException {
        try {
            String sql = "select * from temp_kyc_document_details_view where id IN (:id)";
            Map<String, List<Long>> parameter = Collections.singletonMap("id", id);

            return namedParameterJdbcTemplate.query(sql, parameter, new KycProcessDocumentDetailsCMRowMapper());
        }catch(Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, TempKycDocumentDetailsRepositoryImpl.class);
        }
    }

    public List<KycProcessDocumentDetailsCM> getTempKycDocumentDetailsByCompanyIdAndUserId(Long companyId, Long userId) throws KycDocumentGenericModuleException {
        try {
            String sql = "select * from temp_kyc_document_details_view where requestee_userid =:userId AND company_id=:companyId";
            Map<String, Long> parameter = new HashMap<>();
            parameter.put("userId", userId);
            parameter.put("companyId", companyId);
            return namedParameterJdbcTemplate.query(sql, parameter, new KycProcessDocumentDetailsCMRowMapper());
        }catch(Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, TempKycDocumentDetailsRepositoryImpl.class);
        }
    }


}
