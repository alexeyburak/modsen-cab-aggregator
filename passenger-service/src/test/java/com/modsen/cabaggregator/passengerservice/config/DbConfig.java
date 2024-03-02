package com.modsen.cabaggregator.passengerservice.config;

import jakarta.annotation.PreDestroy;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

public class DbConfig {

    private static final String DOCKER_IMAGE = "postgres:11.1";
    private static final String DB_NAME = "test-db";
    private static final String DB_USERNAME = "username";
    private static final String DB_PASSWORD = "password";

    public static PostgreSQLContainer mysqlContainer = new PostgreSQLContainer(DOCKER_IMAGE)
            .withDatabaseName(DB_NAME)
            .withUsername(DB_USERNAME)
            .withPassword(DB_PASSWORD);

    static {
        mysqlContainer.start();
    }

    @PreDestroy
    public static void stopContainer() {
        mysqlContainer.stop();
    }

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mysqlContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mysqlContainer::getUsername);
        registry.add("spring.datasource.password", mysqlContainer::getPassword);
    }

}
