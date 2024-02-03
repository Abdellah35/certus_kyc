package com.softedge.solution.repository.impl;

import com.softedge.solution.contractmodels.KycDocumentMetaCM;
import com.softedge.solution.contractmodels.KycDocumentMetaOverviewCM;
import com.softedge.solution.exceptionhandlers.custom.kyc.KycDocumentGenericModuleException;
import com.softedge.solution.exceptionhandlers.GenericExceptionHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@Transactional
public class KycDocumentMetaRepositoryImpl {


    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${kyc.document.meta.sql}")
    private String kycDocumentMetaSql;

    @Value("${kyc.document.client.overview}")
    private String kycDocumentClientOveriew;

    @Value("${kyc.document.details.meta.sql}")
    private String kycDocumentMetaByIdSql;


    @Autowired
    public void setDataSource(@Qualifier("kyc-db") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<KycDocumentMetaCM> getAllDocumentsMeta(String emailId, List<Long> docIds) throws KycDocumentGenericModuleException {
        List<KycDocumentMetaCM> kycDocumentMetas = new ArrayList<>();
        try{
            Map<String, Object> parameters = new HashMap<>();
            parameters.put("emailId", emailId);
            parameters.put("ids", docIds);
            kycDocumentMetas = namedParameterJdbcTemplate.query(kycDocumentMetaSql, parameters, new RowMapper<KycDocumentMetaCM>() {
                @Override
                public KycDocumentMetaCM mapRow(ResultSet resultSet, int i) throws SQLException {
                    KycDocumentMetaCM kycDocumentMeta = new KycDocumentMetaCM();
                    kycDocumentMeta.setId(resultSet.getLong("id"));
                    kycDocumentMeta.setDocumentName(resultSet.getString("document_name"));
                    kycDocumentMeta.setDocumentLogo(resultSet.getString("document_logo"));
                    kycDocumentMeta.setDocumentType(resultSet.getString("document_type"));
                    kycDocumentMeta.setCountryName(resultSet.getString("COUNTRY_NAME"));
                    kycDocumentMeta.setCountryCode(resultSet.getString("COUNTRY_CODE"));
                    kycDocumentMeta.setDocumentDesc(resultSet.getString("DOCUMENT_DESC"));
                    return kycDocumentMeta;
                }
            });
        }catch (Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
        return kycDocumentMetas;
    }

    public List<KycDocumentMetaOverviewCM> getAllDocumentsOverview(String emailId) throws KycDocumentGenericModuleException {
        List<KycDocumentMetaOverviewCM> kycDocumentMetas = new ArrayList<>();
        try{
            Map<String, String> parameters = new HashMap<>();
            parameters.put("emailId", emailId);
            kycDocumentMetas = namedParameterJdbcTemplate.query(kycDocumentClientOveriew, parameters, new RowMapper<KycDocumentMetaOverviewCM>() {
                @Override
                public KycDocumentMetaOverviewCM mapRow(ResultSet resultSet, int i) throws SQLException {
                    KycDocumentMetaOverviewCM kycDocumentMeta = new KycDocumentMetaOverviewCM();
                    kycDocumentMeta.setId(resultSet.getLong("id"));
                    kycDocumentMeta.setDocumentName(resultSet.getString("document_name"));
                    kycDocumentMeta.setDocumentLogo(resultSet.getString("document_logo"));
                    kycDocumentMeta.setDocumentType(resultSet.getString("document_type"));
                    kycDocumentMeta.setDocumentDesc(resultSet.getString("DOCUMENT_DESC"));
                    return kycDocumentMeta;
                }
            });
        }catch (Exception e){
            logger.error("Error {}",e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
        return kycDocumentMetas;
    }


    public KycDocumentMetaCM getDocumentsMetaById(Long id) throws KycDocumentGenericModuleException {
        KycDocumentMetaCM kycDocumentMeta = new KycDocumentMetaCM();
        try {
            Map<String, Long> parameters = new HashMap<>();
            parameters.put("id", id);
            kycDocumentMeta = (KycDocumentMetaCM) namedParameterJdbcTemplate.queryForObject(kycDocumentMetaByIdSql, parameters, new RowMapper<KycDocumentMetaCM>() {
                @Override
                public KycDocumentMetaCM mapRow(ResultSet resultSet, int i) throws SQLException {
                    KycDocumentMetaCM kycDocumentMeta = new KycDocumentMetaCM();
                    kycDocumentMeta.setId(resultSet.getLong("id"));
                    kycDocumentMeta.setDocumentName(resultSet.getString("document_name"));
                    kycDocumentMeta.setDocumentLogo(resultSet.getString("document_logo"));
                    kycDocumentMeta.setDocumentType(resultSet.getString("document_type"));
                    kycDocumentMeta.setCountryName(resultSet.getString("COUNTRY_NAME"));
                    kycDocumentMeta.setCountryCode(resultSet.getString("COUNTRY_CODE"));
                    kycDocumentMeta.setDocumentDesc(resultSet.getString("DOCUMENT_DESC"));
                    return kycDocumentMeta;
                }
            });
        } catch (Exception e) {
            logger.error("Error {}", e);
            throw GenericExceptionHandler.exceptionHandler(e, KycDocumentMetaRepositoryImpl.class);
        }
        return kycDocumentMeta;
    }

}
