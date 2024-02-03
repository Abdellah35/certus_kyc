package com.softedge.solution.repository.impl;

import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.repomodels.TempUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Repository
@Transactional
public class TempUserRepositoryImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(@Qualifier("core-db") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long saveTempUser(TempUser tempUser)  throws KycDocumentGenericModuleException {
        try {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            String sql = "INSERT INTO certus_core_db.temp_user_tbl " +
                    "(name, email_id," +
                    "company_id, created_by, created_at) " +
                    "VALUES(:name, :emailId, :companyId, " +
                    ":createdBy, :createdAt)";
            logger.info("Executing the query to save temp user table -> {}", sql);
            SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(tempUser);
            int addedId = namedParameterJdbcTemplate.update(sql, fileParameters);
            return keyHolder.getKey().longValue();
           // return (long) addedId;
        } catch (Exception e) {
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, TempUserRepositoryImpl.class);
        }
    }


    public Boolean deleteTempUser(Long id) throws KycDocumentGenericModuleException {

        try {
            Map<String, Long> parameter = new HashMap<>();
            parameter.put("id", id);

            String sql = "delete from certus_core_db.temp_user_tbl " +
                    "where id=:id";
            logger.info("Executing the delete query for temp user tbl -> {}", sql);
            int row = namedParameterJdbcTemplate.update(sql, parameter);
            if(row>0){
                return true;
            }
            else{
                return false;
            }

        } catch (Exception e) {
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, TempUserRepositoryImpl.class);
        }

    }



    public TempUser getTempUserByEmailId(String emailId) throws KycDocumentGenericModuleException{
        TempUser tempUser = null;
        try {
            Map<String, String> parameter = new HashMap<>();
            parameter.put("emailId", emailId);
            String tmpUserDetailsSql = "select * " +
                    "from " +
                    "temp_user_tbl tut " +
                    "where " +
                    "tut.email_id =:emailId";
            tempUser = namedParameterJdbcTemplate.queryForObject(tmpUserDetailsSql, parameter, new RowMapper<TempUser>() {

                @Override
                public TempUser mapRow(ResultSet resultSet, int i) throws SQLException {
                    TempUser user = new TempUser();
                    user.setEmailId(resultSet.getString("email_id"));
                    user.setName(resultSet.getString("name"));
                    user.setId(resultSet.getLong("id"));
                    user.setCreatedBy(resultSet.getString("created_by"));
                    user.setCreatedAt(resultSet.getDate("created_at"));
                    return user;
                }
            });

        } catch (Exception e) {
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, TempUserRepositoryImpl.class);
        }
        return tempUser;
    }


    public TempUser getTempUserById(Long id) throws KycDocumentGenericModuleException{
        TempUser tempUser = null;
        try {
            Map<String, Long> parameter = new HashMap<>();
            parameter.put("id", id);
            String tmpUserDetailsSql = "select * " +
                    "from " +
                    "temp_user_tbl tut " +
                    "where " +
                    "tut.id =:id";
            tempUser = namedParameterJdbcTemplate.queryForObject(tmpUserDetailsSql, parameter, new RowMapper<TempUser>() {

                @Override
                public TempUser mapRow(ResultSet resultSet, int i) throws SQLException {
                    TempUser user = new TempUser();
                    user.setEmailId(resultSet.getString("email_id"));
                    user.setName(resultSet.getString("name"));
                    user.setId(resultSet.getLong("id"));
                    user.setCreatedBy(resultSet.getString("created_by"));
                    user.setCreatedAt(resultSet.getDate("created_at"));
                    return user;
                }
            });

        } catch (Exception e) {
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, TempUserRepositoryImpl.class);
        }
        return tempUser;
    }


}
