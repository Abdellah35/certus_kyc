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
public class UserRepositoryImpl {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private JdbcTemplate jdbcTemplate;
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Autowired
    public void setDataSource(@Qualifier("core-db") DataSource dataSource) {
        this.namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public Long getUserIdByUsername(String username){
        String sql = "SELECT getUserIdByUsername(:username) as id";
        Map<String, String> parameter = new HashMap<>();
        parameter.put("username", username);
        Long id = namedParameterJdbcTemplate.queryForObject(sql, parameter, Long.class);
        return id;
    }

    public String getNameByEmailId(String emailId){
        String sql = "SELECT getNameByEmailId(:emailId) as name";
        Map<String, String> parameter = new HashMap<>();
        parameter.put("emailId", emailId);
        String name = namedParameterJdbcTemplate.queryForObject(sql, parameter, String.class);
        return name;
    }
}
