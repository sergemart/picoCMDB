package ru.sergm.picocmdb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan
@EnableAutoConfiguration
public class PicocmdbApplication
		//to run on Tomcat
		extends SpringBootServletInitializer{

	// to run on Tomcat
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(PicocmdbApplication.class);
	}


	public static void main(String[] args) {
		SpringApplication.run(PicocmdbApplication.class, args);
	}
}
