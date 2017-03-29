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


	public String getLocalizedErrorMessage(String exceptionName, String errorName) {
		String result = "Undefined error occured."; // default localized error message

		// empty parameters yield default message
		if (exceptionName == "" || exceptionName == null) return result;
		if (errorName == "" || errorName == null) return result;

		String messageId = exceptionName + "." + errorName;
		try {
			result = errorMessages.getErrorMessage(messageId);
		} catch (NoSuchMessageException e) {
			// 'no such message' yield default message
			return result;
		}
		return result;
	}


	public RestError getRestError(BaseException e) {
		if (e == null) return new RestError(null, "", this.getLocalizedErrorMessage(null, null));
		String errorCode = this.getErrorCode(e.getExceptionName());
		String localizedErrorMessage = this.getLocalizedErrorMessage(e.getExceptionName(), e.getErrorName());
		return new RestError(e, errorCode, localizedErrorMessage);
	}

}
