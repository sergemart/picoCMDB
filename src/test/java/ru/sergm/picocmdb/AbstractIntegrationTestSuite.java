package ru.sergm.picocmdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.runner.RunWith;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)		// start embedded tomcat on random free port
public abstract class AbstractIntegrationTestSuite {

	@Autowired
	protected JdbcTemplate jdbcTemplate;


}
