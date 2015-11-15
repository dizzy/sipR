package org.sipr.jpa.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("org.sipr.jpa.dao")
@PropertySource(value= "classpath:postgresql.properties", ignoreResourceNotFound = true)
public class PostgresqlConfiguration {
}
