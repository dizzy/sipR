package org.sipr.mongodb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@PropertySource(value= "classpath:mongodb.properties", ignoreResourceNotFound = true)
@EnableMongoAuditing
@Profile("mongodb")
public class MongoConfiguration {
}
