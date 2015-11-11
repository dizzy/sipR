package org.sipr.cassandra.config;

import com.datastax.driver.core.Session;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cassandra.config.java.AbstractCqlTemplateConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.repository.config.EnableCassandraRepositories;

@Configuration
@EnableCassandraRepositories("org.sipr.cassandra.dao")
@PropertySource(value= "classpath:cassandra.properties", ignoreResourceNotFound = true)
public class CassandraConfiguration extends AbstractCqlTemplateConfiguration {

    @Value("${cassandra.contactpoints}")
    String contactpoints;

    @Value("${cassandra.keyspace}")
    String keyspace;

    @Value("${cassandra.port}")
    Integer port;

    @Override
    public String getContactPoints() {
        return contactpoints;
    }

    @Override
    public String getKeyspaceName() {
        return keyspace;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Bean
    public CassandraTemplate cassandraTemplate(Session session) {
        return new CassandraTemplate(session);
    }
}
