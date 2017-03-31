package ru.sergm.picocmdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class PicocmdbApplication
		//to run on Tomcat
		extends SpringBootServletInitializer{

	private static final Logger LOG = LoggerFactory.getLogger(PicocmdbApplication.class);


	// to run on Tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PicocmdbApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication.run(PicocmdbApplication.class, args);
		//LOG.info("DATABASE_URL environment variable set to: {}", System.getenv("DATABASE_URL"));
		//LOG.info("JDBC_DATABASE_URL environment variable set to: {}", System.getenv("JDBC_DATABASE_URL"));
	}
}
