package ru.sergm.picocmdb.system;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import ru.sergm.picocmdb.exception.BaseException;


@Service
@PropertySource("classpath:errorcodes.properties")
public class SystemService {

	@Autowired
	private Environment env;
	@Autowired
	private ErrorMessages errorMessages;


	public String getErrorCode(String exceptionName) {
		String result = env.getProperty(exceptionName);
		if (result == null) return ""; else return result;
	}


	public String getErrorMessage(String errorName) {
		String result;
		try {
			result = errorMessages.getErrorMessage(errorName);
		} catch (NoSuchMessageException e) {
			return "Undefined error occured.";
		}
		return result;
	}


	public RestError getRestError(BaseException e) {
		if (e == null) return new RestError(null, "");
		String errorCode = this.getErrorCode(e.getClass().getCanonicalName());
		return new RestError(e, errorCode);
	}

}
