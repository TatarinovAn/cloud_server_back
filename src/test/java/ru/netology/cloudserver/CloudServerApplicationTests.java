package ru.netology.cloudserver;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;


import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

import org.springframework.boot.test.web.client.TestRestTemplate;


@Testcontainers
@AutoConfigureMockMvc
@Transactional
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CloudServerApplicationTests {


    //@Container
    static PostgreSQLContainer<?> database = new PostgreSQLContainer<>("postgres")
            .withDatabaseName("netology")
            .withUsername("postgres")
            .withPassword("postgres");

    static {
        database.start();
        System.out.println("yes start");
    }

    @DynamicPropertySource
    public static void properties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", database::getJdbcUrl);
        registry.add("spring.datasource.username", database::getUsername);
        registry.add("spring.datasource.password", database::getPassword);
    }


    private static final GenericContainer<?> cloud_server = new GenericContainer<>("cloud_server:0.0.1")
            .dependsOn(database);

    static {
        cloud_server.start();
    }

    @org.junit.Test
    public void contextLoads() {
        Assertions.assertTrue(cloud_server.isRunning());
    }

    @org.junit.Test
    public void contextDatabase() {
        Assertions.assertTrue(database.isRunning());
    }
}
