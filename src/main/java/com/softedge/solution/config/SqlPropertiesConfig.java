package com.softedge.solution.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:./sql/sql.properties")
public class SqlPropertiesConfig {

}
