package org.uchicago.regie;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DatabaseConnectionTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testDatabaseConnection() {
        // Executes a simple query to verify the connection
        String version = jdbcTemplate.queryForObject("SELECT version();", String.class);
        assertThat(version).isNotNull();
        System.out.println("Database version: " + version);
    }
}
