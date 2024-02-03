package com.softedge.solution.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:./csv/csv.properties")
public class CsvPropertiesConfig {

}
