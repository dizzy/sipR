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
public class CouchDBConfiguration {
    private static final Logger LOGGER = LoggerFactory.getLogger(CouchDBConfiguration.class);

    @Value("${couchdb.uri}")
    String uri;

    @Value("${couchdb.user.database.name}")
    String userDbName;

    @Value("${couchdb.bindings.database.name}")
    String bindingsDbName;

    @Bean
    public HttpClient couchDBHttpClient() {
        HttpClient httpClient = null;
        try {
            httpClient = new StdHttpClient.Builder()
                    .url(uri)
                    .build();
        } catch (Exception e) {
            LOGGER.error("Cannot create HTTP Client");
            return null;
        }
        return httpClient;
    }

    @Bean
    public CouchDbConnector couchDBUserConnector() {
        return couchDBConnector(userDbName);
    }

    @Bean
    public CouchDbConnector couchDBBindingsConnector() {
        return couchDBConnector(bindingsDbName);
    }

    private CouchDbConnector couchDBConnector(String databaseName) {
        LOGGER.info("Create couchDB connector");
        CouchDbConnector db = null;
        try {
            CouchDbInstance dbInstance = new StdCouchDbInstance(couchDBHttpClient());
            db = new StdCouchDbConnector(databaseName, dbInstance);

            db.createDatabaseIfNotExists();
        } catch (Exception e) {
            LOGGER.error("Cannot connect to couchDB", e);
            return null;
        }
        return db;
    }

    @Bean
    public CouchDBRegistrationsRepository registrationsRepository() {
        return new CouchDBRegistrationsRepository(couchDBBindingsConnector());
    }

    @Bean
    public CouchDBUserRepository usersRepository() {
        return new CouchDBUserRepository(couchDBUserConnector());
    }
}
