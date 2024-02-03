package com.softedge.solution.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Repository
public class CompanyRepositoryImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(@Qualifier("core-db") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }


    public Long getCompanyIdByUsername(String username){
        String sql = "SELECT cp.id from company_profile cp, " +
                " user_tbl ut," +
                "user_company_mapping ucm " +
                "where ut.id = ucm.user_id " +
                "and ucm.company_id = cp.id " +
                "and ut.email_id=:username ";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", username);
        Long id = namedParameterJdbcTemplate.queryForObject(sql,parameters, Long.class);
        return id;
    }

    public String getCompanyNameByCompanyId(Long companyId){
        String sql = "SELECT cp.company_name from company_profile cp " +
                "where cp.id= :companyId ";
        Map<String, Long> parameters = new HashMap<>();
        parameters.put("companyId", companyId);
        String companyName = namedParameterJdbcTemplate.queryForObject(sql,parameters, String.class);
        return companyName;
    }
}
