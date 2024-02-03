package com.softedge.solution.repository.impl;

import com.softedge.solution.commons.AppConstants;
import com.softedge.solution.contractmodels.*;
import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.repomodels.KycDocumentDetails;
import com.softedge.solution.repomodels.UserKycStatusDetails;
import com.softedge.solution.repository.rowmappers.KycProcessDocumentDetailsCMRowMapper;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.sql.SQLDataException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class KycDocumentDetailsReposiotoryImpl {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${insert.kyc.request.user.document.sql}")
    private String insertKycRequestDocumentSql;

    @Value("${update.kyc.process.status.document.sql}")
    private String updateprocessStatusDocumentSql;

    @Value("${list.of.requested.clients.sql}")
    private String getRequestedCompaniesSql;

    @Value("${is.user.documents.requestBy.clientSql}")
    private String isUserDocumentRequestedByClient;

    @Value("${get.kyc.user.status.count.sql}")
    private String isUserKYCStatusByClientSql;

    @Value("${update.kyc.user.status.sql}")
    private String updateUserKycStatusByClientSql;

    @Value("${insert.kyc.user.status.sql}")
    private String insertUserKycStatusByClientSql;

    @Value("${get.kyc.user.status.sql}")
    private String getUserKycStatusDetailsSql;

    @Value("${user.kyc.status.summary.dashboard.sql}")
    private String userKycStatusSummaryDashboardSql;



    @Autowired
    public void setDataSource(@Qualifier("kyc-db") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public Long requestKycDocumentSave(KycDocumentDetails kycDocumentDetails) throws KycDocumentGenericModuleException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            logger.info("Executing the query for inserting the kyc documents details -> {}", insertKycRequestDocumentSql);
            SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(kycDocumentDetails);
            namedParameterJdbcTemplate.update(insertKycRequestDocumentSql, fileParameters, keyHolder);
            return keyHolder.getKey().longValue();
        }catch (Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long insertUserKycStatus(UserKycStatusDetails userKycStatusDetails) throws KycDocumentGenericModuleException {
        try{
            KeyHolder keyHolder = new GeneratedKeyHolder();
            logger.info("Executing the query for inserting the kyc user status details -> {}", insertUserKycStatusByClientSql);
            SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(userKycStatusDetails);
            namedParameterJdbcTemplate.update(insertUserKycStatusByClientSql, fileParameters, keyHolder);
            return keyHolder.getKey().longValue();
        }catch (Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long updateUserKycStatus(UserKycStatusDetails userKycStatusDetails)throws KycDocumentGenericModuleException {
        try {
            logger.info("Executing the query for updating the kyc user status details > {}", updateUserKycStatusByClientSql);
            SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(userKycStatusDetails);
            int row = namedParameterJdbcTemplate.update(updateUserKycStatusByClientSql, fileParameters);
            return (long) row;
        } catch(Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }

    public boolean isUserkycStatusByClientExist(Long userId, Long companyId)throws KycDocumentGenericModuleException {
        try{
            Map<String, Long> parameters = new HashMap<>();
            parameters.put("userId", userId);
            parameters.put("companyId", companyId);
            return namedParameterJdbcTemplate.queryForObject(isUserKYCStatusByClientSql, parameters, boolean.class);

        }catch (Exception e) {
            logger.error("Error {}", e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
    }

    public UserKycStatusDetails getUserKycStatusByClientId(Long userId, Long companyId) throws KycDocumentGenericModuleException {
        UserKycStatusDetails userKycStatusDetails = new UserKycStatusDetails();
        try {
            Map<String, Long> parameters = new HashMap<>();
            parameters.put("userId", userId);
            parameters.put("companyId", companyId);
            userKycStatusDetails = (UserKycStatusDetails) namedParameterJdbcTemplate.queryForObject(getUserKycStatusDetailsSql, parameters, new RowMapper<UserKycStatusDetails>() {
                @Override
                public UserKycStatusDetails mapRow(ResultSet resultSet, int i) throws SQLException {
                    UserKycStatusDetails userKycStatusDetails = new UserKycStatusDetails();
                    userKycStatusDetails.setId(resultSet.getLong("id"));
                    userKycStatusDetails.setCompanyId(resultSet.getLong("company_id"));
                    userKycStatusDetails.setUserId(resultSet.getLong("user_id"));
                    userKycStatusDetails.setCreatedAt(resultSet.getDate("created_at"));
                    userKycStatusDetails.setCreatedBy(resultSet.getString("created_by"));
                    userKycStatusDetails.setModifiedAt(resultSet.getDate("modified_at"));
                    userKycStatusDetails.setModifiedBy(resultSet.getString("modified_by"));
                    userKycStatusDetails.setStatus(resultSet.getString("status"));
                    userKycStatusDetails.setUnregisteredUser(resultSet.getBoolean("unregistered_user"));
                    return userKycStatusDetails;
                }
            });
        }
        catch (EmptyResultDataAccessException e){
            logger.warn("Warning {}", e);
            return userKycStatusDetails;
        }
        catch (Exception e) {
            logger.error("Error {}", e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
        return userKycStatusDetails;
    }


    public List<KycProcessDocumentDetailsCM> getKycDocumentDetailsById(List<Long> id)throws KycDocumentGenericModuleException {
        try {
            String sql = "select * from kyc_document_details_view where id IN (:id)";
            Map<String, List<Long>> parameter = Collections.singletonMap("id", id);

            return namedParameterJdbcTemplate.query(sql, parameter, new KycProcessDocumentDetailsCMRowMapper());
        }catch(Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }

    public List<KycProcessDocumentDetailsCM> getKycDocumentDetailsByCompanyIdAndUserId(Long companyId, Long userId) throws KycDocumentGenericModuleException {
        try {
            String sql = "select * from kyc_document_details_view where requestee_userid =:userId AND company_id=:companyId";
            Map<String, Long> parameter = new HashMap<>();
            parameter.put("userId", userId);
            parameter.put("companyId", companyId);
            return namedParameterJdbcTemplate.query(sql, parameter, new KycProcessDocumentDetailsCMRowMapper());
        }catch(Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public Long updateProcessStatusDocuments(KycDocumentProcessStateCM kycDocumentProcessStateCM)throws KycDocumentGenericModuleException {
        try {
            logger.info("Executing the query for update > {}", updateprocessStatusDocumentSql);
            SqlParameterSource fileParameters = new BeanPropertySqlParameterSource(kycDocumentProcessStateCM);
            int row = namedParameterJdbcTemplate.update(updateprocessStatusDocumentSql, fileParameters);
            return (long) row;
        } catch(Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }

    public List<UploadedDocumentsCM> getKycDocumentDetailsByUserId(Long userId, List<Long> docIds) throws KycDocumentGenericModuleException {
        try {
            String sql = "select * from kyc_db.documents_meta dm \n" +
                    "left join \n" +
                    "(select distinct kd.document_id, true as is_shared from kyc_db.kyc_documents kd \n" +
                    "where kd.requestee_userid = :userId) as kd \n" +
                    "on kd.document_id = dm.id \n" +
                    "where dm.id in (:ids)";
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("userId", userId);
            parameter.put("ids", docIds);
            return namedParameterJdbcTemplate.query(sql, parameter,new RowMapper<UploadedDocumentsCM>() {
                @Override
                public UploadedDocumentsCM mapRow(ResultSet resultSet, int i) throws SQLException {
                    UploadedDocumentsCM kycDocumentMeta = new UploadedDocumentsCM();
                    kycDocumentMeta.setId(resultSet.getLong("id"));
                    kycDocumentMeta.setDocumentName(resultSet.getString("document_name"));
                    kycDocumentMeta.setDocumentLogo(resultSet.getString("document_logo"));
                    kycDocumentMeta.setDocumentType(resultSet.getString("document_type"));
                    kycDocumentMeta.setDocumentDesc(resultSet.getString("DOCUMENT_DESC"));
                    kycDocumentMeta.setShared(resultSet.getBoolean("is_shared"));
                    return kycDocumentMeta;
                }
            });
        }catch(Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentDetailsReposiotoryImpl.class);
        }
    }

    public List<RequestedCompaniesCM> getRequestedCompanies(Long userId)throws KycDocumentGenericModuleException {
        try {
            Map<String, Object> parameter = new HashMap<>();
            parameter.put("userId", userId);
            return namedParameterJdbcTemplate.query(getRequestedCompaniesSql, parameter,new RowMapper<RequestedCompaniesCM>() {
                @Override
                public RequestedCompaniesCM mapRow(ResultSet resultSet, int i) throws SQLException {
                    RequestedCompaniesCM kycDocumentMeta = new RequestedCompaniesCM();
                    kycDocumentMeta.setCompanyId(resultSet.getLong("id"));
                    kycDocumentMeta.setCompanyName(resultSet.getString("company_name"));
                    kycDocumentMeta.setLogo(resultSet.getString("logo_url"));
                    kycDocumentMeta.setNotificationCount(resultSet.getString("notification_count"));
                    kycDocumentMeta.setLastRequestedDate(resultSet.getDate("last_requested_date"));
                    kycDocumentMeta.setKycStatus(resultSet.getString("kyc_status"));
                    return kycDocumentMeta;
                }
            });
        }catch (Exception e) {
            logger.error("Error {}", e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
    }

    public boolean getUserDocumentRequestedByClient(Long requesteeUserId, Long companyId, Long docId)throws KycDocumentGenericModuleException {
        try{
            Map<String, Long> parameters = new HashMap<>();
            parameters.put("requesteeUserId", requesteeUserId);
            parameters.put("companyId", companyId);
            parameters.put("docId", docId);
            return namedParameterJdbcTemplate.queryForObject(isUserDocumentRequestedByClient, parameters, boolean.class);

        }catch (Exception e) {
            logger.error("Error {}", e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
    }

    public Boolean deleteKycDocument(Long kycId, Long companyId) throws KycDocumentGenericModuleException {

        try {
            Map<String, Long> parameter = new HashMap<>();
            parameter.put("id", kycId);

            String sql = "delete from kyc_db.kyc_documents " +
                    "where id=:id and company_id= :companyId";
            logger.info("Executing the delete query for kyc documents -> {}", sql);
            int row = namedParameterJdbcTemplate.update(sql, parameter);
            if(row>0){
                return true;
            }
            else{
                return false;
            }
        }catch (Exception e) {
            logger.error("Error {}", e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }

    }

    public ClientDashboardUserKycStatusSummaryCM getClientDashboardUserCounts(Long companyId, boolean isAdmin) throws KycDocumentGenericModuleException {
        ClientDashboardUserKycStatusSummaryCM clientDashboardUserKycStatusSummaryCM = null;
        try {
            String finalSql =null;
            if(companyId==null && isAdmin){
                String queryString = AppConstants.SPACE;
                finalSql = StringUtils.replace(userKycStatusSummaryDashboardSql, "{0}", queryString);
            }else{
                String queryString = AppConstants.SPACE_WHERE_SPACE+"company_id=:company_id";
                finalSql = StringUtils.replace(userKycStatusSummaryDashboardSql, "{0}", queryString);
            }
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("company_id", companyId);
            clientDashboardUserKycStatusSummaryCM = namedParameterJdbcTemplate.queryForObject(finalSql, parameters, new RowMapper<ClientDashboardUserKycStatusSummaryCM>() {
                @Override
                public ClientDashboardUserKycStatusSummaryCM mapRow(ResultSet resultSet, int i) throws SQLException {
                    ClientDashboardUserKycStatusSummaryCM kycStatusSummaryCM = new ClientDashboardUserKycStatusSummaryCM();
                    kycStatusSummaryCM.setApproved(resultSet.getLong("approved_count"));
                    kycStatusSummaryCM.setRejected(resultSet.getLong("rejected_count"));
                    kycStatusSummaryCM.setPending(resultSet.getLong("pending_count"));
                    kycStatusSummaryCM.setRequested(resultSet.getLong("requested_count"));
                    return kycStatusSummaryCM;

                }
            });
            return clientDashboardUserKycStatusSummaryCM;
        }catch (EmptyResultDataAccessException e){
            logger.error("KYC Status is empty {}", e);
            return clientDashboardUserKycStatusSummaryCM;
        }catch (Exception e) {
            logger.error("Error {}", e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
    }




}
