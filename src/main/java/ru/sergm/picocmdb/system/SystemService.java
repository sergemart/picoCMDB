package ru.sergm.picocmdb.system;

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


	public RestError getRestError(Exception e) {
		if (e == null) return new RestError(null, "");
		String errorCode = this.getErrorCode(e.getClass().getCanonicalName());
		return new RestError(e, errorCode);
	}

}
