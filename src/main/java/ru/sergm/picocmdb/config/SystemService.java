package ru.sergm.picocmdb.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
@PropertySource("classpath:errorcodes.properties")
public class SystemService {

	@Autowired
	private Environment env;


	public String getErrorCode(String exceptionName) {
		String result = env.getProperty(exceptionName);
		if (result == null) return ""; else return result;
	}

}
