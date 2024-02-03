package com.softedge.solution.repository.impl;

import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.repomodels.KycDocumentDetails;
import com.softedge.solution.repomodels.NotificationDetails;
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

@Repository
@Transactional
public class NotificationRepositoryImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${insert.kyc.notification.sql}")
    private String insertKycNotificationSql;

    @Autowired
    public void setDataSource(@Qualifier("core-db") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }



    @Transactional(propagation = Propagation.REQUIRED)
    public Long notificationKycDocumentSave(NotificationDetails notificationDetails) throws KycDocumentGenericModuleException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            logger.info("Executing the query for inserting the kyc documents details -> {}", insertKycNotificationSql);
            SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(notificationDetails);
            namedParameterJdbcTemplate.update(insertKycNotificationSql, fileParameters, keyHolder);
            return keyHolder.getKey().longValue();
        }catch (Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }


}
