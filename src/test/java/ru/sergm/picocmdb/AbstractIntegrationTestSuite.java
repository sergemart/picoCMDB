package ru.sergm.picocmdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.runner.RunWith;

import javax.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicInteger;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)		// start embedded tomcat on random free port
public abstract class AbstractIntegrationTestSuite {

	private static AtomicInteger counter = new AtomicInteger(0);

	@Autowired
	protected JdbcTemplate jdbcTemplate;

	//@Autowired
	//protected EntityManager em;


	// to make unique entities' IDs to prevent constraint violation when test executed in parallel
	protected String getSalt() {
		return String.valueOf(counter.incrementAndGet());
	}

}
