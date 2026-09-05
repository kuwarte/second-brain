package com.kuwarte.api;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.postgresql.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {

    @Bean
    @ServiceConnection
    PostgreSQLContainer postgresContainer() {
        // V02__enable_pgvector.sql runs `CREATE EXTENSION vector`, which a plain
        // postgres image does not ship with. Flyway migration fails against
        // vanilla postgres:latest, so every test in the suite fails at context
        // startup. The pgvector image matches docker-compose.yml and actually
        // has the extension available.
        return new PostgreSQLContainer(DockerImageName.parse("pgvector/pgvector:pg16")
                .asCompatibleSubstituteFor("postgres"));
    }

}
