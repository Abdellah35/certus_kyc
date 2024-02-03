package com.softedge.solution.repository.rowmappers;

import com.softedge.solution.contractmodels.KycProcessDocumentDetailsCM;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class KycProcessDocumentDetailsCMRowMapper implements RowMapper<KycProcessDocumentDetailsCM> {
    @Override
    public KycProcessDocumentDetailsCM mapRow(ResultSet resultSet, int i) throws SQLException {
        KycProcessDocumentDetailsCM kycProcessDocumentDetailsCM = new KycProcessDocumentDetailsCM();
        kycProcessDocumentDetailsCM.setCountryCode(resultSet.getString("country_code"));
        kycProcessDocumentDetailsCM.setCountryName(resultSet.getString("country_name"));
        kycProcessDocumentDetailsCM.setCreatedBy(resultSet.getString("created_by"));
        kycProcessDocumentDetailsCM.setCreatedDate(resultSet.getDate("created_date"));
        kycProcessDocumentDetailsCM.setDocumentDesc(resultSet.getString("document_desc"));
        kycProcessDocumentDetailsCM.setDocumentId(resultSet.getLong("document_id"));
        kycProcessDocumentDetailsCM.setDocumentLogo(resultSet.getString("document_logo"));
        kycProcessDocumentDetailsCM.setDocumentName(resultSet.getString("document_name"));
        kycProcessDocumentDetailsCM.setDocumentType(resultSet.getString("document_type"));
        kycProcessDocumentDetailsCM.setId(resultSet.getLong("id"));
        kycProcessDocumentDetailsCM.setModifiedBy(resultSet.getString("modified_by"));
        kycProcessDocumentDetailsCM.setModifiedDate(resultSet.getDate("modified_date"));
        kycProcessDocumentDetailsCM.setProcessStatus(resultSet.getString("process_status"));
        kycProcessDocumentDetailsCM.setRequestorUserId(resultSet.getLong("requestor_userid"));
        kycProcessDocumentDetailsCM.setRequesteeUserId(resultSet.getLong("requestee_userid"));
        kycProcessDocumentDetailsCM.setCompanyId(resultSet.getLong("company_id"));
        kycProcessDocumentDetailsCM.setRequesteeUsername(resultSet.getString("requestee_username"));
        kycProcessDocumentDetailsCM.setRequestorUsername(resultSet.getString("requestor_username"));
        kycProcessDocumentDetailsCM.setRequesteeEmailId(resultSet.getString("requestee_email_id"));
        return kycProcessDocumentDetailsCM;
    }
}
