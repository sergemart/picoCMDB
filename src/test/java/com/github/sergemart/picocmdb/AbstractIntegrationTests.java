package com.github.sergemart.picocmdb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;

import java.lang.management.ManagementFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.github.sergemart.picocmdb.junitrule.JdbcCleaner;
import com.github.sergemart.picocmdb.system.SystemService;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)		// start embedded tomcat on random free port
public abstract class AbstractIntegrationTests {

	private static AtomicInteger counter = new AtomicInteger(0);
	protected String baseUiUrl;
	protected String baseRestUrl;
	@Autowired
	protected JdbcTemplate jdbcTemplate;
	@Autowired
	protected TestRestTemplate restTemplate;
	@Autowired
	protected SystemService systemService;
	@Rule
	public final JdbcCleaner jdbcCleaner = new JdbcCleaner();


	// to make somewhat unique entities' IDs to prevent constraint violation when test executed in parallel
	protected String getSalt() {
		return String.valueOf(counter.incrementAndGet() + "@" + ManagementFactory.getRuntimeMXBean().getName());
	}


	@Before
	public void basicSetUp() { // attn: do not override
		// inject beans to rules (rules are instantiated before Spring context loads)
		jdbcCleaner.setJdbcTemplate(this.jdbcTemplate);
	}


}
