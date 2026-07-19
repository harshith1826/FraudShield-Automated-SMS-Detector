package com.fraudshield.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoAuditing
@EnableMongoRepositories(basePackages = "com.fraudshield.repository")
public class MongoConfig {
    // Spring Boot auto-configures MongoDB via application.properties.
    // This class enables auditing and repository scanning explicitly.
}