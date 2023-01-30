package com.example.todonew;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.PostgreSQLContainer;

@RunWith(SpringRunner.class)
@SpringBootTest
class TodoNewApplicationTests {

	public static PostgreSQLContainer container = (PostgreSQLContainer) (new PostgreSQLContainer("postgres:14")
			.withDatabaseName("db")
			.withUsername("root")
			.withPassword("rootTest"))
			.withReuse(true);

	@Test
	void contextLoads() {
	}


}
