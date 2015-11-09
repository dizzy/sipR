package org.sipr.couchdb.config;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.sipr.couchdb.dao.CouchDBRegistrationsRepository;
import org.sipr.couchdb.dao.CouchDBUserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource(value= "classpath:couchdb.properties", ignoreResourceNotFound = true)
@Profile("couchdb")
public class CouchDBConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouchDBConfiguration.class);

    @Value("${couchdb.uri}")
    String uri;

    @Value("${couchdb.database.name}")
    String dbName;

    @Bean
    public CouchDbConnector couchDBConnector() {
        LOGGER.info("Create couchDB connector");
        HttpClient httpClient = null;
        CouchDbConnector db = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url(uri)
                    .build();
            CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
            db = new StdCouchDbConnector(dbName, dbInstance);

            db.createDatabaseIfNotExists();
        } catch (Exception e) {
            LOGGER.error("Cannot connect to couchDB", e);
            return null;
        }
        return db;
    }

    @Bean
    public CouchDBRegistrationsRepository registrationsRepository() {
        return new CouchDBRegistrationsRepository(couchDBConnector());
    }

    @Bean
    public CouchDBUserRepository usersRepository() {
        return new CouchDBUserRepository(couchDBConnector());
    }
}
